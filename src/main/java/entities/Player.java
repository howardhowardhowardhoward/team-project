package entities;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String playerId;
    private String username;

    private double balance;

    private final List<Hand> hands;


    public Player(String playerId, String username, double initialBalance) {
        this.playerId = playerId;
        this.username = username;
        this.balance = initialBalance;

        this.hands = new ArrayList<>();
    }


    public void adjustBalance(double amount) {
        this.balance += amount;
    }

    public void addHand(Hand hand) {
        this.hands.add(hand);
    }

    public void clearHands() {
        this.hands.clear();
    }


    public String getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public List<Hand> getHands() {
        return hands;
    }

    public Hand getHand(int index) {
        if (index >= 0 && index < hands.size()) {
            return hands.get(index);
        }
        return null;
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
}