package interface_adapter.PlaceBetAndDeal;

import usecase.PlaceBetAndDeal.PlaceBetAndDealOutputBoundary;
import usecase.PlaceBetAndDeal.PlaceBetAndDealOutputData;

/**
 * Presenter for the Place Bet use case.
 */
public class PlaceBetAndDealPresenter implements PlaceBetAndDealOutputBoundary {
    private final PlaceBetAndDealViewModel viewModel;

    public PlaceBetAndDealPresenter(PlaceBetAndDealViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(PlaceBetAndDealOutputData outputData) {
        viewModel.setPlayerCards(outputData.getPlayerCards());
        viewModel.setDealerCards(outputData.getDealerCards());
        viewModel.setPlayerTotal(outputData.getPlayerTotal());
        viewModel.setDealerVisibleTotal(outputData.getDealerVisibleTotal());
        viewModel.setBalance(outputData.getBalance());
        viewModel.setBetAmount(outputData.getBetAmount());
        viewModel.setInitialBlackjack(outputData.isBlackjack());

        viewModel.fireStateChanged();
    }
}