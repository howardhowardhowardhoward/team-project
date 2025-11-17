package usecase.PlaceBetAndDeal;

import entities.*;
import frameworks_and_drivers.DeckApiService;

import java.util.Scanner;

public class PlaceBetAndDealTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DeckApiService deckApiService = new DeckApiService();
        Deck deck = new Deck(deckApiService);
        Player player = new Player("1", "H. Maguire", 1000);
        Dealer dealer = new Dealer(deck);

        PlaceBetAndDealInteractor interactor = new PlaceBetAndDealInteractor(deck, player, dealer);
        PlaceBetAndDealController controller = new PlaceBetAndDealController(interactor);

        System.out.println("Place bet: ");
        double amount = sc.nextDouble();

        controller.placeBetAndDeal(amount); // place $50 bet and deal
        System.out.println("Player hand: " + player.getHand(0));
        System.out.println("Dealer showing: " + dealer.getHand().getCards().get(0));
        System.out.println(player.getBalance());

    }
}
