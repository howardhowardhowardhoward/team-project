package frameworks_and_drivers;

import java.util.*;
import entities.Card;
import usecase.DeckProvider; // IMPORT FIX

public class Deck implements DeckProvider {
    private Stack<Card> cards = new Stack<>();
    private DeckProvider deckProvider;

    public Deck(DeckProvider deckProvider) {
        this.deckProvider = deckProvider;
    }

    @Override
    public void shuffle() {
        deckProvider.shuffle();
    }

    @Override
    public Card drawCard() {
        return deckProvider.drawCard();
    }

    @Override
    public List<Card> drawCards(int count) {
        return deckProvider.drawCards(count);
    }
}