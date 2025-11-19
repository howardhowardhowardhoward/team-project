package usecase.PlaceBetAndDeal;

import entities.Card;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class PlaceBetAndDealViewModel {
    private List<Card> playerCards;
    private List<Card> dealerCards;
    private int playerTotal;
    private int dealerVisibleTotal;
    private double balance;
    private double betAmount;
    private boolean initialBlackjack;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void fireStateChanged() {
        support.firePropertyChange("state", null, this);
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
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public double getBetAmount() {
        return betAmount;
    }
    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }
    public boolean isInitialBlackjack() {
        return initialBlackjack;
    }
    public void setInitialBlackjack(boolean initialBlackjack) {
        this.initialBlackjack = initialBlackjack;
    }
}
