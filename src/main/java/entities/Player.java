package entities;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private double balance;
    private List<Hand> hands;  // Changed to support multiple hands for SPLIT functionality

    public Player(double initialBalance) {
        this.balance = initialBalance;
        this.hands = new ArrayList<>();
        this.hands.add(new Hand());  // Start with one hand
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

    // Legacy method for backward compatibility - returns primary hand
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