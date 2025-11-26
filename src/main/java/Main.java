import entities.Card;
import entities.Dealer;
import entities.Game;
import entities.Player;
import frameworks_and_drivers.DeckApiService;
import gui.GamePanel;
import gui.Homepage;

import usecase.DeckProvider;
import usecase.ExitRestartGame.*;
import usecase.PlaceBetAndDeal.*;
import usecase.PlayerActions.*;
import usecase.StartGame.*;
import usecase.dealeraction.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Main {
    
    private static final String PLAYER_ID = "mainPlayer"; 

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH); 

        // 1. Create GUI
        Homepage homepage = new Homepage();
        GamePanel gamePanel = new GamePanel();

        // 2. Initialize Core
        DeckProvider deckService = new DeckApiService();
        Game game = new Game(); 
        
        // 3. Create Unified Data Access
        UnifiedDataAccess dataAccess = new UnifiedDataAccess(game, deckService);

        // ----------------------------------------------------------------
        // 4. Setup Presenters
        // ----------------------------------------------------------------
        
        PlaceBetAndDealOutputBoundary placeBetPresenter = outputData -> {
            gamePanel.updatePlayerCards(outputData.getPlayerCards(), outputData.getPlayerTotal());
            gamePanel.updateDealerCards(outputData.getDealerCards(), outputData.getDealerVisibleTotal());
            gamePanel.updateBalance(outputData.getBalance());
            
            if (outputData.isBlackjack()) {
                gamePanel.updateMessage("BLACKJACK! You Win!");
                gamePanel.enableGameButtons(false);
            } else {
                gamePanel.updateMessage("Your Turn: Hit, Stand or Double?");
                gamePanel.enableGameButtons(true);
            }
        };

        PlayerActionOutputBoundary playerActionPresenter = outputData -> {
            gamePanel.updateMessage(outputData.getMessage());
            gamePanel.updatePlayerCards(game.getPlayer().getHand().getCards(), outputData.getHandTotal());
            
            if (outputData.isGameComplete()) {
                gamePanel.updateDealerCards(game.getDealer().getHand().getCards(), outputData.getDealerTotal());
                gamePanel.updateBalance(game.getPlayer().getBalance());
                gamePanel.enableGameButtons(false); 
                gamePanel.updateMessage(outputData.getMessage());
            } else if (outputData.isBust()) {
                gamePanel.updateMessage("BUST! You went over 21.");
            }
        };

        ExitRestartGameOutputBoundary exitPresenter = new ExitRestartGameOutputBoundary() {
            @Override
            public void presentExitResult(ExitRestartGameOutputData outputData) {
                // Logic for "Leave" button -> Go back to Homepage
                int confirm = JOptionPane.showConfirmDialog(gamePanel, "Return to Main Menu?", "Leave Game", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    gamePanel.setVisible(false);
                    homepage.setVisible(true);
                }
            }

            @Override
            public void presentRestartResult(ExitRestartGameOutputData outputData) {
                // Logic for "Restart" button
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

        StartGameOutputBoundary startGamePresenter = new StartGameOutputBoundary() {
            @Override
            public void present(StartGameOutputData outputData) {
                gamePanel.updateMessage("Deck Shuffled. Please place your bet.");
                gamePanel.enableGameButtons(false);
            }

            @Override
            public void presentBetError(String message) {
                JOptionPane.showMessageDialog(gamePanel, message);
            }
        };

        // ----------------------------------------------------------------
        // 5. Setup Interactors & Controllers
        // ----------------------------------------------------------------

        PlayerActionInteractor playerActionInteractor = new PlayerActionInteractor(
            playerActionPresenter, dataAccess
        );
        PlayerActionController playerActionController = new PlayerActionController(playerActionInteractor);

        PlaceBetAndDealInputBoundary placeBetInteractor = new PlaceBetAndDealInteractor(
            deckService, game.getPlayer(), game.getDealer(), placeBetPresenter, dataAccess
        );
        PlaceBetAndDealController placeBetController = new PlaceBetAndDealController(placeBetInteractor);

        StartGameInputBoundary startGameInteractor = new StartGameInteractor(
            startGamePresenter, deckService, game
        );
        StartGameController startGameController = new StartGameController(startGameInteractor);

        ExitRestartGameInputBoundary exitInteractor = new ExitRestartGameInteractor(
            exitPresenter, dataAccess, dataAccess
        );
        ExitRestartGameController exitController = new ExitRestartGameController(exitInteractor);


        // ----------------------------------------------------------------
        // 6. Bind GUI Listeners
        // ----------------------------------------------------------------

        homepage.addStartListener(e -> {
            homepage.setVisible(false);
            gamePanel.setVisible(true);
            try {
                startGameController.execute(); 
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        });
        // Exit button on Homepage closes the app
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
        
        // "Leave" button returns to homepage
        gamePanel.addLeaveListener(e -> exitController.handleExitGame());
        // "Restart" button resets balance
        gamePanel.addRestartListener(e -> exitController.handleRestartGame());

        // ----------------------------------------------------------------
        // 7. Launch
        // ----------------------------------------------------------------
        SwingUtilities.invokeLater(() -> {
            homepage.setVisible(true);
        });
    }

    // --------------------------------------------------------------------
    // Unified Data Access Adapter
    // --------------------------------------------------------------------
    private static class UnifiedDataAccess implements 
            GameDataAccess, 
            ExitRestartGameDataAccess, 
            GameStateManager, 
            BalanceUpdater {

        private final Game game;
        private final DeckProvider deckProvider;
        private final Map<Integer, Double> betAmounts = new HashMap<>();
        private final Map<Integer, Boolean> handCompletion = new HashMap<>();
        private String gameState = "WAITING";
        private boolean gameExited = false;

        public UnifiedDataAccess(Game game, DeckProvider deckProvider) {
            this.game = game;
            this.deckProvider = deckProvider;
        }

        @Override
        public Player getPlayer(String playerId) { return game.getPlayer(); }

        @Override
        public String getCurrentPlayerId() { return PLAYER_ID; }

        @Override
        public Dealer getDealer() { return game.getDealer(); }

        @Override
        public Card drawCard() { return deckProvider.drawCard(); }

        @Override
        public double getBetAmount(int handIndex) {
            return betAmounts.getOrDefault(handIndex, 0.0);
        }

        @Override
        public void setBetAmount(int handIndex, double amount) {
            betAmounts.put(handIndex, amount);
        }

        @Override
        public void markHandComplete(int handIndex) {
            handCompletion.put(handIndex, true);
        }

        @Override
        public boolean isHandComplete(int handIndex) {
            return handCompletion.getOrDefault(handIndex, false);
        }

        @Override
        public boolean allHandsComplete() {
            return isHandComplete(0) && (!game.getPlayer().hasSplit() || isHandComplete(1));
        }

        @Override
        public void addHandBet(int handIndex, double amount) {
            double current = getBetAmount(handIndex);
            betAmounts.put(handIndex, current + amount);
        }

        @Override
        public String getGameState() { return gameState; }

        @Override
        public void setGameState(String state) { 
            this.gameState = state; 
            if (state.equals("DEALING") || state.equals("WAITING_FOR_BET")) {
                handCompletion.clear();
                betAmounts.clear();
            }
        }

        @Override
        public void savePlayer(Player player) { }

        @Override
        public boolean isGameActive() { return !gameExited; }

        @Override
        public void setGameExited(boolean exited) { this.gameExited = exited; }

        @Override
        public boolean isGameExited() { return gameExited; }

        @Override
        public void resetGameState() {
            game.reset();
            betAmounts.clear();
            handCompletion.clear();
            gameState = "WAITING";
        }

        @Override
        public String getGameStatus() { return gameState; }

        @Override
        public boolean deductBalance(String playerId, double amount, String reason) {
            if (game.getPlayer().getBalance() >= amount) {
                game.getPlayer().adjustBalance(-amount);
                return true;
            }
            return false;
        }

        @Override
        public double addBalance(String playerId, double amount, String reason) {
            game.getPlayer().adjustBalance(amount);
            return game.getPlayer().getBalance();
        }

        @Override
        public double getBalance(String playerId) {
            return game.getPlayer().getBalance();
        }
    }
}