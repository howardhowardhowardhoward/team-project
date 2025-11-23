package entities;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private double balance;
    private Hand hand1;
    private Hand hand2;

    public Player(double initialBalance) {
        this.balance = initialBalance;
        this.hand1 = new Hand();
        this.hand2 = null;
    }

    public Hand getHand() {
        return this.hand1;
    }

    public Hand getHand1() {
        return hand1;
    }

    public Hand getHand2() {
        return hand2;
    }

    public List<Hand> getHands() {
        List<Hand> hands = new ArrayList<>();
        hands.add(hand1);
        if (hand2 != null) {
            hands.add(hand2);
        }
        return hands;
    }

    public boolean hasSplit() {
        return hand2 != null;
    }

    public void adjustBalance(double amount) {
        this.balance += amount;
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

    public double getBalance() {
        return balance;
    }

    public boolean isBlackjack() {
        return this.hand1.isBlackjack();
    }

    // Ensure clearHands method appears only once
    public void clearHands() {
        this.hand1.clear();
        if (this.hand2 != null) {
            this.hand2.clear();
            this.hand2 = null;
        }
    }

    public void split() {
        if (hasSplit()) {
            throw new IllegalStateException("Cannot split more than once.");
        }
        this.hand2 = new Hand();
    }
    
    // Added for compatibility with PlayerActionInteractor split logic
    public void addHand(Hand hand) {
        if (this.hand2 != null) {
             throw new IllegalStateException("Already split");
        }
        this.hand2 = hand;
    }
    
    // Added for compatibility with PlayerActionInteractor getHand(index) logic
    public Hand getHand(int index) {
        if (index == 0) return hand1;
        if (index == 1 && hand2 != null) return hand2;
        throw new IndexOutOfBoundsException("Invalid hand index: " + index);
    }
}