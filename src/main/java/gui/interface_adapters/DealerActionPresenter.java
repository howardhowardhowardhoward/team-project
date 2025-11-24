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
        view.updateDealerCards(response.getDealerCards());
        view.showDealerOutcome(response.getDealerTotal(), response.isBust(), response.isBlackjack());
    }
}