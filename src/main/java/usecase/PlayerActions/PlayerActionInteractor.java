package usecase.PlayerActions;

import entities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerActionInteractor implements PlayerActionInputBoundary {

    private final PlayerActionOutputBoundary outputBoundary;
    private final GameDataAccess gameDataAccess;

    public PlayerActionInteractor(
            PlayerActionOutputBoundary outputBoundary,
            GameDataAccess gameDataAccess) {
        this.outputBoundary = outputBoundary;
        this.gameDataAccess = gameDataAccess;
    }


    @Override
    public void execute(PlayerActionInputData inputData) {
        String action = inputData.getAction();
        try {
            switch(action) {
                case "HIT": executeHit(inputData); break;
                case "STAND": executeStand(inputData); break;
                case "DOUBLE": executeDouble(inputData); break;
                case "SPLIT": executeSplit(inputData); break;
                case "INSURANCE": executeInsurance(inputData); break;
                default: handleInvalidAction(inputData, "Unknown action: " + action);
            }
        } catch (Exception e) {
            handleError(inputData, e);
        }
    }

    // --- HELPER: Adapter for hand1/hand2 ---
    private Hand getHandByIndex(Player player, int index) {
        if (index == 0) return player.getHand();
        if (index == 1) return player.getHand2();
        throw new IllegalArgumentException("Invalid hand index: " + index);
    }

    private List<Hand> getPlayerHandsList(Player player) {
        List<Hand> hands = new ArrayList<>();
        if (player.getHand() != null) hands.add(player.getHand());
        if (player.hasSplit() && player.getHand2() != null) hands.add(player.getHand2());
        return hands;
    }

    private void executeHit(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand = getHandByIndex(player, inputData.getHandIndex());

        if (hand.isBust() || hand.getTotalPoints() == 21) {
            handleInvalidAction(inputData, "Cannot hit");
            return;
        }

        Card newCard = gameDataAccess.drawCard();
        hand.addCard(newCard);
        int newTotal = hand.getTotalPoints();
        boolean bust = hand.isBust();

        List<String> availableActions = new ArrayList<>();
        if (bust) {
            gameDataAccess.markHandComplete(inputData.getHandIndex());
            if (gameDataAccess.allHandsComplete()) {
                executeStand(inputData);
                return;
            } else availableActions.add("NEXT_HAND");
        } else if (newTotal == 21) availableActions.add("STAND");
        else { availableActions.add("HIT"); availableActions.add("STAND"); }

        String message = String.format("Drew %s. Total: %d", newCard.toString(), newTotal) + (bust ? " - BUST!" : "");
        outputBoundary.present(new PlayerActionOutputData(true, message, newTotal, bust, hand.isBlackjack(), availableActions));
    }

    private void executeStand(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand currentHand = getHandByIndex(player, inputData.getHandIndex());
        gameDataAccess.markHandComplete(inputData.getHandIndex());

        if (!gameDataAccess.allHandsComplete()) {
            int nextHandIndex = findNextIncompleteHand(player);
            if (nextHandIndex != -1) {
                outputBoundary.present(new PlayerActionOutputData(true, "Moving to next hand...",
                        currentHand.getTotalPoints(), false, false, getAvailableActions(getHandByIndex(player, nextHandIndex), player, nextHandIndex)));
                return;
            }
        }

        gameDataAccess.setGameState("DEALER_TURN");
        Dealer dealer = gameDataAccess.getDealer();

        while (dealer.GetDealerScore() < 17) {
            Card newCard = gameDataAccess.drawCard();
            dealer.draw(newCard);
        }

        int dealerScore = dealer.GetDealerScore();
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append(String.format("Dealer: %d\nResults:\n", dealerScore));

        double totalPayout = 0.0;
        List<Hand> allHands = getPlayerHandsList(player);
        for (int i = 0; i < allHands.size(); i++) {
            Hand h = allHands.get(i);
            double payout = calculatePayout(h, dealer, gameDataAccess.getBetAmount(i));
            resultMessage.append(String.format("Hand %d: %s\n", i+1, determineHandResult(h, dealer)));
            totalPayout += payout;
        }

        if (totalPayout > 0) {
            player.adjustBalance(totalPayout);
        }

        outputBoundary.present(new PlayerActionOutputData(true, resultMessage.toString(),
                currentHand.getTotalPoints(), false, false, Arrays.asList("NEW_ROUND"), true, "COMPLETE", dealerScore));
    }

    private void executeDouble(PlayerActionInputData inputData) {
        //Simplified double logic
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand = getHandByIndex(player, inputData.getHandIndex());
        double bet = gameDataAccess.getBetAmount(inputData.getHandIndex());

        if (player.getBalance() >= bet) {
            player.adjustBalance(-bet);
            gameDataAccess.setBetAmount(inputData.getHandIndex(), bet * 2);
            hand.addCard(gameDataAccess.drawCard());
            gameDataAccess.markHandComplete(inputData.getHandIndex());
            if (gameDataAccess.allHandsComplete()) executeStand(inputData);
            else outputBoundary.present(new PlayerActionOutputData(true, "Doubled!", hand.getTotalPoints(), hand.isBust(), false, Arrays.asList("NEXT_HAND")));
        } else handleInvalidAction(inputData, "Insufficient balance");
    }

    private void executeSplit(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand1 = player.getHand();
        double bet = gameDataAccess.getBetAmount(inputData.getHandIndex());

        if (player.getBalance() >= bet && hand1.canSplit() && !player.hasSplit()) {
            player.adjustBalance(-bet);
            player.split();
            // Move card logic
            List<Card> cards = hand1.getCards();
            Card firstCard = cards.get(0);
            Card secondCard = cards.get(1);
            hand1.clear();
            hand1.addCard(firstCard);
            player.getHand2().addCard(secondCard);
            hand1.addCard(gameDataAccess.drawCard());
            player.getHand2().addCard(gameDataAccess.drawCard());
            gameDataAccess.setBetAmount(1, bet);

            outputBoundary.present(new PlayerActionOutputData(true, "Split successful!", hand1.getTotalPoints(), false, false, getAvailableActions(hand1, player, 0)));
        } else handleInvalidAction(inputData, "Cannot split");
    }

    private void executeInsurance(PlayerActionInputData inputData) {
        // Simplified insurance
        outputBoundary.present(new PlayerActionOutputData(true, "Insurance handled", 0, false, false, Arrays.asList("HIT", "STAND")));
    }

    // Helpers
    private int findNextIncompleteHand(Player player) {
        if (!gameDataAccess.isHandComplete(0)) return 0;
        if (player.hasSplit() && !gameDataAccess.isHandComplete(1)) return 1;
        return -1;
    }
    private List<String> getAvailableActions(Hand hand, Player player, int handIndex) {
        List<String> actions = new ArrayList<>();

        // If bust or reach 21, no available moves
        if (hand.isBust() || hand.getTotalPoints() == 21) {
            return actions;
        }

        // basic moves
        actions.add("HIT");
        actions.add("STAND");

        // Double only available at first two cards
        if (hand.getCards().size() == 2) {
            double betAmount = gameDataAccess.getBetAmount(handIndex);
            if (player.getBalance() >= betAmount) {
                actions.add("DOUBLE");
            }

            // Splitting is only allowed on pairs, and only if you haven't split yet.
            if (hand.canSplit() && !player.hasSplit() && player.getBalance() >= betAmount) {
                actions.add("SPLIT");
            }
        }

        return actions;
    }

    private String determineHandResult(Hand playerHand, Dealer dealer) {
        int playerScore = playerHand.getTotalPoints();
        int dealerScore = dealer.GetDealerScore();

        if (playerHand.isBust()) {
            return "LOSE (Bust)";
        } else if (dealer.isBust()) {
            return "WIN (Dealer Bust)";
        } else if (playerHand.isBlackjack() && !dealer.isBlackJack()) {
            return "BLACKJACK!";
        } else if (playerScore > dealerScore) {
            return "WIN";
        } else if (playerScore < dealerScore) {
            return "LOSE";
        } else {
            return "PUSH (Tie)";
        }
    }

    private double calculatePayout(Hand playerHand, Dealer dealer, double betAmount) {
        if (playerHand.isBust()) {
            return 0.0; // lose bet
        } else if (dealer.isBust()) {
            return betAmount * 2.0; // won 1:1, return principle and prize
        } else if (playerHand.isBlackjack() && !dealer.isBlackJack()) {
            return betAmount * 2.5; // Blackjack won 3:2, return principal+1.5x
        } else {
            int playerScore = playerHand.getTotalPoints();
            int dealerScore = dealer.GetDealerScore();

            if (playerScore > dealerScore) {
                return betAmount * 2.0; // won1:1
            } else if (playerScore == dealerScore) {
                return betAmount; // draw return principal
            } else {
                return 0.0; // lose
            }
        }
    }
    private void handleInvalidAction(PlayerActionInputData i, String r) { outputBoundary.present(new PlayerActionOutputData(false, r, 0, false, false, null)); }
    private void handleError(PlayerActionInputData i, Exception e) { outputBoundary.present(new PlayerActionOutputData(false, e.getMessage(), 0, false, false, null)); }
}
