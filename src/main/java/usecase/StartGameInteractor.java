package usecase.StartGame;

import entities.*;

public class StartGameInteractor implements StartGameInputBoundary {

    private final StartGameOutputBoundary presenter;
    private final StartGameDataAccessInterface dataAccess;

    public StartGameInteractor(StartGameOutputBoundary presenter,
                               StartGameDataAccessInterface dataAccess) {

        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(StartGameInputData inputData) {
        try {
            Deck deck = dataAccess.getDeck();
            Game game =  dataAccess.getGame();
            // Reset game state
            game.reset();

            // Shuffle deck
            deck.shuffleDeck();

            StartGameOutputData outputData = new StartGameOutputData(true);

            presenter.present(outputData);

        } catch (Exception e) {
            // Optional: send error output to presenter
            e.printStackTrace();
        }
    }

    @Override
    public Deck getDeck() {
        return dataAccess.getDeck();
    }

    @Override
    public Player getPlayer() {
        return dataAccess.getGame().getPlayer();
    }

    @Override
    public Dealer getDealer() {
        return dataAccess.getGame().getDealer();
    }
}
