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

    public double getBalance() {
        return balance;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setHand2(Hand hand){
        this.hand2 = hand;
    }

    public Hand getHand() {return hand;}

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



    public boolean hasSplit(){
        return this.hand2 != null;
    }

    public boolean isBlackjack() {
        // Check if primary hand is blackjack
        return hand.isBlackjack();
    }
}