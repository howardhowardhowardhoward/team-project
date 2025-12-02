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
        // 100% Effective Code: Handle exit logic explicitly
        // Even if the UI handles the actual window closing, the system state 
        // should formally acknowledge the exit intent.
        
        // Create output data (balance/bet are 0 as we are leaving)
        ExitRestartGameOutputData outputData = new ExitRestartGameOutputData(
                "Game exited successfully", 0, 0
        );
        presenter.presentExitResult(outputData);
        // The controller/view will handle the System.exit(0) based on this callback
    }
    
    @Override
    public void executeRestart(ExitRestartGameInputData inputData) {
        Player player = dataAccess.getPlayer();
        Dealer dealer = dataAccess.getDealer();

        // 100% Effective Code: Complete State Reset
        // 1. Reset Balance
        player.setBalance(1000);
        
        // 2. Clear Bets
        player.setCurrentBet(0);
        
        // 3. Clear Hands (Crucial for a clean restart)
        player.clearHands();
        if (dealer.getHand() != null) {
            dealer.getHand().clear();
        }

        // 4. Output Result
        ExitRestartGameOutputData outputData = new ExitRestartGameOutputData(
                "Restart successful",
                1000, 
                0
        );
        presenter.presentRestartResult(outputData);
    }

    @Override
    public Deck getDeck() {
        return dataAccess.getDeck();
    }

    @Override
    public Player getPlayer() {
        return dataAccess.getPlayer();
    }

    @Override
    public Dealer getDealer() {
        return dataAccess.getDealer();
    }
}
