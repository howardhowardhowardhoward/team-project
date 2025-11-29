package frameworks_and_drivers.startgame;

import entities.Deck;
import entities.Game;
import usecase.startgame.StartGameDataAccessInterface;

public class StartGameDataAccess implements StartGameDataAccessInterface {
    private final Deck deck;
    private final Game game;

    public StartGameDataAccess(Deck deck, Game game) {
        this.deck = deck;
        this.game = game;
    }

    @Override
    public Deck getDeck() {
        return deck;
    }

    @Override
    public Game getGame() {
        return game;
    }
}
