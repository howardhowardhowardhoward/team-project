package usecase.DealerAction;
import entities.*;

public class DealerActionResponseModel {
    private final Hand dealerHand;
    private final int dealerScore;
    private final boolean isBust;
    private final boolean isBlackjack;

    public DealerActionResponseModel(Hand dealerHand, int dealerScore, boolean isBust, boolean isBlackjack) {
        this.dealerHand = dealerHand;
        this.dealerScore = dealerScore;
        this.isBust = isBust;
        this.isBlackjack = isBlackjack;
    }

    public boolean isBlackjack() {
        return isBlackjack;
    }

    public boolean isBust() {
        return isBust;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public int getDealerScore() {
        return dealerScore;
    }
}
