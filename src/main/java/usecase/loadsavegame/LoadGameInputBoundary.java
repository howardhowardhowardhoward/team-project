package usecase.loadsavegame;

import entities.Game;

public interface LoadGameInputBoundary {
    Game loadGame(LoadGameInputData inputData);
}
