package entities;
import java.util.List;

public interface DeckProvider {
    void shuffleDeck();
    Card drawCard();
    List<Card> drawCards(int count);
}
