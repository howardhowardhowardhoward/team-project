package interface_adapters.exitrestartgame;

import entities.Dealer;
import entities.Deck;
import entities.Player;
import usecase.exitrestartgame.ExitRestartGameInputBoundary;
import usecase.exitrestartgame.ExitRestartGameInputData;

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
    public void handleExitGame(ExitRestartGameInputData inputData) {
        interactor.executeExit(inputData);
    }
    
    /**
     * Handle restart game request  
     * Alternate Flow: User selects restart button
     */
    public void handleRestartGame(ExitRestartGameInputData inputData) {
        interactor.executeRestart(inputData);
    }

    public Deck getDeck() {
        return interactor.getDeck();
    }

    public Player getPlayer() {
        return interactor.getPlayer();
    }

    public Dealer getDealer() {
        return interactor.getDealer();
    }
}
