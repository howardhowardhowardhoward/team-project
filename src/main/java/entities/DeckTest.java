package entities;

import frameworks_and_drivers.DeckApiService;

// calling API
public class DeckTest {
    public static void main(String[] args) {
        DeckApiService deckApiService = new DeckApiService();
        Deck deck = new Deck(deckApiService);

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
