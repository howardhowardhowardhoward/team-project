package entities;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private double balance;
    private Hand hand;
    private Hand hand2;


    public Player(double initialBalance) {
        this.balance = initialBalance;
        this.hand = new Hand();
        this.hand2 = new Hand();
    }

    public void adjustBalance(double amount) {
        this.balance += amount;
    }

    public void clearHands() {
        this.hands.clear();
        this.hands.add(new Hand());  // Reset to one empty hand
    }

    public double getBalance() {
        return balance;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setHand2(Hand hand){
        this.hand2 = hand;
    }

    public Hand getHand() {
        return hands.isEmpty() ? new Hand() : hands.get(0);
    }

    // New method to support multiple hands (for SPLIT)
    public Hand getHand(int index) {
        if (index < 0 || index >= hands.size()) {
            throw new IndexOutOfBoundsException("Invalid hand index: " + index);
        }
        return hands.get(index);
    }

    // Get all hands
    public List<Hand> getHands() {
        return new ArrayList<>(hands);  // Return defensive copy
    }

    // Add a new hand (used for SPLIT)
    public void addHand(Hand hand) {
        this.hands.add(hand);
    }

    public Hand getHand2() { return  hand2;}

    public void placeBet(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Not enough balance.");
        }
        this.balance -= amount;
    }

    public boolean isBlackjack() {
        // Check if primary hand is blackjack
        return !hands.isEmpty() && hands.get(0).isBlackjack();
    }
}