package frameworks_and_drivers;

// If you deleted the 'Deck' class as per clean up suggestions, 
// we test DeckApiService directly.
public class DeckTest {
    public static void main(String[] args) {
        DeckApiService deckApiService = new DeckApiService();
        
        System.out.println("Testing DeckApiService directly:");
        for (int i = 0; i < 5; i++) {
            System.out.println(deckApiService.drawCard());
        }
        
        deckApiService.shuffle();
        System.out.println("Deck shuffled.");
    }
}