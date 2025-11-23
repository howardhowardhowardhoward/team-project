package usecase.PlaceBetAndDeal;

import entities.*;
import frameworks_and_drivers.DeckApiService;
import frameworks_and_drivers.Deck; 
import frameworks_and_drivers.DeckProvider; // **REQUIRED** Interface for the Interactor

import java.util.Scanner;

/**
 * MISPLACED TEST FILE - Should be in src/test/java, not src/main/java
 * Console simulation test for the PlaceBetAndDeal use case.
 */
public class PlaceBetAndDealTest {

    // --- 1. Dummy Presenter Implementation for Testing ---
    /**
     * A placeholder Presenter that satisfies the Interactor's dependency
     * and prints the output data to the console instead of updating a UI.
     */
    private static class DummyPlaceBetPresenter implements PlaceBetAndDealOutputBoundary {
        @Override
        public void present(PlaceBetAndDealOutputData outputData) {
            System.out.println("\n--- Presenter Output ---");
            System.out.println("New Hand Dealt! Status:");
            System.out.println("Player Cards: " + outputData.getPlayerCards());
            System.out.println("Dealer Visible Card: " + outputData.getDealerCards().get(0));
            System.out.println("Player Total: " + outputData.getPlayerTotal());
            // Note: outputData.getBalance() contains the balance AFTER deduction.
            System.out.printf("New Balance: %.2f (Bet Deducted)\n", outputData.getBalance());
            System.out.println("------------------------\n");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DeckApiService deckApiService = new DeckApiService();
        
        // Declare variable as interface DeckProvider, instantiate as concrete Deck.
        DeckProvider deckProvider = new Deck(deckApiService); 
        
        Player player = new Player(1000);
        
        // Dealer uses parameterless constructor
        Dealer dealer = new Dealer(); 
        
        // Instantiate the Dummy Presenter
        PlaceBetAndDealOutputBoundary presenter = new DummyPlaceBetPresenter();

        // Constructor parameter types match strictly: (DeckProvider, Player, Dealer, PlaceBetAndDealOutputBoundary)
        PlaceBetAndDealInteractor interactor = new PlaceBetAndDealInteractor(deckProvider, player, dealer, presenter);
        PlaceBetAndDealController controller = new PlaceBetAndDealController(interactor);

        System.out.println("Initial Player balance: " + player.getBalance());
        System.out.println("Place bet: ");
        double amount = sc.nextDouble();

        // Call correct execute method
        controller.execute(amount); 
        
        // Print final status 
        System.out.println("Final Player balance (after bet deduction): " + player.getBalance());

    }
}