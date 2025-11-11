package entities;
import java.util.*;

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

    /** Draw multiple cards */
    public List<Card> drawCards(int count) {
        return deckProvider.drawCards(count);
    }
}
