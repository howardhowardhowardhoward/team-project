package usecase.StartGame;

import entities.*;
import frameworks_and_drivers.DeckApiService;

import java.io.IOException;
import java.util.List;

public class StartGameInteractor implements StartGameInputBoundary {

    private final StartGameOutputBoundary presenter;
    private final Deck gameDeck;
    private final Game game;

    public StartGameInteractor(StartGameOutputBoundary presenter,
                               DeckApiService deckService,
                               Game game) {

        DeckApiService deckApiService = new DeckApiService();
        this.presenter = presenter;
        this.gameDeck = new Deck(deckApiService);
        this.game = game;
    }

    @Override
    public void execute(StartGameInputData inputData) {
        try {
            // 1. Reset game state
            game.reset();

            // 2. Shuffle or request a new deck
            gameDeck.shuffle();

            // 3. Deal initial cards (2 player, 2 dealer)
            List<Card> playerCards = gameDeck.drawCards(2);
            List<Card> dealerCards = gameDeck.drawCards(2);

            Hand playerhand = new Hand();
            Hand dealerhand = new Hand();

            for (int i = 0; i <= 1; i++){
                playerhand.addCard(playerCards.get(i));
                dealerhand.addCard(dealerCards.get(i));
            }

            int playerTotal = playerhand.getTotalPoints();
            int dealerVisibleTotal = dealerhand.getCards().get(0).getValue();

            boolean playerBlackjack = (playerTotal == 21);

            // 4. Prepare output
            StartGameOutputData outputData = new StartGameOutputData(
                    playerCards,
                    dealerCards,
                    playerTotal,
                    dealerVisibleTotal,
                    playerBlackjack
            );

            presenter.present(outputData);

        } catch (Exception e) {
            // Optional: send error output to presenter
            e.printStackTrace();
        }
    }
}
