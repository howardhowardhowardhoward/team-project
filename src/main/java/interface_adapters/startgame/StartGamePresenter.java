package interface_adapters.startgame;

import usecase.startgame.StartGameOutputBoundary;
import usecase.startgame.StartGameOutputData;

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
