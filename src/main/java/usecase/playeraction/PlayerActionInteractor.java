package usecase.playeraction;

import entities.Deck;
import entities.Player;
import entities.Dealer;
import entities.Hand;
import entities.Card;
import usecase.dealeraction.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerActionInteractor implements PlayerActionInputBoundary {

    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private final PlayerActionOutputBoundary presenter;
    private final DealerActionInteractor dealerInteractor;

    public PlayerActionInteractor(Deck deck, Player player, Dealer dealer, PlayerActionOutputBoundary presenter,
                                  DealerActionInteractor dealerInteractor) {
        this.deck = deck;
        this.player = player;
        this.dealer = dealer;
        this.presenter = presenter;
        this.dealerInteractor = dealerInteractor;
    }

    @Override
    public void hit() {
        try {
            double balance = player.getBalance();
            double betAmount = player.getCurrentBet();
            Hand hand = player.getHand(0);
            Card drawnCard = deck.drawCard();
            hand.addCard(drawnCard);

            int playerTotal = hand.getTotalPoints();
            boolean bust = hand.isBust();
            int dealerVisibleTotal = dealer.getHand().getCards().get(1).getValue();

            List<String> newPlayerImages = new ArrayList<>();
            for (Card c : hand.getCards()) {
                newPlayerImages.add(c.getImage());
            }

            PlayerActionOutputData outputData = new PlayerActionOutputData(
                    newPlayerImages,
                    null,
                    playerTotal,
                    dealerVisibleTotal,
                    bust,
                    hand.isBlackjack(),
                    bust, // actionComplete true if bust
                    balance,
                    betAmount
            );

            presenter.present(outputData);
        } catch (Exception e) {
            presenter.presentError("Error during hit: " + e.getMessage());
        }
    }

    @Override
    public void stand() {
        double balance = player.getBalance();
        double betAmount = player.getCurrentBet();
        Hand hand = player.getHand(0);
        int dealerVisibleTotal = dealer.getHand().getCards().get(1).getValue();

        PlayerActionOutputData outputData = new PlayerActionOutputData(
                null,
                null,
                hand.getTotalPoints(),
                dealerVisibleTotal,
                hand.isBust(),
                hand.isBlackjack(),
                true,
                balance,
                betAmount
        );

        presenter.present(outputData);

        // Trigger dealer play on a separate thread so GUI stays responsive
        new Thread(dealerInteractor::dealerPlay).start();
    }

    @Override
    public void doubleDown() {
        if (player.getCurrentBet() > player.getBalance()) {
            presenter.presentError("Insufficient funds to double down.");
        }
        else if (player.getHand(0).getCards().size() > 2) {
            presenter.presentError("You cannot double down after hitting.");
        }
        else {
            double balance = player.getBalance();
            double betAmount = player.getCurrentBet();
            double newBalance = balance - betAmount;
            double newBetAmount = betAmount * 2;

            // draw just one more card
            Hand hand = player.getHand(0);
            Card drawnCard = deck.drawCard();
            hand.addCard(drawnCard);

            int playerTotal = hand.getTotalPoints();
            boolean bust = hand.isBust();
            int dealerVisibleTotal = dealer.getHand().getCards().get(1).getValue();

            List<String> newPlayerImages = new ArrayList<>();
            for (Card c : hand.getCards()) {
                newPlayerImages.add(c.getImage());
            }

            // update Player
            player.setBalance(newBalance);
            player.setCurrentBet(newBetAmount);

            PlayerActionOutputData outputData = new PlayerActionOutputData(
                    newPlayerImages,
                    null,
                    playerTotal,
                    dealerVisibleTotal,
                    bust,
                    hand.isBlackjack(),
                    true, // actionComplete true if doubling down
                    newBalance,
                    newBetAmount
            );

            presenter.present(outputData);

            // If player not bust, trigger dealer play on a separate thread so GUI stays responsive
            if (!bust) {
                new Thread(dealerInteractor::dealerPlay).start();
            }
        }
    }

    @Override
    public void split() {
        // TODO: implement split
        presenter.presentError("Split not implemented yet");
    }

    @Override
    public void insurance() {
        try {
            // insurance
            if (player.hasInsurance()) {
                presenter.presentError("Insurance already purchased for this round");
                return;
            }

            // dealer upcard
            List<Card> dealerCards = dealer.getHand().getCards();
            if (dealerCards.isEmpty()) {
                presenter.presentError("Cannot take insurance - dealer has no cards");
                return;
            }

            Card dealerUpCard = dealerCards.get(1);
            if (!dealerUpCard.getRank().equalsIgnoreCase("ACE")) {
                presenter.presentError("Insurance only available when dealer shows Ace");
                return;
            }

            double originalBet = player.getCurrentBet();
            double insuranceBet = originalBet / 2.0;
            double balance = player.getBalance();

            if (balance < insuranceBet) {
                presenter.presentError(String.format(
                        "Insufficient balance for insurance. Need $%.2f, have $%.2f",
                        insuranceBet, balance
                ));
                return;
            }

            // insurance
            player.setInsurance(true);
            player.setBalance(balance - insuranceBet);

            // dealer blackjack
            Hand dealerHand = dealer.getHand();
            boolean dealerBlackjack = dealerHand.isBlackjack();

            String message;
            double newBalance;

            if (dealerBlackjack) {
                double payout = insuranceBet * 3.0;
                newBalance = player.getBalance() + payout;
                player.setBalance(newBalance);
                message = String.format(
                        "Insurance wins! Dealer has Blackjack. Won $%.2f",
                        payout - insuranceBet
                );
            } else {
                newBalance = player.getBalance();
                message = String.format(
                        "Insurance loses. Dealer does not have Blackjack. Lost $%.2f",
                        insuranceBet
                );
            }

            Hand playerHand = player.getHand(0);
            int dealerVisibleTotal = dealerCards.get(1).getValue();

            List<String> playerImages = new ArrayList<>();
            for (Card c : playerHand.getCards()) {
                playerImages.add(c.getImage());
            }

            PlayerActionOutputData outputData = new PlayerActionOutputData(
                    playerImages,
                    null,
                    playerHand.getTotalPoints(),
                    dealerVisibleTotal,
                    playerHand.isBust(),
                    playerHand.isBlackjack(),
                    dealerBlackjack,
                    newBalance,
                    originalBet
            );

            presenter.present(outputData);

            if (dealerBlackjack) {
                handleRoundResult();
            }

        } catch (Exception e) {
            presenter.presentError("Error during insurance: " + e.getMessage());
        }
        //presenter.presentError("Insurance not implemented yet");
    }


    @Override
    public void handleRoundResult() {

        Hand playerHand = player.getHand(0);
        Hand dealerHand = dealer.getHand();
        double balance = player.getBalance();
        double betAmount = player.getCurrentBet();

        int playerScore = playerHand.getTotalPoints();
        int dealerScore = dealerHand.getTotalPoints();

        boolean playerBlackjack = playerHand.isBlackjack();
        boolean dealerBlackjack = dealerHand.isBlackjack();
        boolean playerBust = playerHand.isBust();
        boolean dealerBust = dealerHand.isBust();

        double payout = 0;
        String message;

        if (playerBust) {
            // player loses, no return
            payout = 0;
            message = "Player busts! Dealer wins.";
        }
        else if (dealerBust) {
            payout = betAmount * 2;
            message = "Dealer busts! You win!";
        }
        else if (playerBlackjack && !dealerBlackjack) {
            payout = betAmount * 2.5; // 3:2 blackjack
            message = "Blackjack! You win!";
        }
        else if (!playerBlackjack && dealerBlackjack) {
            payout = 0;
            message = "Dealer has blackjack. You lose.";
        }
        else if (playerScore > dealerScore) {
            payout = betAmount * 2;
            message = "You win!";
        }
        else if (dealerScore > playerScore) {
            payout = 0;
            message = "Dealer wins.";
        }
        else {
            // push, return bet only
            payout = betAmount;
            message = "Push.";
        }

        double newBalance = balance + payout;

        // update Player entity
        player.setBalance(newBalance);
        player.setCurrentBet(0);

        // push into ViewModel
        presenter.presentResult(message, newBalance, 0);
    }

    public Player getPlayer() {
        return player;
    }

    public Deck getDeck() {
        return deck;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
