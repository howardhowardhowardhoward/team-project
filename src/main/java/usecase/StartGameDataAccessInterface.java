package usecase.StartGame;

import entities.Deck;
import entities.Game;

public interface StartGameDataAccessInterface {
    Deck getDeck();
    Game getGame();
}
