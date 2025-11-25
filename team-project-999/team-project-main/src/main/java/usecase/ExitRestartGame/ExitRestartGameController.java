package usecase.ExitRestartGame;

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
     */
    public void handleExitGame() {
        interactor.executeExit();
    }
    
    /**
     * Handle restart game request  
     */
    public void handleRestartGame() {
        interactor.executeRestart();
    }
}