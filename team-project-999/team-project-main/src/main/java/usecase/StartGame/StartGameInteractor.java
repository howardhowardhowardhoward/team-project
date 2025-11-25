package usecase.StartGame;

import entities.*;
import usecase.DeckProvider; // Correct Import

import java.util.List;

public class StartGameInteractor implements StartGameInputBoundary {

    private final StartGameOutputBoundary presenter;
    private final DeckProvider gameDeck;
    private final Game game;

    public StartGameInteractor(StartGameOutputBoundary presenter,
                               DeckProvider deckProvider,
                               Game game) {
        this.presenter = presenter;
        this.gameDeck = deckProvider;
        this.game = game;
    }

    @Override
    public void execute(StartGameInputData inputData) {
        try {
            // 1. Reset game state
            game.reset();

            // 2. Shuffle deck
            gameDeck.shuffle();

            // 3. Deal initial dummy cards just for visual setup (logic handled in PlaceBet)
            // Note: We simulate a deal here to populate the initial view if needed, 
            // but usually this step waits for a bet.
            // For this specific architecture, we just reset and notify.
            
            // Pass empty lists/zeros to indicate a fresh table
            StartGameOutputData outputData = new StartGameOutputData(
                    java.util.Collections.emptyList(),
                    java.util.Collections.emptyList(),
                    0,
                    0,
                    false
            );

            presenter.present(outputData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}