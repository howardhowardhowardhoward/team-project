package usecase.PlaceBetAndDeal;

import entities.*;
import frameworks_and_drivers.DeckApiService;
import frameworks_and_drivers.Deck; 
import usecase.DeckProvider; // <--- FIXED: Import required for interface

import java.util.Scanner;

/**
 * Console simulation test for the PlaceBetAndDeal use case.
 */
public class PlaceBetAndDealTest {

    // --- 1. Dummy Presenter Implementation for Testing ---
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
        DeckApiService deckApiService = new DeckApiService();
        
        // Fix: Import usecase.DeckProvider so this line compiles
        DeckProvider deckProvider = new Deck(deckApiService); 
        
        Player player = new Player(1000);
        Dealer dealer = new Dealer(); 
        
        PlaceBetAndDealOutputBoundary presenter = new DummyPlaceBetPresenter();

        // --- FIXED: Create a Dummy Data Access ---
        // The Interactor now requires GameDataAccess to save the bet.
        // We create a simple anonymous implementation for this test.
        usecase.PlayerActions.GameDataAccess dummyDataAccess = new usecase.PlayerActions.GameDataAccess() {
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

        // FIXED: Pass all 5 arguments (added dummyDataAccess)
        PlaceBetAndDealInteractor interactor = new PlaceBetAndDealInteractor(
            deckProvider, player, dealer, presenter, dummyDataAccess
        );
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