package usecase;
import java.util.List;
import entities.*;

public interface DeckProvider {
    void shuffle();
    Card drawCard();
    List<Card> drawCards(int count);
}
