package usecase.PlaceBetAndDeal;

import entities.*;
import frameworks_and_drivers.DeckApiService;
import usecase.DeckProvider;

import java.util.Scanner;

public class PlaceBetAndDealTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        DeckProvider deck = new DeckApiService();

        Player player = new Player(1000);
        Dealer dealer = new Dealer();

        PlaceBetAndDealOutputBoundary presenter =
                new PlaceBetAndDealPresenter(new PlaceBetAndDealViewModel());

        PlaceBetAndDealInteractor interactor =
                new PlaceBetAndDealInteractor(deck, player, dealer, presenter);

        PlaceBetAndDealController controller =
                new PlaceBetAndDealController(interactor);

        System.out.println("Place bet: ");
        double amount = sc.nextDouble();

        controller.execute(amount);

        // You may still print these for debugging:
        System.out.println("Player hand: " + player.getHand().getCards());
        System.out.println("Dealer showing: " + dealer.getHand().getCards().get(0));
        System.out.println("Player balance: " + player.getBalance());
    }
}
