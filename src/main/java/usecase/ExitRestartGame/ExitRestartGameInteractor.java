package usecase.ExitRestartGame;

import entities.Player;

/**
 * Interactor for Exit or Restart Game use case
 */
public class ExitRestartGameInteractor implements ExitRestartGameInputBoundary {
    
    private final ExitRestartGameOutputBoundary outputBoundary;
    private final ExitRestartGameDataAccess dataAccess;
    private final GameStateManager gameStateManager;
    
    public ExitRestartGameInteractor(ExitRestartGameOutputBoundary outputBoundary,
                                   ExitRestartGameDataAccess dataAccess,
                                   GameStateManager gameStateManager) {
        this.outputBoundary = outputBoundary;
        this.dataAccess = dataAccess;
        this.gameStateManager = gameStateManager;
    }
    
    @Override
    public void executeExit() {
        try {
            // Main Flow: Exit the game
            gameStateManager.setGameExited(true);
            
            // Create success output
            ExitRestartGameOutputData outputData = new ExitRestartGameOutputData(
                true,
                "Game exited successfully. Thank you for playing!",
                0.0,
                true,
                false
            );
            
            outputBoundary.presentExitResult(outputData);
            
        } catch (Exception e) {
            outputBoundary.presentError("Failed to exit game: " + e.getMessage());
        }
    }
    
    @Override
    public void executeRestart() {
        try {
            String playerId = dataAccess.getCurrentPlayerId();
            Player player = dataAccess.getPlayer(playerId);
            
            if (player == null) {
                outputBoundary.presentError("Player not found");
                return;
            }
            
            // Alternate Flow: Reset balance to $1000
            double currentBalance = player.getBalance();
            double adjustment = 1000.0 - currentBalance;
            player.adjustBalance(adjustment);
            
            // Save the updated player
            dataAccess.savePlayer(player);
            
            // Reset game state
            gameStateManager.resetGameState();
            
            // Create success output
            ExitRestartGameOutputData outputData = new ExitRestartGameOutputData(
                true,
                "Game restarted successfully! Balance reset to $1000.",
                1000.0,
                false,
                true
            );
            
            outputBoundary.presentRestartResult(outputData);
            
        } catch (Exception e) {
            outputBoundary.presentError("Failed to restart game: " + e.getMessage());
        }
    }
}