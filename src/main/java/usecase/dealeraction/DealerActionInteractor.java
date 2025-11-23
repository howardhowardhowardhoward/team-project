package usecase.dealeraction;
import entities.*;

public class DealerActionInteractor {
    private Game game;
    private Dealer dealer;

    public DealerActionInteractor(Game game){
        this.game = game;
        this.dealer = game.getDealer();
    }

    public void play(Deck deck) {
        // FIXED: Use hand.getTotalPoints() instead of maintaining separate score field
        while (this.hand.getTotalPoints() < 17) {
            Card newcard = this.deck.drawCard();
            this.draw(newcard);
        }
    }

    public void execute(DealerActionInputBoundary dealerActionInputBoundary){
        dealer.play();
        DealerActionOutputBoundary.present();
    }

}
