package interface_adapters.LoadGame;

import entities.Game;
import usecase.LoadGame.LoadGameInputBoundary;
import usecase.LoadGame.LoadGameInputData;

public class LoadGameController {
    private final LoadGameInputBoundary interactor;

    public LoadGameController(LoadGameInputBoundary interactor) {
        this.interactor = interactor;
    }

    public Game loadGame() {
        LoadGameInputData inputData =  new LoadGameInputData();
        return interactor.loadGame(inputData);
    }
}
