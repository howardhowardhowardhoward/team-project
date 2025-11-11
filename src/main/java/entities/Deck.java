package entities;

import java.io.IOException;
import java.util.*;
import frameworks_and_drivers.DeckApiService;

public class Deck {
    private Stack<Card> cards = new Stack<>();
    private DeckProvider deckProvider;

    public Deck(DeckProvider deckProvider) {
        this.deckProvider = deckProvider;
    }

    public void shuffle() {
        deckProvider.shuffleDeck();
    }

    /** Draw a single card */
    public Card drawCard() {
        return deckProvider.drawCard();
    }
}
