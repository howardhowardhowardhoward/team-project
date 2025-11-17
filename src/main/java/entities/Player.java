package entities;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private double balance;
    private Hand hand;


    public Player(double initialBalance) {
        this.balance = initialBalance;
        this.hand = new Hand();
    }


    public void adjustBalance(double amount) {
        this.balance += amount;
    }

    public void clearHands() {
        this.hand.clear();
    }

    public double getBalance() {
        return balance;
    }

    public Hand getHand() {
        return hand;
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

    public boolean isBlackjack(){
        return this.hand.isBlackjack();
    }
}