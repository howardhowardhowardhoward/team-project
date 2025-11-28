package usecase.dealeraction;

import java.util.List;

public class DealerActionOutputData {
    private final List<String> playerCardImages;
    private final List<String> dealerCardImages;
    private final int playerTotal;
    private final int dealerTotal;
    private final boolean dealerBust;
    private final boolean dealerBlackjack;
    private final boolean dealerActionComplete;

    public DealerActionOutputData(List<String> playerCardImages, List<String> dealerCardImages,
                                  int playerTotal, int dealerTotal, boolean dealerBust,
                                  boolean dealerBlackjack, boolean dealerActionComplete) {
        this.playerCardImages = playerCardImages;
        this.dealerCardImages = dealerCardImages;
        this.playerTotal = playerTotal;
        this.dealerTotal = dealerTotal;
        this.dealerBust = dealerBust;
        this.dealerBlackjack = dealerBlackjack;
        this.dealerActionComplete = dealerActionComplete;
    }

    public List<String> getPlayerCardImages() { return playerCardImages; }
    public List<String> getDealerCardImages() { return dealerCardImages; }
    public int getPlayerTotal() { return playerTotal; }
    public int getDealerTotal() { return dealerTotal; }
    public boolean isDealerBust() { return dealerBust; }
    public boolean isDealerBlackjack() { return dealerBlackjack; }
    public boolean isActionComplete() { return dealerActionComplete; }
}
