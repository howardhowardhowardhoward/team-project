package usecase.DealerAction;
import entities.*;
import usecase.DeckProvider;

public class DealerActionInteractor {
    private Game game;
    private Dealer dealer;

    public DealerActionInteractor(Game game){
        this.game = game;
        this.dealer = this.game.getDealer();
    }

    public void play(DeckProvider deck) {
        while (this.dealer.getHand().getTotalPoints() < 17) {
            Card newcard = deck.drawCard();
            this.dealer.draw(newcard);
        }
        Dealer dealer = this.dealer;
        int dealerScore = dealer.GetDealerScore();
        boolean
    }

    public void execute(DealerActionInputBoundary dealerActionInputBoundary){

        DealerActionOutputBoundary.present();
    }

}
