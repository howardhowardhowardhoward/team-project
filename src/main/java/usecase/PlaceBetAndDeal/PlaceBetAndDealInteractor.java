package usecase.PlaceBetAndDeal;

import entities.*;

public class PlaceBetAndDealInteractor {
    private Deck deck;
    private Player player;
    private Dealer dealer;

    public PlaceBetAndDealInteractor(Deck deck, Player player, Dealer dealer) {
        this.deck = deck;
        this.player = player;
        this.dealer = dealer;
    }

    public Bet execute(double betAmount) {
        player.placeBet(betAmount);
        Bet bet = new Bet(betAmount, BetType.MAIN);

        // clear hands for new round
        player.clearHands();
        dealer.getHand().clear();
        Hand playerHand = new Hand();
        Hand dealerHand = dealer.getHand();

        // dealing cards
        playerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard()); // should be face down
        dealerHand.addCard(deck.drawCard()); // should be face up

        player.addHand(playerHand);

        return bet;
    }
}
