package interface_adapter.PlayerActions;

import entities.Card;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * ViewModel for Player Actions.
 * Holds the state of the view after a player action (Hit, Stand, etc.).
 */
public class PlayerActionViewModel {
    private String message;
    private List<Card> playerCards;
    private List<Card> dealerCards;
    private int playerTotal;
    private int dealerTotal;
    private double balance;
    private boolean isGameButtonsEnabled;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void fireStateChanged() {
        support.firePropertyChange("state", null, this);
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Card> getPlayerCards() { return playerCards; }
    public void setPlayerCards(List<Card> playerCards) { this.playerCards = playerCards; }

    public List<Card> getDealerCards() { return dealerCards; }
    public void setDealerCards(List<Card> dealerCards) { this.dealerCards = dealerCards; }

    public int getPlayerTotal() { return playerTotal; }
    public void setPlayerTotal(int playerTotal) { this.playerTotal = playerTotal; }

    public int getDealerTotal() { return dealerTotal; }
    public void setDealerTotal(int dealerTotal) { this.dealerTotal = dealerTotal; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public boolean isGameButtonsEnabled() { return isGameButtonsEnabled; }
    public void setGameButtonsEnabled(boolean gameButtonsEnabled) { isGameButtonsEnabled = gameButtonsEnabled; }
}