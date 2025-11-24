package frameworks_and_drivers;
import java.util.List;
import entities.*;

public interface DeckProvider {
    void shuffleDeck();
    Card drawCard();
    List<Card> drawCards(int count);
}
