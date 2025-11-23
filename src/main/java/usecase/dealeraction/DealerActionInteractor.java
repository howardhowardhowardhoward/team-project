package usecase.dealeraction;
import entities.*;

/**
 * INCOMPLETE IMPLEMENTATION - DealerAction use case not implemented
 * TODO: Implement dealer's automatic play logic
 *
 * Expected functionality:
 * - Dealer draws cards until reaching 17 or higher
 * - Handle bust scenarios
 * - Determine winner and update balance
 *
 * Note: Some dealer logic exists in Dealer.play() method
 * This interactor should coordinate that with game state
 */
public class DealerActionInteractor {
    private Game game;
    private Dealer dealer;

    public DealerActionInteractor(Game game){
        this.game = game;
        this.dealer = game.getDealer();
    }

    public void execute(DealerActionInputBoundary dealerActionInputBoundary){
        dealer.play();
        DealerActionOutputBoundary.present();
    }

}
