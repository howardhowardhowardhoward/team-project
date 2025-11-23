package usecase.PlaceBetAndDeal;

import entities.*;
import frameworks_and_drivers.DeckApiService;

import java.util.Scanner;

/**
 * MISPLACED TEST FILE - Should be in src/test/java, not src/main/java
 * TODO: Move this file to src/test/java/usecase/PlaceBetAndDeal/
 */
public class PlaceBetAndDealTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DeckApiService deckApiService = new DeckApiService();
        Deck deck = new Deck(deckApiService);
        // FIXED: Player constructor only accepts balance (double), not id, name, balance
        Player player = new Player(1000);
        Dealer dealer = new Dealer(deck);

        PlaceBetAndDealInteractor interactor = new PlaceBetAndDealInteractor(deck, player, dealer);
        PlaceBetAndDealController controller = new PlaceBetAndDealController(interactor);

        System.out.println("Place bet: ");
        double amount = sc.nextDouble();

        controller.placeBetAndDeal(amount); // place bet and deal
        // Use getHand() to access primary hand (index 0)
        System.out.println("Player hand: " + player.getHand());
        System.out.println("Dealer showing: " + dealer.getHand().getCards().get(0));
        System.out.println("Player balance: " + player.getBalance());

    }
}
