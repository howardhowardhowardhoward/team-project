package usecase.PlayerActions;

import entities.*;
import usecase.dealeraction.DealerController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interactor for handling all player actions in Blackjack.
 * FIXED: Adapted to work with the new Player entity (hand1/hand2 structure).
 */
public class PlayerActionInteractor implements PlayerActionInputBoundary {

    private final PlayerActionOutputBoundary outputBoundary;
    private final GameDataAccess gameDataAccess;
    private final BalanceUpdater balanceUpdater;
    private final DealerController dealerController;

    public PlayerActionInteractor(
            PlayerActionOutputBoundary outputBoundary,
            GameDataAccess gameDataAccess,
            BalanceUpdater balanceUpdater,
            DealerController dealerController) {
        this.outputBoundary = outputBoundary;
        this.gameDataAccess = gameDataAccess;
        this.balanceUpdater = balanceUpdater;
        this.dealerController = dealerController;
    }

    public PlayerActionInteractor(
            PlayerActionOutputBoundary outputBoundary,
            GameDataAccess gameDataAccess) {
        this(outputBoundary, gameDataAccess, null, null);
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
        if (index == 0) return player.getHand1();
        if (index == 1) return player.getHand2();
        throw new IllegalArgumentException("Invalid hand index: " + index);
    }

    // --- HELPER: Convert hand1/hand2 to List for iteration ---
    private List<Hand> getPlayerHandsList(Player player) {
        List<Hand> hands = new ArrayList<>();
        if (player.getHand1() != null) hands.add(player.getHand1());
        if (player.hasSplit() && player.getHand2() != null) hands.add(player.getHand2());
        return hands;
    }

    /**
     * HIT: Draw one additional card
     */
    private void executeHit(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand = getHandByIndex(player, inputData.getHandIndex());

        if (hand.isBust()) {
            handleInvalidAction(inputData, "Cannot hit - hand is already bust");
            return;
        }
        if (hand.getTotalPoints() == 21) {
            handleInvalidAction(inputData, "Cannot hit - hand is already 21");
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
            } else {
                availableActions.add("NEXT_HAND");
            }
        } else if (newTotal == 21) {
            availableActions.add("STAND");
        } else {
            availableActions.add("HIT");
            availableActions.add("STAND");
        }

        String message = String.format("Drew %s. Total: %d", newCard.toString(), newTotal);
        if (bust) message += " - BUST!";

