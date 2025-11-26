package usecase;

import entities.Card;
import java.util.List;

public interface DeckProvider {
    void shuffle();
    Card drawCard();
    List<Card> drawCards(int count);
}