package frameworks_and_drivers;

import java.util.*;
import entities.Card;

// Critical Fix: Add implements DeckProvider
public class Deck implements DeckProvider {
    private Stack<Card> cards = new Stack<>();
    private DeckProvider deckProvider;

    public Deck(DeckProvider deckProvider) {
        this.deckProvider = deckProvider;
    }

    @Override
    public void shuffleDeck() {
        deckProvider.shuffleDeck();
    }

    @Override
    public Card drawCard() {
        return deckProvider.drawCard();
    }

    @Override
    public List<Card> drawCards(int count) {
        return deckProvider.drawCards(count);
    }
    
    // Compatible shuffle method for legacy code
    public void shuffle() {
        shuffleDeck();
    }
}