package interface_adapters;

import entities.Card;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class PlaceBetAndDealViewModel {
    private List<Card> playerCards;
    private List<Card> dealerCards;
    private int playerTotal;
    private int dealerVisibleTotal;
    private double balance;
    private double betAmount;
    private boolean initialBlackjack;
    private List<String> playerCardImages = new ArrayList<>();
    private List<String> dealerCardImages = new ArrayList<>();

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void fireStateChanged() {
        support.firePropertyChange("cardsDealt", null, this);
    }

    public void fireError(String message) {
        support.firePropertyChange("error", null, message);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }
    public void setPlayerCards(List<Card> playerCards) {
        this.playerCards = playerCards;
    }
    public List<Card> getDealerCards() {
        return dealerCards;
    }
    public void setDealerCards(List<Card> dealerCards) {
        this.dealerCards = dealerCards;
    }
    public int getPlayerTotal() {
        return playerTotal;
    }
    public void setPlayerTotal(int playerTotal) {
        this.playerTotal = playerTotal;
    }
    public int getDealerVisibleTotal() {
        return dealerVisibleTotal;
    }
    public void setDealerVisibleTotal(int dealerVisibleTotal) {
        this.dealerVisibleTotal = dealerVisibleTotal;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double newBalance) {
        double old = this.balance;
        this.balance = newBalance;
        support.firePropertyChange("balance", old, newBalance);
    }
    public double getBetAmount() {
        return betAmount;
    }
    public void setBetAmount(double newBet) {
        double old = this.betAmount;
        this.betAmount = newBet;
        support.firePropertyChange("bet", old, newBet);
    }
    public boolean isInitialBlackjack() {
        return initialBlackjack;
    }
    public void setInitialBlackjack(boolean initialBlackjack) {
        this.initialBlackjack = initialBlackjack;
    }
    public List<String> getPlayerCardImages() {
        return playerCardImages;
    }
    public List<String> getDealerCardImages() {
        return dealerCardImages;
    }
    public void setCards(List<String> player, List<String> dealer) {
        this.playerCardImages = player;
        this.dealerCardImages = dealer;
        support.firePropertyChange("cardsDealt", null, null);
    }
}
