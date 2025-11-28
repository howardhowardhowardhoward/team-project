package usecase.ExitRestartGame;

import interface_adapters.ExitRestartGame.ExitRestartGameController;

/**
 * Main class for Exit or Restart Game use case
 * Coordinates the use case operations
 */
public class ExitRestartGame {
    
    private final ExitRestartGameController controller;
    private final ExitRestartGameOutputBoundary outputBoundary;
    
    public ExitRestartGame(ExitRestartGameController controller, 
                          ExitRestartGameOutputBoundary outputBoundary) {
        this.controller = controller;
        this.outputBoundary = outputBoundary;
    }
    
    /**
     * Execute exit game operation
     */
    public void exitGame() {
        controller.handleExitGame();
    }
    
    /**
     * Execute restart game operation
     */
    public void restartGame() {
        controller.handleRestartGame();
    }
    
    /**
     * Check if game should exit (for main application loop)
     */
    public boolean shouldExitGame(GameStateManager stateManager) {
        return stateManager != null && stateManager.isGameExited();
    }
}
