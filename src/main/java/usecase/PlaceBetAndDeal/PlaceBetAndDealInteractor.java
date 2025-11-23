package usecase.PlaceBetAndDeal;

import entities.*;
import usecase.DeckProvider;

import java.util.List;

public class PlaceBetAndDealInteractor implements PlaceBetAndDealInputBoundary {
    private final DeckProvider deck;
    private final Player player;
    private final Dealer dealer;
    private final PlaceBetAndDealOutputBoundary presenter;

    public PlaceBetAndDealInteractor(DeckProvider deck, Player player, Dealer dealer,
                                     PlaceBetAndDealOutputBoundary presenter) {
        this.deck = deck;
        this.player = player;
        this.dealer = dealer;
        this.presenter = presenter;
    }

    @Override
    public void execute(PlaceBetAndDealInputData inputData) {
        try {
            double balance = player.getBalance();
            double betAmount = inputData.getBetAmount();
            boolean playerBlackjack = player.isBlackjack();

            // Place bet
            player.placeBet(betAmount);
            Bet bet = new Bet(betAmount, BetType.MAIN);

            // Reset hands
            player.clearHands();
            dealer.getHand().clear();

            // Draw cards
            List<Card> playerCards = deck.drawCards(2);
            List<Card> dealerCards = deck.drawCards(2);

            Hand playerHand = player.getHand();
            Hand dealerHand = dealer.getHand();

            playerHand.addCard(playerCards.get(0));
            playerHand.addCard(playerCards.get(1));
            dealerHand.addCard(dealerCards.get(0)); // visible
            dealerHand.addCard(dealerCards.get(1)); // hidden

            int playerTotal = playerHand.getTotalPoints();
            int dealerVisibleTotal = dealerCards.get(0).getValue();

            // Output
            PlaceBetAndDealOutputData outputData = new PlaceBetAndDealOutputData(
                    bet, playerCards, dealerCards, playerTotal, dealerVisibleTotal,
                    balance, betAmount, playerBlackjack
            );

            presenter.present(outputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
