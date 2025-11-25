package usecase.dealeraction;
import entities.*;
import usecase.PlayerActions.BalanceUpdater;
import usecase.PlayerActions.GameDataAccess;
import java.util.List;

/**
 * Interactor for DealerAction use case.
 * Implements the dealer's automatic play logic and acts as the DealerController for PlayerAction.
 */
public class DealerActionInteractor implements DealerActionInputBoundary, DealerController {
    
    private final GameDataAccess gameDataAccess;
    private final DealerActionOutputBoundary outputBoundary;
    private final BalanceUpdater balanceUpdater; // Not strictly used here, but for completeness
    
    public DealerActionInteractor(GameDataAccess gameDataAccess, 
                                  DealerActionOutputBoundary outputBoundary,
                                  BalanceUpdater balanceUpdater){
        this.gameDataAccess = gameDataAccess;
        this.outputBoundary = outputBoundary;
        this.balanceUpdater = balanceUpdater;
    }
    
    @Override
    public void execute(DealerActionInputData inputData) {
        // Dealer's turn is triggered by the PlayerActionInteractor, which then handles the full settlement.
        // This execution only handles the drawing of cards.
        executeDealerTurn();
        
        // NOTE: A more complex implementation would calculate the final result and call outputBoundary.present() here.
        // For a seamless flow with PlayerActionInteractor, we rely on the PlayerActionInteractor for final presentation.
    }

    /**
     * Executes the dealer's turn (implements DealerController method)
     */
    @Override
    public boolean executeDealerTurn() {
        Dealer dealer = gameDataAccess.getDealer();
        
        // Dealer plays until score is 17 or more
        while (dealer.GetDealerScore() < 17) {
            Card newCard = gameDataAccess.drawCard();
            dealer.draw(newCard);
        }
        
        return true;
    }

    @Override
    public int getDealerFinalScore() {
        return gameDataAccess.getDealer().GetDealerScore();
    }

    @Override
    public boolean isDealerBust() {
        return gameDataAccess.getDealer().isBust();
    }
}