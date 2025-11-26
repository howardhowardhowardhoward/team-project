package usecase.PlaceBetAndDeal;

import entities.*;
import usecase.DeckProvider;
import usecase.PlayerActions.GameDataAccess;

import java.util.ArrayList;
import java.util.List;

public class PlaceBetAndDealInteractor implements PlaceBetAndDealInputBoundary {
    private final DeckProvider deck;
    private final Player player;
    private final Dealer dealer;
    private final PlaceBetAndDealOutputBoundary presenter;
    private final GameDataAccess gameDataAccess;

    public PlaceBetAndDealInteractor(DeckProvider deck, Player player, Dealer dealer,
                                     PlaceBetAndDealOutputBoundary presenter,
                                     GameDataAccess gameDataAccess) {
        this.deck = deck;
        this.player = player;
        this.dealer = dealer;
        this.presenter = presenter;
        this.gameDataAccess = gameDataAccess;
    }

    @Override
    public void execute(PlaceBetAndDealInputData inputData) {
        try {
            double betAmount = inputData.getBetAmount();

            if (betAmount <= 0 || betAmount > player.getBalance()) return;

            // 1. Place bet
            player.placeBet(betAmount);
            
            // 2. Save bet for future calculation
            gameDataAccess.setBetAmount(0, betAmount);
            gameDataAccess.setGameState("PLAYER_TURN");

            Bet bet = new Bet(betAmount, BetType.MAIN);

            // 3. Deal
            player.clearHands();
            dealer.getHand().clear();

            List<Card> playerCards = deck.drawCards(2);
            List<Card> dealerCards = deck.drawCards(2);

            player.getHand().addCard(playerCards.get(0));
            player.getHand().addCard(playerCards.get(1));
            dealer.getHand().addCard(dealerCards.get(0)); 
            dealer.getHand().addCard(dealerCards.get(1)); 

            int playerTotal = player.getHand().getTotalPoints();
            int dealerVisibleTotal = dealerCards.get(0).getValue();
            boolean playerBlackjack = player.isBlackjack();

            // 4. Hide dealer's second card
            List<Card> visibleDealerCards = new ArrayList<>();
            visibleDealerCards.add(dealerCards.get(0));
            visibleDealerCards.add(null); // Hidden

            PlaceBetAndDealOutputData outputData = new PlaceBetAndDealOutputData(
                    bet, 
                    playerCards, 
                    visibleDealerCards, 
                    playerTotal, 
                    dealerVisibleTotal,
                    player.getBalance(), 
                    betAmount, 
                    playerBlackjack
            );

            presenter.present(outputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}