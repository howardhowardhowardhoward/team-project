package interface_adapters.playeraction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class PlayerActionViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private List<String> playerCardImages = new ArrayList<>();
    private List<String> dealerCardImages = new ArrayList<>();
    private int playerTotal;
    private int dealerVisibleTotal;
    private boolean playerBust;
    private boolean playerBlackjack;
    private boolean actionComplete;
    private String errorMessage;
    private String roundMessage;
    private double balance;
    private double betAmount;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void setPlayerCardImages(List<String> images) {
        this.playerCardImages = images;
        support.firePropertyChange("playerCards", null, images);
    }

    public void setDealerCardImages(List<String> images) {
        this.dealerCardImages = images;
        support.firePropertyChange("dealerCards", null, images);
    }

    public void setPlayerTotal(int total) {
        this.playerTotal = total;
        support.firePropertyChange("playerTotal", null, total);
    }

    public void setDealerVisibleTotal(int total) {
        this.dealerVisibleTotal = total;
        support.firePropertyChange("dealerTotal", null, total);
    }

    public void setPlayerBust(boolean bust) {
        this.playerBust = bust;
        support.firePropertyChange("playerBust", null, bust);
    }

    public void setPlayerBlackjack(boolean blackjack) {
        this.playerBlackjack = blackjack;
        support.firePropertyChange("playerBlackjack", null, blackjack);
    }

    public void setActionComplete(boolean complete) {
        this.actionComplete = complete;
        support.firePropertyChange("playerActionComplete", null, complete);
    }

    public void setError(String message) {
        this.errorMessage = message;
        support.firePropertyChange("error", null, message);
    }

    public void fireRoundMessage(String message) {
        this.roundMessage = message;
        support.firePropertyChange("roundMessage", null, message);
    }

    public void fireRoundComplete() {
        support.firePropertyChange("roundComplete", null, null);
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

    public List<String> getPlayerCardImages() { return playerCardImages; }
    public List<String> getDealerCardImages() { return dealerCardImages; }
    public int getPlayerTotal() { return playerTotal; }
    public int getDealerVisibleTotal() { return dealerVisibleTotal; }
    public boolean isPlayerBust() { return playerBust; }
    public boolean isPlayerBlackjack() { return playerBlackjack; }
    public boolean isActionComplete() { return actionComplete; }
    public String getErrorMessage() { return errorMessage; }
    public String getRoundMessage() { return roundMessage; }
}
