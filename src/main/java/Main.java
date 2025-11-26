import entities.*;
import frameworks_and_drivers.DeckApiService;
import frameworks_and_drivers.InMemoryGameDataAccess;
import gui.GamePanel;
import gui.Homepage;

// Interface Adapters (Controllers & Presenters) - Moved to correct package
import interface_adapter.dealeraction.DealerActionController;
import interface_adapter.dealeraction.DealerActionPresenter;
import interface_adapter.update_balance.UpdateBalanceController;
import interface_adapter.update_balance.UpdateBalancePresenter;
import interface_adapter.PlayerActions.*;
import interface_adapter.PlaceBetAndDeal.*;
import interface_adapter.StartGame.*;
import interface_adapter.ExitRestartGame.*;

import usecase.DeckProvider;
import usecase.ExitRestartGame.*;
import usecase.PlaceBetAndDeal.*;
import usecase.PlayerActions.*;
import usecase.StartGame.*;
import usecase.dealeraction.DealerActionInteractor;
import usecase.updatebalance.UpdateBalanceInteractor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Locale;

public class Main {
    
    private static final String PLAYER_ID = "mainPlayer"; 

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH); 

        // 1. Create GUI
        Homepage homepage = new Homepage();
        GamePanel gamePanel = new GamePanel();

        // 2. Initialize Drivers & Core Data
        DeckProvider deckService = new DeckApiService();
        
        // Use the unified Data Access class that implements all required interfaces
        InMemoryGameDataAccess dataAccess = new InMemoryGameDataAccess(deckService);

        // Retrieve shared entities to ensure synchronization across Use Cases
        Player sharedPlayer = dataAccess.getPlayer(PLAYER_ID);
        Dealer sharedDealer = dataAccess.getDealer();

        // Create Game entity with shared objects
        Game game = new Game(sharedPlayer, sharedDealer);

        // ----------------------------------------------------------------
        // 3. Setup Use Cases (Clean Architecture Wiring)
        // ----------------------------------------------------------------

        // --- A. Update Balance Use Case (Retained) ---
        UpdateBalancePresenter updateBalancePresenter = new UpdateBalancePresenter();
        UpdateBalanceInteractor updateBalanceInteractor = new UpdateBalanceInteractor(dataAccess, updateBalancePresenter);
        UpdateBalanceController updateBalanceController = new UpdateBalanceController(updateBalanceInteractor);

        // --- B. Dealer Action Use Case (Retained) ---
        DealerActionPresenter dealerPresenter = new DealerActionPresenter();
        // DealerInteractor acts as a controller/helper for PlayerAction logic
        DealerActionInteractor dealerInteractor = new DealerActionInteractor(dataAccess, dealerPresenter, dataAccess);
        DealerActionController dealerController = new DealerActionController(dealerInteractor);

        // --- C. Place Bet & Deal ---
        PlaceBetAndDealViewModel placeBetViewModel = new PlaceBetAndDealViewModel();
        PlaceBetAndDealOutputBoundary placeBetPresenter = new PlaceBetAndDealPresenter(placeBetViewModel);

        // Connect ViewModel -> View (Observer Pattern)
        placeBetViewModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                gamePanel.updatePlayerCards(placeBetViewModel.getPlayerCards(), placeBetViewModel.getPlayerTotal());
                gamePanel.updateDealerCards(placeBetViewModel.getDealerCards(), placeBetViewModel.getDealerVisibleTotal());
                gamePanel.updateBalance(placeBetViewModel.getBalance());
                
                if (placeBetViewModel.isInitialBlackjack()) {
                    gamePanel.updateMessage("BLACKJACK! You Win!");
                    gamePanel.enableGameButtons(false);
                } else {
                    gamePanel.updateMessage("Your Turn: Hit, Stand or Double?");
                    gamePanel.enableGameButtons(true);
                }
            }
        });

        PlaceBetAndDealInputBoundary placeBetInteractor = new PlaceBetAndDealInteractor(
            deckService, sharedPlayer, sharedDealer, placeBetPresenter, dataAccess
        );
        PlaceBetAndDealController placeBetController = new PlaceBetAndDealController(placeBetInteractor);

        // --- D. Player Actions (Hit/Stand/etc) ---
        PlayerActionViewModel playerActionViewModel = new PlayerActionViewModel();
        PlayerActionOutputBoundary playerActionPresenter = new PlayerActionPresenter(playerActionViewModel);
        
        // View listens to ViewModel
        playerActionViewModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                gamePanel.updateMessage(playerActionViewModel.getMessage());
                // Refresh View with latest data from shared entities
                gamePanel.updatePlayerCards(sharedPlayer.getHand().getCards(), playerActionViewModel.getPlayerTotal());
                
                if (!playerActionViewModel.isGameButtonsEnabled()) {
                    // Game Over state
                    gamePanel.updateDealerCards(sharedDealer.getHand().getCards(), playerActionViewModel.getDealerTotal());
                    gamePanel.updateBalance(sharedPlayer.getBalance());
                    gamePanel.enableGameButtons(false);
                } else {
                    // Game Continues
                }
            }
        });

        // Inject DealerInteractor as the DealerController logic for the PlayerAction use case
        PlayerActionInteractor playerActionInteractor = new PlayerActionInteractor(
            playerActionPresenter, dataAccess, dataAccess, dealerInteractor
        );
        PlayerActionController playerActionController = new PlayerActionController(playerActionInteractor);

        // --- E. Start Game (Reset) ---
        StartGameOutputBoundary startGamePresenter = new StartGameOutputBoundary() {
            @Override
            public void present(StartGameOutputData outputData) {
                gamePanel.updateMessage("Deck Shuffled. Please place your bet.");
                gamePanel.enableGameButtons(false);
                gamePanel.updatePlayerCards(new ArrayList<>(), 0);
                gamePanel.updateDealerCards(new ArrayList<>(), 0);
            }

            @Override
            public void presentBetError(String message) {
                JOptionPane.showMessageDialog(gamePanel, message);
            }
        };

        StartGameInputBoundary startGameInteractor = new StartGameInteractor(
            startGamePresenter, deckService, game
        );
        StartGameController startGameController = new StartGameController(startGameInteractor);

        // --- F. Exit / Restart ---
        ExitRestartGameOutputBoundary exitPresenter = new ExitRestartGameOutputBoundary() {
            @Override
            public void presentExitResult(ExitRestartGameOutputData outputData) {
                int confirm = JOptionPane.showConfirmDialog(gamePanel, "Return to Main Menu?", "Leave Game", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    gamePanel.setVisible(false);
                    homepage.setVisible(true);
                }
            }

            @Override
            public void presentRestartResult(ExitRestartGameOutputData outputData) {
                gamePanel.updateBalance(outputData.getNewBalance());
                gamePanel.updateMessage("Game Restarted! Balance reset to $1000.");
                gamePanel.updatePlayerCards(new ArrayList<>(), 0);
                gamePanel.updateDealerCards(new ArrayList<>(), 0);
                gamePanel.enableGameButtons(false);
            }

            @Override
            public void presentError(String errorMessage) {
                JOptionPane.showMessageDialog(gamePanel, errorMessage);
            }
        };

        ExitRestartGameInputBoundary exitInteractor = new ExitRestartGameInteractor(
            exitPresenter, dataAccess, dataAccess
        );
        ExitRestartGameController exitController = new ExitRestartGameController(exitInteractor);


        // ----------------------------------------------------------------
        // 4. Bind GUI Listeners
        // ----------------------------------------------------------------

        homepage.addStartListener(e -> {
            homepage.setVisible(false);
            gamePanel.setVisible(true);
            startGameController.execute();
        });
        homepage.addExitListener(e -> System.exit(0));

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

        gamePanel.addHitListener(e -> playerActionController.execute(PLAYER_ID, "HIT", 0, 0));
        gamePanel.addStandListener(e -> playerActionController.execute(PLAYER_ID, "STAND", 0, 0));
        gamePanel.addDoubleListener(e -> playerActionController.execute(PLAYER_ID, "DOUBLE", 0, 0));
        gamePanel.addSplitListener(e -> playerActionController.execute(PLAYER_ID, "SPLIT", 0, 0));

        gamePanel.addLeaveListener(e -> exitController.handleExitGame());
        gamePanel.addRestartListener(e -> exitController.handleRestartGame());

        SwingUtilities.invokeLater(() -> {
            homepage.setVisible(true);
        });
    }
}