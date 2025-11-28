package interface_adapters;

import usecase.LoadGame.LoadGameOutputBoundary;
import usecase.LoadGame.LoadGameOutputData;

public class LoadGamePresenter implements LoadGameOutputBoundary {
    private final LoadGameViewModel viewModel;

    public LoadGamePresenter(LoadGameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(LoadGameOutputData outputData) {
        viewModel.setSuccess(outputData.isSuccess());
        viewModel.setBalance(outputData.getBalance());
        viewModel.setMessage(outputData.getMessage());
    }
}

