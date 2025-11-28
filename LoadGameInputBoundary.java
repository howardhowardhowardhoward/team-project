package usecase.LoadGame;

import entities.Game;

public interface LoadGameInputBoundary {
    Game loadGame(LoadGameInputData inputData);
}
