import entities.ChipDenomination;
import entities.Dealer;
import entities.GameRules;
import entities.Player;
import frameworks_and_drivers.DeckApiService;
import frameworks_and_drivers.DeckProvider;
import frameworks_and_drivers.InMemoryGameDataAccess;
import gui.GamePanel;
import gui.Homepage;
import interface_adapter.update_balance.UpdateBalanceController;
import interface_adapter.update_balance.UpdateBalancePresenter;

import usecase.ExitRestartGame.*; 
import usecase.PlaceBetAndDeal.*;
import usecase.PlayerActions.*;
import usecase.StartGame.*;
import usecase.dealeraction.*;
import usecase.updatebalance.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class Main {
    
    private static final String PLAYER_ID = "mainPlayer"; 

    public static void main(String[] args) {
        
        // 1. Create GUI Components
        Homepage homepage = new Homepage();
        GamePanel gamePanel = new GamePanel();

        // 2. Initialize Core Data Layer
        DeckProvider deckService = new DeckApiService();
        GameRules rules = new GameRules(5.0, 500.0, 1.5, true, true, true, 
                                        Arrays.asList(ChipDenomination.FIVE, ChipDenomination.TEN, ChipDenomination.TWENTY));
        
        InMemoryGameDataAccess dataAccess = new InMemoryGameDataAccess(deckService, rules);
        Player player = dataAccess.getPlayer(PLAYER_ID);
        Dealer dealer = dataAccess.getDealer();

        // ----------------------------------------------------------------
        // 3. Initialize Presenters (Connect Use Case Output -> GUI)
        // ----------------------------------------------------------------
        
        // PlaceBetAndDeal Presenter: Update UI after dealing
        PlaceBetAndDealOutputBoundary placeBetPresenter = new PlaceBetAndDealOutputBoundary() {
            @Override
            public void present(PlaceBetAndDealOutputData outputData) {
                gamePanel.updatePlayerCards(outputData.getPlayerCards(), outputData.getPlayerTotal());
                gamePanel.updateDealerCards(outputData.getDealerCards(), outputData.getDealerVisibleTotal());
                gamePanel.updateBalance(outputData.getBalance());
                
                if (outputData.isBlackjack()) {
                    gamePanel.updateMessage("BLACKJACK! You win!");
                    gamePanel.enableGameButtons(false);
                } else {
                    gamePanel.updateMessage("Your turn: Hit, Stand or Double?");
                    gamePanel.enableGameButtons(true);
                }
            }
        };

        // PlayerAction Presenter: Update results after Hit/Stand/Double
        PlayerActionOutputBoundary playerActionPresenter = new PlayerActionOutputBoundary() {
            @Override
            public void present(PlayerActionOutputData outputData) {
                gamePanel.updateMessage(outputData.getMessage());
                
                // Refresh hand display (Note: Ideally fetch latest hand state from DataAccess or use OutputData)
                // OutputData only gives Total, reading directly from Player entity for card display (pragmatic approach)
                gamePanel.updatePlayerCards(player.getHand().getCards(), outputData.getHandTotal());
                
                if (outputData.isGameComplete()) {
                    gamePanel.updateMessage(outputData.getMessage()); // Show final result
                    gamePanel.updateDealerCards(dealer.getHand().getCards(), outputData.getDealerTotal());
                    gamePanel.updateBalance(player.getBalance()); // Refresh balance
                    gamePanel.enableGameButtons(false); // Lock buttons, wait for new round
                } else if (outputData.isBust()) {
                    gamePanel.updateMessage("Bust! You went over 21.");
                }
            }
        };

        // Exit/Restart Presenter
        ExitRestartGameOutputBoundary exitPresenter = new ExitRestartGameOutputBoundary() {
            @Override
            public void presentExitResult(ExitRestartGameOutputData outputData) {
                // Return to Homepage instead of System.exit(0)
                JOptionPane.showMessageDialog(gamePanel, "Returning to Main Menu...");
                gamePanel.setVisible(false);
                homepage.setVisible(true);
            }

            @Override
            public void presentRestartResult(ExitRestartGameOutputData outputData) {
                // Handle Restart (Reset Balance)
                gamePanel.updateBalance(outputData.getNewBalance());
                gamePanel.updateMessage("Game Restarted! Balance reset to $1000.");
                gamePanel.updatePlayerCards(java.util.Collections.emptyList(), 0);
                gamePanel.updateDealerCards(java.util.Collections.emptyList(), 0);
                gamePanel.enableGameButtons(false); // Reset buttons, wait for Deal
            }

            @Override
            public void presentError(String errorMessage) {
                JOptionPane.showMessageDialog(gamePanel, errorMessage);
            }
        };

        // StartGame Presenter (Initialization only)
        StartGameOutputBoundary startGamePresenter = outputData -> {
            gamePanel.updateMessage("Deck Shuffled. Please place your bet.");
            gamePanel.enableGameButtons(false); // Cannot act until bet placed
        };

        // ----------------------------------------------------------------
        // 4. Initialize Interactors & Controllers (Connect GUI Input -> Use Case)
        // ----------------------------------------------------------------

        // Dealer Action (Injected into PlayerAction)
        // No dedicated Presenter needed, PlayerActionInteractor handles final display
        DealerActionInputBoundary dealerInteractor = new DealerActionInteractor(dataAccess, null, dataAccess);
        DealerController dealerController = (DealerController) dealerInteractor;

        // Player Action
        PlayerActionInputBoundary playerActionInteractor = new PlayerActionInteractor(
            playerActionPresenter, dataAccess, dataAccess, dealerController
        );
        PlayerActionController playerActionController = new PlayerActionController(playerActionInteractor);

        // Place Bet
        PlaceBetAndDealInputBoundary placeBetInteractor = new PlaceBetAndDealInteractor(
            deckService, player, dealer, placeBetPresenter
        );
        PlaceBetAndDealController placeBetController = new PlaceBetAndDealController(placeBetInteractor);

        // Start Game
        StartGameInputBoundary startGameInteractor = new StartGameInteractor(
            startGamePresenter, deckService, player, dealer
        );
        StartGameController startGameController = new StartGameController(startGameInteractor);

        // Exit/Restart
        ExitRestartGameInputBoundary exitInteractor = new ExitRestartGameInteractor(
            exitPresenter, dataAccess, dataAccess
        );
        ExitRestartGameController exitController = new ExitRestartGameController(exitInteractor);


        // ----------------------------------------------------------------
        // 5. Bind GUI Listeners
        // ----------------------------------------------------------------

        // Homepage: START Button
        homepage.addStartListener(e -> {
            homepage.setVisible(false);
            gamePanel.setVisible(true);
            startGameController.execute(); // Shuffle, ready to start
        });

        // Homepage: EXIT Button
        homepage.addExitListener(e -> System.exit(0)); // Homepage Exit still closes application

        // GamePanel: BET (Deal) Button
        gamePanel.addBetListener(e -> {
            try {
                double amount = Double.parseDouble(gamePanel.getBetAmount());
                placeBetController.execute(amount);
            } catch (NumberFormatException ex) {
                gamePanel.updateMessage("Invalid bet amount!");
            } catch (Exception ex) {
                gamePanel.updateMessage("Error: " + ex.getMessage());
            }
        });

        // GamePanel: Action Buttons
        gamePanel.addHitListener(e -> playerActionController.execute(PLAYER_ID, "HIT", 0, 0));
        gamePanel.addStandListener(e -> playerActionController.execute(PLAYER_ID, "STAND", 0, 0));
        gamePanel.addDoubleListener(e -> playerActionController.execute(PLAYER_ID, "DOUBLE", 0, 0));
        gamePanel.addSplitListener(e -> playerActionController.execute(PLAYER_ID, "SPLIT", 0, 0));
        
        // Bind Exit and Restart buttons
        gamePanel.addExitListener(e -> exitController.handleExitGame());       // Returns to Homepage
        gamePanel.addRestartListener(e -> exitController.handleRestartGame()); // Resets Balance

        // ----------------------------------------------------------------
        // 6. Launch Application
        // ----------------------------------------------------------------
        SwingUtilities.invokeLater(() -> {
            homepage.setVisible(true);
        });
    }
}