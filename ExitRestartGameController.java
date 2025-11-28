package interface_adapters;

import usecase.ExitRestartGame.ExitRestartGameInputBoundary;

/**
 * Controller for Exit or Restart Game use case
 */
public class ExitRestartGameController {
    
    private final ExitRestartGameInputBoundary interactor;
    
    public ExitRestartGameController(ExitRestartGameInputBoundary interactor) {
        this.interactor = interactor;
    }
    
    /**
     * Handle exit game request
     * Main Flow: User selects exit button
     */
    public void handleExitGame() {
        interactor.executeExit();
    }
    
    /**
     * Handle restart game request  
     * Alternate Flow: User selects restart button
     */
    public void handleRestartGame() {
        interactor.executeRestart();
    }
}