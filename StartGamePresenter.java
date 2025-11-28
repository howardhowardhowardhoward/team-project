package interface_adapters.StartGame;

import usecase.StartGame.StartGameOutputBoundary;
import usecase.StartGame.StartGameOutputData;

public class StartGamePresenter implements StartGameOutputBoundary {
    private final StartGameViewModel viewModel;

    public StartGamePresenter(StartGameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(StartGameOutputData outputData) {
        viewModel.setGameReady(outputData.isGameReady());
    }
}
