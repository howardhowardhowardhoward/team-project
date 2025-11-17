package usecase;

import entities.*;
import usecase.dealeraction.DealerController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interactor for handling all player actions in Blackjack.
 * Implements Use Case #3: Player Action (Hit/Stand/Double/Split/Insurance)
 *
 * Responsible for User Stories: #2, #3, #5, #6, #7, #9, #13, #14
 *
 * @author Wentai Zhang (eurekoko)
 */
public class PlayerActionInteractor implements PlayerActionInputBoundary {

    private final PlayerActionOutputBoundary outputBoundary;
    private final GameDataAccess gameDataAccess;
    private final BalanceUpdater balanceUpdater;  // Eden's UpdateBalance
    private final DealerController dealerController;  // Howard's DealerAction (optional)

    /**
     * Constructor with dependency injection
     *
     * @param outputBoundary for presenting results to UI
     * @param gameDataAccess for accessing game state
     * @param balanceUpdater Eden's balance update interface (can be null, will use Player.adjustBalance)
     * @param dealerController Howard's dealer controller (can be null, will use Dealer.play())
     */
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

    /**
     * Simplified constructor for independent testing
     * Uses direct entity methods instead of teammate interfaces
     */
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
                case "HIT":
                    executeHit(inputData);
                    break;
                case "STAND":
                    executeStand(inputData);
                    break;
                case "DOUBLE":
                    executeDouble(inputData);
                    break;
                case "SPLIT":
                    executeSplit(inputData);
                    break;
                case "INSURANCE":
                    executeInsurance(inputData);
                    break;
                default:
                    handleInvalidAction(inputData, "Unknown action: " + action);
            }
        } catch (Exception e) {
            handleError(inputData, e);
        }
    }

    /**
     * HIT: Draw one additional card
     * User Story #2
     */
    private void executeHit(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand = player.getHand(inputData.getHandIndex());

        // Validation: Can't hit if already bust or at 21
        if (hand.isBust()) {
            handleInvalidAction(inputData, "Cannot hit - hand is already bust");
            return;
        }
        if (hand.getTotalPoints() == 21) {
            handleInvalidAction(inputData, "Cannot hit - hand is already 21");
            return;
        }

        // Draw a card from the deck
        Card newCard = gameDataAccess.drawCard();
        hand.addCard(newCard);

        // Calculate new state
        int newTotal = hand.getTotalPoints();
        boolean bust = hand.isBust();
        boolean blackjack = hand.isBlackjack();

        // Determine available actions after hit
        List<String> availableActions = new ArrayList<>();
        if (bust) {
            // Bust - no more actions, move to next hand or dealer
            gameDataAccess.markHandComplete(inputData.getHandIndex());
            if (gameDataAccess.allHandsComplete()) {
                // All hands complete, trigger dealer turn
                executeStand(inputData);
                return;
            } else {
                availableActions.add("NEXT_HAND");
            }
        } else if (newTotal == 21) {
            // Automatically stand at 21
            availableActions.add("STAND");
        } else {
            // Can continue playing
            availableActions.add("HIT");
            availableActions.add("STAND");
        }

        // Create output (User Story #13: real-time score update)
        String message = String.format("Drew %s. Total: %d", newCard.toString(), newTotal);
        if (bust) {
            message += " - BUST!";
        }

        PlayerActionOutputData output = new PlayerActionOutputData(
                true,
                message,
                newTotal,
                bust,
                blackjack,
                availableActions
        );

        outputBoundary.present(output);
    }

    /**
     * STAND: End player's turn
     * User Story #3
     */
    private void executeStand(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand currentHand = player.getHand(inputData.getHandIndex());

        // Mark this hand as complete
        gameDataAccess.markHandComplete(inputData.getHandIndex());

        // Check if there are more hands to play (from SPLIT)
        if (!gameDataAccess.allHandsComplete()) {
            // Move to next hand
            int nextHandIndex = findNextIncompleteHand(player);
            Hand nextHand = player.getHand(nextHandIndex);

            PlayerActionOutputData output = new PlayerActionOutputData(
                    true,
                    String.format("Hand %d stands at %d. Moving to hand %d...",
                            inputData.getHandIndex() + 1,
                            currentHand.getTotalPoints(),
                            nextHandIndex + 1),
                    currentHand.getTotalPoints(),
                    false,
                    false,
                    getAvailableActions(nextHand, player, nextHandIndex)
            );

            outputBoundary.present(output);
            return;
        }

        // All player hands complete - trigger dealer turn
        gameDataAccess.setGameState("DEALER_TURN");

        Dealer dealer = gameDataAccess.getDealer();

        // Execute dealer turn
        if (dealerController != null) {
            // Use Howard's implementation if available
            dealerController.executeDealerTurn();
        } else {
            // Fallback: use Dealer entity's play() method directly
            dealer.play();
        }

        // Determine winner for all hands
        int dealerScore = dealer.GetDealerScore();
        boolean dealerBust = dealer.isBust();

        // Calculate results for all hands
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append(String.format("Dealer: %d", dealerScore));
        if (dealerBust) {
            resultMessage.append(" (BUST)");
        }
        resultMessage.append("\n\nResults:\n");

        double totalPayout = 0.0;

        for (int i = 0; i < player.getHands().size(); i++) {
            Hand playerHand = player.getHand(i);
            double betAmount = gameDataAccess.getBetAmount(i);

            String handResult = determineHandResult(playerHand, dealer);
            double payout = calculatePayout(playerHand, dealer, betAmount);

            resultMessage.append(String.format("Hand %d (%d): %s",
                    i + 1,
                    playerHand.getTotalPoints(),
                    handResult));

            if (payout > 0) {
                resultMessage.append(String.format(" - Won $%.2f", payout));
                totalPayout += payout;
            }
            resultMessage.append("\n");
        }

        // Update balance
        if (totalPayout > 0) {
            if (balanceUpdater != null) {
                // Use Eden's implementation
                balanceUpdater.addBalance(inputData.getPlayerId(), totalPayout, "HAND_WIN");
            } else {
                // Fallback: use Player's adjustBalance
                player.adjustBalance(totalPayout);
            }
        }

        resultMessage.append(String.format("\nTotal payout: $%.2f", totalPayout));

        gameDataAccess.setGameState("COMPLETE");

        // Create final output
        PlayerActionOutputData output = new PlayerActionOutputData(
                true,
                resultMessage.toString(),
                currentHand.getTotalPoints(),
                false,
                false,
                Arrays.asList("NEW_ROUND", "QUIT"),
                true,  // game complete
                determineOverallResult(player, dealer),
                dealerScore
        );

        outputBoundary.present(output);
    }

    /**
     * DOUBLE: Double the bet, draw one card, then automatically stand
     * User Story #5
     */
    private void executeDouble(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand = player.getHand(inputData.getHandIndex());

        // Validation: Can only double on first two cards
        if (hand.getCards().size() != 2) {
            handleInvalidAction(inputData, "Can only double down on first two cards");
            return;
        }

        // Check if player has sufficient balance
        double originalBet = gameDataAccess.getBetAmount(inputData.getHandIndex());
        double doubleAmount = originalBet;

        boolean balanceOk;
        if (balanceUpdater != null) {
            // Use Eden's implementation
            balanceOk = balanceUpdater.deductBalance(
                    inputData.getPlayerId(),
                    doubleAmount,
                    "DOUBLE_DOWN"
            );
        } else {
            // Fallback: check and adjust balance directly
            balanceOk = player.getBalance() >= doubleAmount;
            if (balanceOk) {
                player.adjustBalance(-doubleAmount);
            }
        }

        if (!balanceOk) {
            handleInvalidAction(inputData, "Insufficient balance to double down");
            return;
        }

        // Draw exactly one card
        Card newCard = gameDataAccess.drawCard();
        hand.addCard(newCard);

        int newTotal = hand.getTotalPoints();
        boolean bust = hand.isBust();

        // Automatically mark hand as complete (can't act further)
        gameDataAccess.markHandComplete(inputData.getHandIndex());

        // Create message
        String message = String.format("Doubled down! Drew %s. Total: %d",
                newCard.toString(), newTotal);
        if (bust) {
            message += " - BUST!";
        }

        // Check if need to continue to next hand or dealer
        if (gameDataAccess.allHandsComplete()) {
            // Automatically trigger stand to finish the game
            executeStand(inputData);
        } else {
            PlayerActionOutputData output = new PlayerActionOutputData(
                    true,
                    message,
                    newTotal,
                    bust,
                    false,
                    Arrays.asList("NEXT_HAND")
            );
            outputBoundary.present(output);
        }
    }

    /**
     * SPLIT: Split a pair into two separate hands
     * User Story #6
     */
    private void executeSplit(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand = player.getHand(inputData.getHandIndex());

        // Validation: Can only split pairs
        if (!hand.canSplit()) {
            handleInvalidAction(inputData, "Can only split pairs (two cards of same rank)");
            return;
        }

        // Check if player has sufficient balance for second bet
        double originalBet = gameDataAccess.getBetAmount(inputData.getHandIndex());

        boolean balanceOk;
        if (balanceUpdater != null) {
            // Use Eden's implementation
            balanceOk = balanceUpdater.deductBalance(
                    inputData.getPlayerId(),
                    originalBet,
                    "SPLIT_BET"
            );
        } else {
            // Fallback
            balanceOk = player.getBalance() >= originalBet;
            if (balanceOk) {
                player.adjustBalance(-originalBet);
            }
        }

        if (!balanceOk) {
            handleInvalidAction(inputData, "Insufficient balance to split");
            return;
        }

        // Split the hand
        List<Card> cards = hand.getCards();
        Card firstCard = cards.get(0);
        Card secondCard = cards.get(1);

        // Clear original hand and add first card back
        hand.clear();
        hand.addCard(firstCard);

        // Create new hand with second card
        Hand newHand = new Hand();
        newHand.addCard(secondCard);
        player.addHand(newHand);

        // Draw one card for each hand
        Card card1 = gameDataAccess.drawCard();
        Card card2 = gameDataAccess.drawCard();
        hand.addCard(card1);
        newHand.addCard(card2);

        // Create output
        String message = String.format("Split! Hand 1: %s + %s = %d. Hand 2: %s + %s = %d",
                firstCard.toString(), card1.toString(), hand.getTotalPoints(),
                secondCard.toString(), card2.toString(), newHand.getTotalPoints());

        PlayerActionOutputData output = new PlayerActionOutputData(
                true,
                message,
                hand.getTotalPoints(),
                false,
                false,
                getAvailableActions(hand, player, inputData.getHandIndex())
        );

        outputBoundary.present(output);
    }

    /**
     * INSURANCE: Place side bet when dealer shows Ace
     * User Story #7
     */
    private void executeInsurance(PlayerActionInputData inputData) {
        Dealer dealer = gameDataAccess.getDealer();

        // Validation: Can only take insurance when dealer shows Ace
        List<Card> dealerCards = dealer.getHand().getCards();
        if (dealerCards.isEmpty() || !dealerCards.get(0).getRank().equalsIgnoreCase("ACE")) {
            handleInvalidAction(inputData, "Insurance only available when dealer shows Ace");
            return;
        }

        // Insurance bet is half of original bet
        double originalBet = gameDataAccess.getBetAmount(inputData.getHandIndex());
        double insuranceBet = originalBet / 2.0;

        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());

        // Check balance
        boolean balanceOk;
        if (balanceUpdater != null) {
            balanceOk = balanceUpdater.deductBalance(
                    inputData.getPlayerId(),
                    insuranceBet,
                    "INSURANCE"
            );
        } else {
            balanceOk = player.getBalance() >= insuranceBet;
            if (balanceOk) {
                player.adjustBalance(-insuranceBet);
            }
        }

        if (!balanceOk) {
            handleInvalidAction(inputData, "Insufficient balance for insurance");
            return;
        }

        // Check if dealer has blackjack
        boolean dealerBlackjack = dealer.isBlackJack();
        String message;

        if (dealerBlackjack) {
            // Insurance wins 2:1
            double payout = insuranceBet * 2.0;
            if (balanceUpdater != null) {
                balanceUpdater.addBalance(inputData.getPlayerId(), payout, "INSURANCE_WIN");
            } else {
                player.adjustBalance(payout);
            }
            message = String.format("Insurance wins! Dealer has Blackjack. Won $%.2f", payout);
        } else {
            message = String.format("Insurance loses. Dealer does not have Blackjack. Lost $%.2f", insuranceBet);
        }

        PlayerActionOutputData output = new PlayerActionOutputData(
                true,
                message,
                player.getHand(inputData.getHandIndex()).getTotalPoints(),
                false,
                false,
                getAvailableActions(player.getHand(inputData.getHandIndex()), player, inputData.getHandIndex())
        );

        outputBoundary.present(output);
    }

    // ==================== Helper Methods ====================

    /**
     * Determine available actions based on current hand state
     * User Story #14: Enable buttons only during appropriate stage
     */
    private List<String> getAvailableActions(Hand hand, Player player, int handIndex) {
        List<String> actions = new ArrayList<>();

        if (hand.isBust() || hand.getTotalPoints() == 21) {
            // No actions available if bust or at 21
            return actions;
        }

        // Basic actions always available
        actions.add("HIT");
        actions.add("STAND");

        // Double only available on first two cards with sufficient balance
        if (hand.getCards().size() == 2) {
            double betAmount = gameDataAccess.getBetAmount(handIndex);
            if (player.getBalance() >= betAmount) {
                actions.add("DOUBLE");
            }

            // Split only on pairs
            if (hand.canSplit() && player.getBalance() >= betAmount) {
                actions.add("SPLIT");
            }
        }

        // Insurance only when dealer shows Ace (checked in executeInsurance)
        Dealer dealer = gameDataAccess.getDealer();
        if (!dealer.getHand().getCards().isEmpty() &&
                dealer.getHand().getCards().get(0).getRank().equalsIgnoreCase("ACE")) {
            double betAmount = gameDataAccess.getBetAmount(handIndex);
            if (player.getBalance() >= betAmount / 2.0) {
                actions.add("INSURANCE");
            }
        }

        return actions;
    }

    /**
     * Find the next hand that hasn't been completed yet
     */
    private int findNextIncompleteHand(Player player) {
        for (int i = 0; i < player.getHands().size(); i++) {
            if (!gameDataAccess.isHandComplete(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Determine result for a single hand
     */
    private String determineHandResult(Hand playerHand, Dealer dealer) {
        int playerScore = playerHand.getTotalPoints();
        int dealerScore = dealer.GetDealerScore();

        if (playerHand.isBust()) {
            return "LOSE (Bust)";
        } else if (dealer.isBust()) {
            return "WIN (Dealer Bust)";
        } else if (playerHand.isBlackjack() && !dealer.isBlackJack()) {
            return "BLACKJACK!";  // User Story #9
        } else if (playerScore > dealerScore) {
            return "WIN";
        } else if (playerScore < dealerScore) {
            return "LOSE";
        } else {
            return "PUSH (Tie)";
        }
    }

    /**
     * Calculate payout for a hand
     */
    private double calculatePayout(Hand playerHand, Dealer dealer, double betAmount) {
        if (playerHand.isBust()) {
            return 0.0;  // Lose bet
        } else if (dealer.isBust()) {
            return betAmount * 2.0;  // Win even money
        } else if (playerHand.isBlackjack() && !dealer.isBlackJack()) {
            return betAmount * 2.5;  // Blackjack pays 3:2
        } else {
            int playerScore = playerHand.getTotalPoints();
            int dealerScore = dealer.GetDealerScore();

            if (playerScore > dealerScore) {
                return betAmount * 2.0;  // Win even money
            } else if (playerScore == dealerScore) {
                return betAmount;  // Push - return bet
            } else {
                return 0.0;  // Lose
            }
        }
    }

    /**
     * Determine overall result
     */
    private String determineOverallResult(Player player, Dealer dealer) {
        int wins = 0;
        int losses = 0;
        int pushes = 0;

        for (Hand hand : player.getHands()) {
            String result = determineHandResult(hand, dealer);
            if (result.contains("WIN") || result.contains("BLACKJACK")) {
                wins++;
            } else if (result.contains("LOSE")) {
                losses++;
            } else {
                pushes++;
            }
        }

        if (wins > losses) {
            return "WIN";
        } else if (losses > wins) {
            return "LOSE";
        } else {
            return "PUSH";
        }
    }

    /**
     * Handle invalid action
     */
    private void handleInvalidAction(PlayerActionInputData inputData, String reason) {
        PlayerActionOutputData output = new PlayerActionOutputData(
                false,
                "Invalid action: " + reason,
                0,
                false,
                false,
                new ArrayList<>()
        );
        outputBoundary.present(output);
    }

    /**
     * Handle errors
     */
    private void handleError(PlayerActionInputData inputData, Exception e) {
        PlayerActionOutputData output = new PlayerActionOutputData(
                false,
                "Error executing action: " + e.getMessage(),
                0,
                false,
                false,
                new ArrayList<>()
        );
        outputBoundary.present(output);
    }
}