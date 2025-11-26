package usecase.PlaceBetAndDeal;

import entities.*;
import java.util.List;

public class PlaceBetAndDealOutputData {
    private final Bet bet;
    private final List<Card> playerCards;
    private final List<Card> dealerCards;
    private final int playerTotal;
    private final int dealerVisibleTotal;
    private final double balance;
    private final double betAmount;
    private final boolean isBlackjack;

    public PlaceBetAndDealOutputData(Bet bet,
                                     List<Card> playerCards,
                                     List<Card> dealerCards,
                                     int playerTotal,
                                     int dealerVisibleTotal,
                                     double balance,
                                     double betAmount,
                                     boolean isBlackjack) {
        this.bet = bet;
        this.playerCards = playerCards;
        this.dealerCards = dealerCards;
        this.playerTotal = playerTotal;
        this.dealerVisibleTotal = dealerVisibleTotal;
        this.balance = balance;
        this.betAmount = betAmount;
        this.isBlackjack = isBlackjack;
    }

    public Bet getBet() {
        return bet;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public List<Card> getDealerCards() {
        return dealerCards;
    }

    public int getPlayerTotal() {
        return playerTotal;
    }

    public int getDealerVisibleTotal() {
        return dealerVisibleTotal;
    }

    public double getBalance() {
        return balance;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public boolean isBlackjack() {
        return isBlackjack;
    }
}
