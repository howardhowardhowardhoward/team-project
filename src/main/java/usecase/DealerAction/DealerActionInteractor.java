package usecase.DealerAction;
import entities.*;
import usecase.DeckProvider;

public class DealerActionInteractor {
    private Game game;
    private Dealer dealer;
    private final DeckProvider deckProvider;
    private final DealerActionOutputBoundary presenter;

    public DealerActionInteractor(Game game, DeckProvider deckProvider, DealerActionOutputBoundary presenter){
        this.game = game;
        this.dealer = game.getDealer();
        this.deckProvider = deckProvider;
        this.presenter = presenter;
    }

    public void play(DeckProvider deck) {
        while (this.dealer.getHand().getTotalPoints() < 17) {
            Card newcard = deck.drawCard();
            this.dealer.draw(newcard);
        }
        Dealer dealer = this.dealer;
        int dealerScore = dealer.GetDealerScore();
        boolean dealerBust = (dealerScore > 21);
        boolean dealerBlackjack = dealer.isBlackJack();
    }

    public void execute(DealerActionInputBoundary dealerActionInputBoundary){

        DealerActionOutputBoundary.present();
    }

}
