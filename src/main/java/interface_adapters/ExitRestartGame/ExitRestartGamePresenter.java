package interface_adapters.ExitRestartGame;

import frameworks_and_drivers.Homepage;
import interface_adapters.PlaceBetAndDeal.PlaceBetAndDealViewModel;
import usecase.ExitRestartGame.ExitRestartGameOutputBoundary;
import usecase.ExitRestartGame.ExitRestartGameOutputData;

public class ExitRestartGamePresenter implements ExitRestartGameOutputBoundary {
    private final PlaceBetAndDealViewModel viewModel;

    public ExitRestartGamePresenter(PlaceBetAndDealViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentExitResult(ExitRestartGameOutputData outputData) {
    }

    @Override
    public void presentRestartResult(ExitRestartGameOutputData outputData) {
        double balance = outputData.getNewBalance();
        double betAmount = outputData.getNewBetAmount();
        viewModel.setBalance(balance);
        viewModel.setBetAmount(betAmount);
    }

    @Override
    public void presentError(String errorMessage) {
    }
}
