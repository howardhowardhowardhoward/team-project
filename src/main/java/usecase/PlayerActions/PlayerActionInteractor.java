package usecase.PlayerActions;

import entities.*;
import usecase.dealeraction.DealerController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    // Simplified constructor for testing
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
            e.printStackTrace(); // Print stack trace for debugging
            handleError(inputData, e);
        }
    }

    // --- HELPER: Correctly access hand by index ---
    private Hand getHandByIndex(Player player, int index) {
        List<Hand> hands = player.getHands();
        if (index >= 0 && index < hands.size()) {
            return hands.get(index);
        }
        throw new IllegalArgumentException("Invalid hand index: " + index);
    }

    private List<Hand> getPlayerHandsList(Player player) {
        return player.getHands();
    }

    private void executeHit(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand = getHandByIndex(player, inputData.getHandIndex());

        if (hand.isBust() || hand.getTotalPoints() == 21) {
            handleInvalidAction(inputData, "Cannot hit/Action already complete for this hand.");
            return;
        }

        Card newCard = gameDataAccess.drawCard();
        hand.addCard(newCard);
        int newTotal = hand.getTotalPoints();
        boolean bust = hand.isBust();

        List<String> availableActions = new ArrayList<>();
        if (bust || newTotal == 21) {
            gameDataAccess.markHandComplete(inputData.getHandIndex());
            if (gameDataAccess.allHandsComplete()) {
                executeDealerTurnAndSettle(inputData, hand);
                return;
            } else {
                availableActions.add("NEXT_HAND");
            }
        } else {
            availableActions.add("HIT");
            availableActions.add("STAND");
        }

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
                Hand nextHand = getHandByIndex(player, nextHandIndex);
                // Fix parameter matching: getAvailableActions accepts 3 arguments
                outputBoundary.present(new PlayerActionOutputData(true, "Stand successful. Moving to next hand...",
                        currentHand.getTotalPoints(), false, false, getAvailableActions(nextHand, player, nextHandIndex)));
                return;
            }
        }
        
        executeDealerTurnAndSettle(inputData, currentHand);
    }
    
    private void executeDealerTurnAndSettle(PlayerActionInputData inputData, Hand lastActedHand) {
        gameDataAccess.setGameState("DEALER_TURN");
        Dealer dealer = gameDataAccess.getDealer();
        
        if (dealerController != null) {
            dealerController.executeDealerTurn();
        } else {
            while (dealer.GetDealerScore() < 17) {
                 dealer.draw(gameDataAccess.drawCard());
            }
        }

        int dealerScore = dealer.GetDealerScore();
        boolean dealerBust = dealer.isBust();
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append(String.format("Dealer total: %d (%s)\n-- Round Results --\n", dealerScore, dealerBust ? "BUST" : "STAND"));

        double totalPayout = 0.0;
        List<Hand> allHands = getPlayerHandsList(gameDataAccess.getPlayer(inputData.getPlayerId()));
        
        for (int i = 0; i < allHands.size(); i++) {
            Hand h = allHands.get(i);
            String result = determineHandResult(h, dealer);
            double betAmount = gameDataAccess.getBetAmount(i);
            double payout = calculatePayout(h, dealer, betAmount);
            
            resultMessage.append(String.format("Hand %d (%d): %s. Payout: %.2f\n", 
                                                i+1, h.getTotalPoints(), result, payout));
            totalPayout += payout;
        }

        if (totalPayout > 0) {
            if (balanceUpdater != null) {
                balanceUpdater.addBalance(inputData.getPlayerId(), totalPayout, "ROUND_WINNINGS");
            } else {
                gameDataAccess.getPlayer(inputData.getPlayerId()).adjustBalance(totalPayout);
            }
        }

        outputBoundary.present(new PlayerActionOutputData(true, resultMessage.toString(),
                lastActedHand.getTotalPoints(), lastActedHand.isBust(), lastActedHand.isBlackjack(), 
                Arrays.asList("NEW_ROUND"), true, "ROUND_COMPLETE", dealerScore));
    }

    private void executeDouble(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand = getHandByIndex(player, inputData.getHandIndex());
        double bet = gameDataAccess.getBetAmount(inputData.getHandIndex());

        if (hand.getCards().size() != 2) {
             handleInvalidAction(inputData, "Can only double down on the first two cards.");
             return;
        }
        
        boolean deducted = false;
        if (balanceUpdater != null) {
            deducted = balanceUpdater.deductBalance(inputData.getPlayerId(), bet, "DOUBLE_DOWN");
        } else {
            if (player.getBalance() >= bet) {
                player.adjustBalance(-bet);
                deducted = true;
            }
        }

        if (deducted) {
            gameDataAccess.addHandBet(inputData.getHandIndex(), bet); 
            
            Card newCard = gameDataAccess.drawCard();
            hand.addCard(newCard);
            gameDataAccess.markHandComplete(inputData.getHandIndex());
            
            String message = String.format("Doubled down. Drew %s. Final Total: %d", newCard.toString(), hand.getTotalPoints());
            if (hand.isBust()) message += " - BUST!";
            
            if (gameDataAccess.allHandsComplete()) {
                executeDealerTurnAndSettle(inputData, hand);
            } else {
                outputBoundary.present(new PlayerActionOutputData(true, message, hand.getTotalPoints(), hand.isBust(), false, Arrays.asList("NEXT_HAND")));
            }
        } else {
            handleInvalidAction(inputData, "Insufficient balance for double down bet.");
        }
    }

    private void executeSplit(PlayerActionInputData inputData) {
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        Hand hand1 = player.getHand1();
        double bet = gameDataAccess.getBetAmount(inputData.getHandIndex());

        if (inputData.getHandIndex() != 0) {
            handleInvalidAction(inputData, "Can only split on the first hand (index 0).");
            return;
        }
        if (!hand1.canSplit()) {
             handleInvalidAction(inputData, "Cards must be of the same rank to split.");
             return;
        }
        if (player.hasSplit()) {
            handleInvalidAction(inputData, "Cannot split more than once.");
            return;
        }
        
        boolean deducted = false;
        if (balanceUpdater != null) {
            deducted = balanceUpdater.deductBalance(inputData.getPlayerId(), bet, "SPLIT_BET");
        } else {
            if (player.getBalance() >= bet) {
                player.adjustBalance(-bet);
                deducted = true;
            }
        }

        if (deducted) {
            player.split(); 
            gameDataAccess.addHandBet(1, bet); 
            
            List<Card> cards = hand1.getCards();
            Card firstCard = cards.get(0);
            Card secondCard = cards.get(1);
            hand1.clear();
            hand1.addCard(firstCard);
            player.getHand2().addCard(secondCard);
            
            hand1.addCard(gameDataAccess.drawCard());
            player.getHand2().addCard(gameDataAccess.drawCard());
            
            outputBoundary.present(new PlayerActionOutputData(true, "Split successful! Starting with Hand 1...", hand1.getTotalPoints(), false, false, getAvailableActions(hand1, player, 0)));
        } else {
            handleInvalidAction(inputData, "Insufficient balance for split bet.");
        }
    }

    private void executeInsurance(PlayerActionInputData inputData) {
        // Fix: use getHandByIndex instead of player.getHand() to avoid parameter error
        Hand hand = getHandByIndex(gameDataAccess.getPlayer(inputData.getPlayerId()), inputData.getHandIndex());
        Player player = gameDataAccess.getPlayer(inputData.getPlayerId());
        
        outputBoundary.present(new PlayerActionOutputData(true, "Insurance side bet registered (Functionality TBD)", 
                                                         0, false, false, getAvailableActions(hand, player, inputData.getHandIndex())));
    }

    private int findNextIncompleteHand(Player player) {
        if (!gameDataAccess.isHandComplete(0)) return 0;
        if (player.hasSplit() && !gameDataAccess.isHandComplete(1)) return 1;
        return -1;
    }
    
    // Ensure this method takes 3 arguments to match usage
    private List<String> getAvailableActions(Hand h, Player p, int i) { 
        List<String> actions = new ArrayList<>(Arrays.asList("HIT", "STAND"));
        if (h.getCards().size() == 2) {
            actions.add("DOUBLE");
            if (h.canSplit() && !p.hasSplit()) {
                actions.add("SPLIT");
            }
        }
        return actions; 
    }
    
    private String determineHandResult(Hand playerHand, Dealer dealer) { 
        int playerScore = playerHand.getTotalPoints();
        int dealerScore = dealer.GetDealerScore();

        if (playerHand.isBust()) return "BUST (LOST)";
        if (dealer.isBust()) return "WIN (Dealer Bust)";
        // Fixed method: Removed undefined dealerHand
        if (playerHand.isBlackjack() && !dealer.isBlackJack()) return "BLACKJACK (WIN)"; 
        if (playerScore > dealerScore) return "WIN";
        if (playerScore < dealerScore) return "LOSE";
        return "PUSH (TIE)";
    }
    
    private double calculatePayout(Hand playerHand, Dealer dealer, double betAmount) { 
        String result = determineHandResult(playerHand, dealer);
        switch (result) {
            case "BLACKJACK (WIN)": return betAmount * 2.5; 
            case "WIN":
            case "WIN (Dealer Bust)": return betAmount * 2.0;
            case "PUSH (TIE)": return betAmount;
            default: return 0.0;
        }
    }
    
    private void handleInvalidAction(PlayerActionInputData i, String r) { 
        outputBoundary.present(new PlayerActionOutputData(false, r, 0, false, false, null)); 
    }
    
    private void handleError(PlayerActionInputData i, Exception e) { 
        outputBoundary.present(new PlayerActionOutputData(false, "An error occurred: " + e.getMessage(), 0, false, false, null)); 
    }
}