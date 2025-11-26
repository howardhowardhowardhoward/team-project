package usecase.PlaceBetAndDeal;

import entities.*;
import frameworks_and_drivers.DeckApiService;
import usecase.DeckProvider;
import usecase.PlayerActions.GameDataAccess; 

// FIX: Import the Controller from the interface_adapter layer
import interface_adapter.PlaceBetAndDeal.PlaceBetAndDealController; 

import java.util.Scanner;

/**
 * Console simulation test for the PlaceBetAndDeal use case.
 * LOCATION: src/test/java/usecase/PlaceBetAndDeal/
 */
public class PlaceBetAndDealTest {

    // --- Dummy Presenter Implementation for Testing ---
    private static class DummyPlaceBetPresenter implements PlaceBetAndDealOutputBoundary {
        @Override
        public void present(PlaceBetAndDealOutputData outputData) {
            System.out.println("\n--- Presenter Output ---");
            System.out.println("New Hand Dealt! Status:");
            System.out.println("Player Cards: " + outputData.getPlayerCards());
            System.out.println("Dealer Visible Card: " + outputData.getDealerCards().get(0));
            System.out.println("Player Total: " + outputData.getPlayerTotal());
            System.out.printf("New Balance: %.2f (Bet Deducted)\n", outputData.getBalance());
            System.out.println("------------------------\n");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // We can use the real API service for the test
        DeckApiService deckApiService = new DeckApiService();
        DeckProvider deckProvider = deckApiService; 
        
        Player player = new Player(1000);
        Dealer dealer = new Dealer(); 
        
        PlaceBetAndDealOutputBoundary presenter = new DummyPlaceBetPresenter();

        // Create a simple anonymous DataAccess for this test
        GameDataAccess dummyDataAccess = new GameDataAccess() {
            public Player getPlayer(String id) { return player; }
            public Dealer getDealer() { return dealer; }
            public Card drawCard() { return deckProvider.drawCard(); }
            public double getBetAmount(int i) { return 0; }
            public void markHandComplete(int i) {}
            public boolean isHandComplete(int i) { return false; }
            public boolean allHandsComplete() { return false; }
            public String getGameState() { return ""; }
            public void setGameState(String s) {}
            public void setBetAmount(int i, double v) {}
            public void addHandBet(int i, double v) {}
        };

        PlaceBetAndDealInteractor interactor = new PlaceBetAndDealInteractor(
            deckProvider, player, dealer, presenter, dummyDataAccess
        );
        
        // Initialize the Controller with the Interactor
        PlaceBetAndDealController controller = new PlaceBetAndDealController(interactor);

        System.out.println("Initial Player balance: " + player.getBalance());
        System.out.println("Place bet: ");
        
        if(sc.hasNextDouble()) {
            double amount = sc.nextDouble();
            controller.execute(amount);
            System.out.println("Final Player balance (after bet deduction): " + player.getBalance());
        } else {
            System.out.println("Invalid input.");
        }
    }
}