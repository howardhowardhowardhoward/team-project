package usecase.PlayerActions;

import java.util.List;

public class PlayerActionOutputData {

    private final List<String> newPlayerCardImages;
    private final List<String> dealerCardImages;
    private final int playerTotal;
    private final int dealerVisibleTotal;
    private final boolean playerBust;
    private final boolean playerBlackjack;
    private final boolean actionComplete;
    private final double balance;
    private final double betAmount;

    public PlayerActionOutputData(List<String> newPlayerCardImages,
                                  List<String> dealerCardImages,
                                  int playerTotal,
                                  int dealerVisibleTotal,
                                  boolean playerBust,
                                  boolean playerBlackjack,
                                  boolean actionComplete,
                                  double balance,
                                  double betAmount) {
        this.newPlayerCardImages = newPlayerCardImages;
        this.dealerCardImages = dealerCardImages;
        this.playerTotal = playerTotal;
        this.dealerVisibleTotal = dealerVisibleTotal;
        this.playerBust = playerBust;
        this.playerBlackjack = playerBlackjack;
        this.actionComplete = actionComplete;
        this.balance = balance;
        this.betAmount = betAmount;
    }

    public List<String> getNewPlayerCardImages() { return newPlayerCardImages; }
    public List<String> getDealerCardImages() { return dealerCardImages; }
    public int getPlayerTotal() { return playerTotal; }
    public int getDealerVisibleTotal() { return dealerVisibleTotal; }
    public boolean isPlayerBust() { return playerBust; }
    public boolean isPlayerBlackjack() { return playerBlackjack; }
    public boolean isActionComplete() { return actionComplete; }
    public double getBalance() {
        return balance;
    }
    public double getBetAmount() { return betAmount; }
}
