package interface_adapters;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class DealerActionViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private List<String> playerCardImages = new ArrayList<>();
    private List<String> dealerCardImages = new ArrayList<>();
    private int playerTotal;
    private int dealerTotal;
    private boolean dealerBust;
    private boolean dealerBlackjack;
    private boolean actionComplete;
    private String errorMessage;

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

    public void setDealerTotal(int total) {
        this.dealerTotal = total;
        support.firePropertyChange("dealerTotal", null, total);
    }

    public void setDealerBust(boolean bust) {
        this.dealerBust = bust;
        support.firePropertyChange("dealerBust", null, bust);
    }

    public void setDealerBlackjack(boolean blackjack) {
        this.dealerBlackjack = blackjack;
        support.firePropertyChange("dealerBlackjack", null, blackjack);
    }

    public void setActionComplete(boolean complete) {
        this.actionComplete = complete;
        support.firePropertyChange("dealerActionComplete", null, complete);
    }

    public void setError(String message) {
        this.errorMessage = message;
        support.firePropertyChange("error", null, message);
    }

    public void fireRoundComplete() {
        support.firePropertyChange("roundComplete", null, null);
    }

    public List<String> getPlayerCardImages() { return playerCardImages; }
    public List<String> getDealerCardImages() { return dealerCardImages; }
    public int getPlayerTotal() { return playerTotal; }
    public int getDealerTotal() { return dealerTotal; }
    public boolean isDealerBust() { return dealerBust; }
    public boolean isDealerBlackjack() { return dealerBlackjack; }
    public boolean isActionComplete() { return actionComplete; }
    public String getErrorMessage() { return errorMessage; }
}