        outputBoundary.present(new PlayerActionOutputData(
                true, message, newTotal, bust, hand.isBlackjack(), availableActions
        ));
    }

    /**
     * STAND: End player's turn
     */
    private void executeStand(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand currentHand = getHandByIndex(player, inputData.getHandIndex());

        gameDataAccess.markHandComplete(inputData.getHandIndex());

        if (!gameDataAccess.allHandsComplete()) {
            int nextHandIndex = findNextIncompleteHand(player);
            // Check validity of next index
            if (nextHandIndex != -1) {
                Hand nextHand = getHandByIndex(player, nextHandIndex);
                outputBoundary.present(new PlayerActionOutputData(
                        true,
                        String.format("Hand %d stands. Moving to hand %d...", inputData.getHandIndex() + 1, nextHandIndex + 1),
                        currentHand.getTotalPoints(), false, false,
                        getAvailableActions(nextHand, player, nextHandIndex)
                ));
                return;
            }
        }

        // Dealer Turn
        gameDataAccess.setGameState("DEALER_TURN");
        Dealer dealer = gameDataAccess.getDealer();
        if (dealerController != null) dealerController.executeDealerTurn();
        else dealer.play();

        int dealerScore = dealer.GetDealerScore();
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append(String.format("Dealer: %d%s\n\nResults:\n",
                dealerScore, dealer.isBust() ? " (BUST)" : ""));

        double totalPayout = 0.0;
        List<Hand> allHands = getPlayerHandsList(player); // Use helper list

        for (int i = 0; i < allHands.size(); i++) {
            Hand playerHand = allHands.get(i);
            double betAmount = gameDataAccess.getBetAmount(i);

            String handResult = determineHandResult(playerHand, dealer);
            double payout = calculatePayout(playerHand, dealer, betAmount);

            resultMessage.append(String.format("Hand %d (%d): %s", i + 1, playerHand.getTotalPoints(), handResult));
            if (payout > 0) {
                resultMessage.append(String.format(" - Won $%.2f", payout));
                totalPayout += payout;
            }
            resultMessage.append("\n");
        }

        if (totalPayout > 0) {
            if (balanceUpdater != null) balanceUpdater.addBalance(inputData.getPlayerId(), totalPayout, "HAND_WIN");
            else player.adjustBalance(totalPayout);
        }

        resultMessage.append(String.format("\nTotal payout: $%.2f", totalPayout));
        gameDataAccess.setGameState("COMPLETE");

        outputBoundary.present(new PlayerActionOutputData(
                true, resultMessage.toString(), currentHand.getTotalPoints(),
                false, false, Arrays.asList("NEW_ROUND", "QUIT"),
                true, determineOverallResult(player, dealer), dealerScore
        ));
    }

    /**
     * DOUBLE: Double down
     */
    private void executeDouble(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand = getHandByIndex(player, inputData.getHandIndex());

        if (hand.getCards().size() != 2) {
            handleInvalidAction(inputData, "Can only double down on first two cards");
            return;
        }

        double originalBet = gameDataAccess.getBetAmount(inputData.getHandIndex());

        boolean balanceOk;
        if (balanceUpdater != null) balanceOk = balanceUpdater.deductBalance(inputData.getPlayerId(), originalBet, "DOUBLE_DOWN");
        else {
            balanceOk = player.getBalance() >= originalBet;
            if (balanceOk) player.adjustBalance(-originalBet);
        }

        if (!balanceOk) {
            handleInvalidAction(inputData, "Insufficient balance to double down");
            return;
        }

        gameDataAccess.setBetAmount(inputData.getHandIndex(), originalBet * 2);

        Card newCard = gameDataAccess.drawCard();
        hand.addCard(newCard);

        gameDataAccess.markHandComplete(inputData.getHandIndex());

        String message = String.format("Doubled! Drew %s. Total: %d", newCard.toString(), hand.getTotalPoints());
        if (hand.isBust()) message += " - BUST!";

        if (gameDataAccess.allHandsComplete()) executeStand(inputData);
        else outputBoundary.present(new PlayerActionOutputData(true, message, hand.getTotalPoints(), hand.isBust(), false, Arrays.asList("NEXT_HAND")));
    }

    /**
     * SPLIT: Split pairs
     */
    private void executeSplit(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand1 = player.getHand1(); // Always splitting from hand1

        if (!hand1.canSplit()) {
            handleInvalidAction(inputData, "Can only split pairs");
            return;
        }
        if (player.hasSplit()) {
            handleInvalidAction(inputData, "Cannot split more than once");
            return;
        }

        double originalBet = gameDataAccess.getBetAmount(inputData.getHandIndex());

        boolean balanceOk;
        if (balanceUpdater != null) balanceOk = balanceUpdater.deductBalance(inputData.getPlayerId(), originalBet, "SPLIT_BET");
        else {
            balanceOk = player.getBalance() >= originalBet;
            if (balanceOk) player.adjustBalance(-originalBet);
        }

        if (!balanceOk) {
            handleInvalidAction(inputData, "Insufficient balance to split");
            return;
        }

        // --- CORE SPLIT LOGIC ADAPTED FOR hand1/hand2 ---

        // 1. Activate split in Player entity (creates hand2)
        player.split();
        Hand hand2 = player.getHand2();

        // 2. Move card: Remove 2nd card from hand1, add to hand2
        List<Card> cards = hand1.getCards();
        Card splitCard = cards.remove(1); // Remove index 1
        hand2.addCard(splitCard);

        // 3. Draw new cards for both
        Card newCard1 = gameDataAccess.drawCard();
        Card newCard2 = gameDataAccess.drawCard();
        hand1.addCard(newCard1);
        hand2.addCard(newCard2);

        String message = String.format("Split! Hand 1: %d. Hand 2: %d",
                hand1.getTotalPoints(), hand2.getTotalPoints());

        outputBoundary.present(new PlayerActionOutputData(
                true, message, hand1.getTotalPoints(), false, false,
                getAvailableActions(hand1, player, 0)
        ));
    }

    private void executeInsurance(PlayerActionInputData inputData) {
        // (Insurance logic remains largely same, assuming Dealer works)
        Dealer dealer = gameDataAccess.getDealer();
        List<Card> dealerCards = dealer.getHand().getCards();
        if (dealerCards.isEmpty() || !dealerCards.get(0).getRank().equalsIgnoreCase("ACE")) {
            handleInvalidAction(inputData, "Insurance only available when dealer shows Ace");
            return;
        }

        double originalBet = gameDataAccess.getBetAmount(inputData.getHandIndex());
        double insuranceBet = originalBet / 2.0;
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());

        boolean balanceOk;
        if (balanceUpdater != null) balanceOk = balanceUpdater.deductBalance(inputData.getPlayerId(), insuranceBet, "INSURANCE");
        else {
            balanceOk = player.getBalance() >= insuranceBet;
            if (balanceOk) player.adjustBalance(-insuranceBet);
        }

        if (!balanceOk) {
            handleInvalidAction(inputData, "Insufficient balance");
            return;
        }

        boolean dealerBlackjack = dealer.isBlackJack();
        String message;
        if (dealerBlackjack) {
            double payout = insuranceBet * 3.0;
            if (balanceUpdater != null) balanceUpdater.addBalance(inputData.getPlayerId(), payout, "INSURANCE_WIN");
            else player.adjustBalance(payout);
            message = "Insurance wins! Dealer has Blackjack.";
            gameDataAccess.setGameState("ROUND_COMPLETE");
        } else {
            message = "Insurance loses. Dealer no Blackjack.";
        }

        Hand currentHand = getHandByIndex(player, inputData.getHandIndex());
        outputBoundary.present(new PlayerActionOutputData(
                true, message, currentHand.getTotalPoints(), false, false,
                dealerBlackjack ? new ArrayList<>() : getAvailableActions(currentHand, player, inputData.getHandIndex())
        ));
    }

    private List<String> getAvailableActions(Hand hand, Player player, int handIndex) {
        List<String> actions = new ArrayList<>();
        if (hand.isBust() || hand.getTotalPoints() == 21) return actions;
        actions.add("HIT");
        actions.add("STAND");
        if (hand.getCards().size() == 2) {
            double bet = gameDataAccess.getBetAmount(handIndex);
            if (player.getBalance() >= bet) actions.add("DOUBLE");
            if (hand.canSplit() && player.getBalance() >= bet && !player.hasSplit()) actions.add("SPLIT");
        }
        return actions;
    }

    private int findNextIncompleteHand(Player player) {
        // Adapted to check specific indices 0 and 1
        if (!gameDataAccess.isHandComplete(0)) return 0;
        if (player.hasSplit() && !gameDataAccess.isHandComplete(1)) return 1;
        return -1;
    }

    private String determineHandResult(Hand playerHand, Dealer dealer) {
        int pScore = playerHand.getTotalPoints();
        int dScore = dealer.GetDealerScore();
        if (playerHand.isBust()) return "LOSE";
        if (dealer.isBust()) return "WIN";
        if (playerHand.isBlackjack() && !dealer.isBlackJack()) return "BLACKJACK";
        if (pScore > dScore) return "WIN";
        if (pScore < dScore) return "LOSE";
        return "PUSH";
    }

    private double calculatePayout(Hand playerHand, Dealer dealer, double bet) {
        String result = determineHandResult(playerHand, dealer);
        if (result.equals("WIN")) return bet * 2.0;
        if (result.equals("BLACKJACK")) return bet * 2.5;
        if (result.equals("PUSH")) return bet;
        return 0.0;
    }

    private String determineOverallResult(Player player, Dealer dealer) {
        int wins = 0, losses = 0;
        for (Hand h : getPlayerHandsList(player)) {
            String res = determineHandResult(h, dealer);
            if (res.equals("WIN") || res.equals("BLACKJACK")) wins++;
            else if (res.equals("LOSE")) losses++;
        }
        if (wins > losses) return "WIN";
        if (losses > wins) return "LOSE";
        return "PUSH";
    }

    private void handleInvalidAction(PlayerActionInputData inputData, String reason) {
        outputBoundary.present(new PlayerActionOutputData(false, "Invalid: " + reason, 0, false, false, new ArrayList<>()));
    }

    private void handleError(PlayerActionInputData inputData, Exception e) {
        outputBoundary.present(new PlayerActionOutputData(false, "Error: " + e.getMessage(), 0, false, false, new ArrayList<>()));
    }
}