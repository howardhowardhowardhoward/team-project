package frameworks_and_drivers;
import usecase.DeckProvider;

public class DeckTest {
    public static void main(String[] args) {
        DeckApiService deckApiService = new DeckApiService();
        DeckProvider deck = DeckProvider();

        for (int i = 0; i < 52; i++) {
            System.out.println(deck.drawCard());
        }
        deck.shuffle();
        System.out.println("Cards shuffled, order should be different");
        System.out.println();
        for (int i = 0; i < 52; i++) {
            System.out.println(deck.drawCard());
        }
    }
}
