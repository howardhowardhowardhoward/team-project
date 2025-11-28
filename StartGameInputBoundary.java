package usecase.StartGame;
import entities.*;

public interface StartGameInputBoundary {
    void execute(StartGameInputData inputData);
    Deck getDeck();
    Player getPlayer();
    Dealer getDealer();
}
