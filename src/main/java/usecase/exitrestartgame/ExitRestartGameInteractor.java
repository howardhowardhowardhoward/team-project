package usecase.exitrestartgame;

import entities.Deck;
import entities.Player;
import entities.Dealer;

/**
 * Interactor for Exit or Restart Game use case
 * Implements Use Case 5: Exit or Restart Game
 */
public class ExitRestartGameInteractor implements ExitRestartGameInputBoundary {
    
    private final ExitRestartGameOutputBoundary presenter;
    private final ExitRestartGameDataAccess dataAccess;
    
    public ExitRestartGameInteractor(ExitRestartGameOutputBoundary presenter,
                                     ExitRestartGameDataAccess dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }
    
    @Override
    public void executeExit(ExitRestartGameInputData inputData) {

    }
    
    @Override
    public void executeRestart(ExitRestartGameInputData inputData) {
        Player player = dataAccess.getPlayer();
        player.setBalance(1000);
        player.setCurrentBet(0);

        ExitRestartGameOutputData outputData = new ExitRestartGameOutputData("Restart successful",
                1000, 0);
        presenter.presentRestartResult(outputData);
    }

    public Deck getDeck() {
        return dataAccess.getDeck();
    }

    public Player getPlayer() {
        return dataAccess.getPlayer();
    }

    public Dealer getDealer() {
        return dataAccess.getDealer();
    }
}
