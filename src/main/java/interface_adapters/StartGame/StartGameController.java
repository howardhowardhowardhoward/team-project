package interface_adapters.StartGame;

import entities.*;
import usecase.StartGame.StartGameInputBoundary;
import usecase.StartGame.StartGameInputData;

public class StartGameController {
    private final StartGameInputBoundary interactor;

    public StartGameController(StartGameInputBoundary interactor) {
        this.interactor = interactor; // upcasting
    }

    public void startGame() {
        StartGameInputData inputData = new StartGameInputData();
        interactor.execute(inputData);
    }
    public Deck getDeck() {
        return interactor.getDeck();
    }

    public Player getPlayer() {
        return interactor.getPlayer();
    }

    public Dealer getDealer() {
        return interactor.getDealer();
    }
}
