package usecase.StartGame;

import entities.Card;
import entities.Game;
import frameworks_and_drivers.DeckApiService;

import java.io.IOException;
import java.util.List;

public class StartGameInteractor implements StartGameInputBoundary {

    private final StartGameOutputBoundary presenter;
    private final DeckApiService deckService;
    private final Game game;

    public StartGameInteractor(StartGameOutputBoundary presenter,
                               DeckApiService deckService,
                               Game game) {
        this.presenter = presenter;
        this.deckService = deckService;
        this.game = game;
    }

    @Override
    public void execute(StartGameInputData inputData) {
        try {
            // 1. Reset game state
            game.reset();

            // 2. Shuffle or request a new deck
            deckService.shuffleIfNeeded(game);

            // 3. Deal initial cards (2 player, 2 dealer)
            List<Card> playerCards = deckService.drawCards(2);
            List<Card> dealerCards = deckService.drawCards(2);

            game.getPlayer().setHandCards(playerCards);
            game.getDealer().setHandCards(dealerCards);

            int playerTotal = game.getPlayer().getHand().getTotalPoints();
            int dealerVisibleTotal = game.getDealer().getHand().getCard(0).getValue();

            boolean blackjack = game.checkInitialBlackjack();

            // 4. Prepare output
            StartGameOutputData outputData = new StartGameOutputData(
                    playerCards,
                    dealerCards,
                    playerTotal,
                    dealerVisibleTotal,
                    blackjack
            );

            presenter.present(outputData);

        } catch (IOException e) {
            // Optional: send error output to presenter
            e.printStackTrace();
        }
    }
}
