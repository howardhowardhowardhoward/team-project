package entities;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private double balance;
    private List<Hand> hands = new ArrayList<>();
    private double currentBet;


    public Player(double initialBalance) {
        this.balance = initialBalance;
        hands.add(new Hand());
    }

    public void setBalance(double balance) {this.balance = balance;}

    public void adjustBalance(double amount) {
        this.balance += amount;
    }

    public void clearHands() {
        hands.clear();
        hands.add(new Hand()); // always have at least one hand
    }

    public double getBalance() {
        return balance;
    }

    public Hand getHand(int index) {
        return hands.get(index);
    }

    public void addHand(Hand hand) {
        hands.add(hand);
    }

    public List<Hand> getHands() {return hands;}

    public void placeBet(double amount, double reservedAmount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        double balanceBeforeBet = balance + reservedAmount;
        if (amount > balanceBeforeBet) {
            throw new IllegalArgumentException("Not enough balance.");
        }
        // this.balance -= amount;
        this.currentBet = amount;
    }

    public boolean isBlackjack(int index){
        return hands.get(index).isBlackjack();
    }

    public double getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(double amount) {
        this.currentBet = amount;
    }
}
