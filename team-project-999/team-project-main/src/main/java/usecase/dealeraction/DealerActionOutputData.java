package usecase.dealeraction;

import entities.Card;
import java.util.List;

/**
 * Output data from DealerAction execution.
 */
public class DealerActionOutputData {
    private final List<Card> dealerCards;
    private final int dealerTotal;
    private final boolean dealerBust;
    private final String gameResultSummary;
    private final double finalBalance;

    public DealerActionOutputData(List<Card> dealerCards, int dealerTotal, boolean dealerBust, 
                                  String gameResultSummary, double finalBalance) {
        this.dealerCards = dealerCards;
        this.dealerTotal = dealerTotal;
        this.dealerBust = dealerBust;
        this.gameResultSummary = gameResultSummary;
        this.finalBalance = finalBalance;
    }

    // Getters
    public List<Card> getDealerCards() { return dealerCards; }
    public int getDealerTotal() { return dealerTotal; }
    public boolean isDealerBust() { return dealerBust; }
    public String getGameResultSummary() { return gameResultSummary; }
    public double getFinalBalance() { return finalBalance; }
}