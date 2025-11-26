package frameworks_and_drivers;

import entities.*;
import usecase.DeckProvider;
import usecase.PlayerActions.BalanceUpdater;
import usecase.PlayerActions.GameDataAccess;
import usecase.updatebalance.UpdateBalanceDataAccessInterface;
import usecase.ExitRestartGame.ExitRestartGameDataAccess;
import usecase.ExitRestartGame.GameStateManager;

import java.util.HashMap;
import java.util.Map;

/**
 * A unified in-memory data access implementation.
 * It implements interfaces for all use cases (PlayerAction, Exit/Restart, UpdateBalance)
 * to ensure they all share the same game state and player data.
 */
public class InMemoryGameDataAccess implements 
        GameDataAccess, 
        ExitRestartGameDataAccess, 
        UpdateBalanceDataAccessInterface,
        GameStateManager,
        BalanceUpdater {

    private Player player;
    private final Dealer dealer;
    private final DeckProvider deck;
    
    // Tracks the bet amount for each hand (0 for main hand, 1 for split hand)
    private final Map<Integer, Double> betAmounts = new HashMap<>(); 
    // Tracks whether a hand is finished
    private final Map<Integer, Boolean> handCompletion = new HashMap<>();
    
    private String gameState = "WAITING";
    private boolean gameExited = false;
    
    private static final String PLAYER_ID = "mainPlayer"; 

    public InMemoryGameDataAccess(DeckProvider deckProvider) {
        this.deck = deckProvider;
        this.dealer = new Dealer();
        // Initialize player with default balance of 1000
        this.player = new Player(1000.0);
    }

    // --- GameDataAccess Implementation (For PlayerActions) ---
    @Override
    public Player getPlayer(String playerId) { return player; }

    @Override
    public Dealer getDealer() { return dealer; }

    @Override
    public Card drawCard() { return deck.drawCard(); }

    @Override
    public double getBetAmount(int handIndex) {
        return betAmounts.getOrDefault(handIndex, 0.0);
    }

    @Override
    public void setBetAmount(int handIndex, double amount) {
        betAmounts.put(handIndex, amount);
    }

    @Override
    public void addHandBet(int handIndex, double amount) {
        double current = getBetAmount(handIndex);
        betAmounts.put(handIndex, current + amount);
    }

    @Override
    public void markHandComplete(int handIndex) {
        handCompletion.put(handIndex, true);
    }

    @Override
    public boolean isHandComplete(int handIndex) {
        return handCompletion.getOrDefault(handIndex, false);
    }

    @Override
    public boolean allHandsComplete() {
        // If split, check both hands; otherwise just check main hand
        return isHandComplete(0) && (!player.hasSplit() || isHandComplete(1));
    }

    @Override
    public String getGameState() { return gameState; }

    @Override
    public void setGameState(String state) { 
        this.gameState = state;
        // Reset round state if a new round begins
        if (state.equals("DEALING") || state.equals("WAITING_FOR_BET")) {
            betAmounts.clear();
            handCompletion.clear();
        }
    }

    // --- ExitRestartGameDataAccess Implementation ---
    @Override
    public String getCurrentPlayerId() { return PLAYER_ID; }

    @Override
    public void savePlayer(Player player) { this.player = player; }

    @Override
    public boolean isGameActive() { return !gameExited; }

    // --- UpdateBalanceDataAccessInterface Implementation ---
    @Override
    public Player get(String username) { return getPlayer(username); } 

    @Override
    public void save(Player player) { savePlayer(player); } 

    // --- GameStateManager Implementation ---
    @Override
    public void setGameExited(boolean exited) { this.gameExited = exited; }

    @Override
    public boolean isGameExited() { return gameExited; }

    @Override
    public void resetGameState() {
        player.clearHands();
        dealer.getHand().clear();
        betAmounts.clear();
        handCompletion.clear();
        gameState = "WAITING";
    }

    @Override
    public String getGameStatus() { return gameState; }

    // --- BalanceUpdater Implementation ---
    @Override
    public boolean deductBalance(String playerId, double amount, String reason) {
        if (amount <= 0) return true;
        if (player.getBalance() >= amount) {
            player.adjustBalance(-amount);
            return true;
        }
        return false;
    }

    @Override
    public double addBalance(String playerId, double amount, String reason) {
        if (amount > 0) {
            player.adjustBalance(amount);
        }
        return player.getBalance();
    }

    @Override
    public double getBalance(String playerId) { return player.getBalance(); }
}