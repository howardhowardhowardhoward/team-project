package usecase.LoadGame;

import entities.Deck;
import entities.Game;
import entities.Player;
import usecase.StartGame.StartGameDataAccessInterface;

public class LoadGameInteractor implements LoadGameInputBoundary {
    private final LoadGameDataAccessInterface dataAccess;
    private final StartGameDataAccessInterface startGameDataAccess;
    private final LoadGameOutputBoundary presenter;

    public LoadGameInteractor(LoadGameDataAccessInterface dataAccess,
                              StartGameDataAccessInterface startGameDataAccess,
                              LoadGameOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.startGameDataAccess = startGameDataAccess;
        this.presenter = presenter;
    }

    @Override
    public Game loadGame(LoadGameInputData inputData) {
        try {
            // Load saved player balance from JSON file
            double loadedBalance = dataAccess.loadBalance();
            Player player = startGameDataAccess.getGame().getPlayer();
            player.setBalance(loadedBalance);

            Deck deck = startGameDataAccess.getDeck();
            Game game = new Game(deck, loadedBalance);
            // Reset game state
            game.reset();

            // Shuffle deck
            deck.shuffleDeck();

            LoadGameOutputData outputData = new LoadGameOutputData(true, loadedBalance,
                    "Saved game loaded successfully");

            presenter.present(outputData);
            return game;
        } catch (Exception e) {
            LoadGameOutputData outputData = new LoadGameOutputData(false, 0,
                    "Failed to load saved game");

            presenter.present(outputData);
        }
        return null;
    }
}
