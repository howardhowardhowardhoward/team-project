package usecase.DealerAction;
import entities.*;
import usecase.DeckProvider;

public class DealerActionInteractor implements DealerActionInputBoundary{
    private Dealer dealer;
    private final DeckProvider deckProvider;
    private final DealerActionOutputBoundary presenter;

    public DealerActionInteractor(Dealer dealer, DeckProvider deckProvider, DealerActionOutputBoundary presenter){
        this.dealer = dealer;
        this.deckProvider = deckProvider;
        this.presenter = presenter;
    }

    public void execute(DealerActionRequestModel dealerActionRequestModel) {
        while (this.dealer.getHand().getTotalPoints() < 17) {
            Card newcard = deckProvider.drawCard();
            this.dealer.draw(newcard);
        }
        Dealer dealer = this.dealer;
        int dealerScore = dealer.GetDealerScore();
        boolean dealerBust = (dealerScore > 21);
        boolean dealerBlackjack = dealer.isBlackJack();

        DealerActionResponseModel responseModel = new DealerActionResponseModel(dealer.getHand(),dealerScore,
                dealerBust, dealerBlackjack);
        presenter.present(responseModel);
    }

}
