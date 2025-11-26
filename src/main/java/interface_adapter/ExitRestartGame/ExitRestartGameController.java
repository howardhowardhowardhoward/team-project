package interface_adapter.ExitRestartGame;

import usecase.ExitRestartGame.ExitRestartGameInputBoundary;

/**
 * Controller for Exit or Restart Game use case.
 */
public class ExitRestartGameController {
    
    private final ExitRestartGameInputBoundary interactor;
    
    public ExitRestartGameController(ExitRestartGameInputBoundary interactor) {
        this.interactor = interactor;
    }
    
    public void handleExitGame() {
        interactor.executeExit();
    }
    
    public void handleRestartGame() {
        interactor.executeRestart();
    }
}