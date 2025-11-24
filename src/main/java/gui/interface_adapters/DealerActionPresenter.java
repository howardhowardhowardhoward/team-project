package gui.interface_adapters;

import gui.GameView;
import usecase.DealerAction.DealerActionOutputBoundary;
import usecase.DealerAction.DealerActionResponseModel;

public class DealerActionPresenter implements DealerActionOutputBoundary {

    private final GameView view;

    public DealerActionPresenter(GameView view) {
        this.view = view;
    }

    @Override
    public void present(DealerActionResponseModel response) {
        view.updateDealerCards(response.getDealerHand().getCards());
        view.showDealerOutcome(response.getDealerScore(), response.isBust(), response.isBlackjack());
    }
}