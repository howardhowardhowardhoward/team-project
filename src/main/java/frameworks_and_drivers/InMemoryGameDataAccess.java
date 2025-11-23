package frameworks_and_drivers;

import entities.*;
import usecase.PlayerActions.BalanceUpdater;
import usecase.PlayerActions.GameDataAccess;
import usecase.updatebalance.UpdateBalanceDataAccessInterface;

// --- Critical Fix: Import correct ExitRestartGame interfaces ---
import usecase.ExitRestartGame.ExitRestartGameDataAccess;
import usecase.ExitRestartGame.GameStateManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central, in-memory implementation of all Data Access and State Manager interfaces.
 * This class ties the core use cases together for a runnable prototype.
 */
public class InMemoryGameDataAccess implements 
        GameDataAccess, 
        ExitRestartGameDataAccess, 
        UpdateBalanceDataAccessInterface,
        GameStateManager,
        BalanceUpdater {

    private Player player;
    private final Dealer dealer;
    private final Deck deck;
    private final Map<Integer, Double> betAmounts = new HashMap<>(); 
    private final Map<Integer, Boolean> handCompletion = new HashMap<>();
    private GamePhase gamePhase;
    private boolean gameExited = false;
    
    private static final String PLAYER_ID = "mainPlayer"; 
    private static final double INITIAL_BALANCE = 1000.0;
    private final GameRules gameRules;

    public InMemoryGameDataAccess(DeckProvider deckProvider, GameRules gameRules) {
        this.deck = new Deck(deckProvider);
        this.dealer = new Dealer();
        this.player = new Player(INITIAL_BALANCE);
        this.gamePhase = GamePhase.WAITING_FOR_BET;
        this.gameRules = gameRules;
    }

    // --- GameDataAccess / Core Game Logic ---
    @Override
    public Player getPlayer(String playerId) { return player; }

    @Override
    public Dealer getDealer() { return dealer; }

    @Override
    public Card drawCard() { 
        return deck.drawCard(); 
    }

    @Override
    public double getBetAmount(int handIndex) {
        return betAmounts.getOrDefault(handIndex, 0.0);
    }

    @Override
    public void markHandComplete(int handIndex) {
        handCompletion.put(handIndex, true);
        if (allHandsComplete()) {
            setGameState(GamePhase.DEALER_TURN.name());
        }
    }

    @Override
    public boolean isHandComplete(int handIndex) {
        return handCompletion.getOrDefault(handIndex, false);
    }

    @Override
    public boolean allHandsComplete() {
        List<Hand> hands = player.getHands();
        for (int i = 0; i < hands.size(); i++) {
            if (!isHandComplete(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getGameState() { return gamePhase.name(); }

    @Override
    public void setGameState(String state) {
        try {
            this.gamePhase = GamePhase.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid GamePhase state set: " + state);
        }
    }
    
    @Override
    public void setBetAmount(int handIndex, double amount) {
        betAmounts.put(handIndex, amount);
    }

    @Override
    public void addHandBet(int handIndex, double amount) {
        double currentBet = betAmounts.getOrDefault(handIndex, 0.0);
        betAmounts.put(handIndex, currentBet + amount);
    }

    // --- ExitRestartGameDataAccess ---
    @Override
    public String getCurrentPlayerId() { return PLAYER_ID; }

    @Override
    public void savePlayer(Player player) {
        this.player = player; 
    }

    @Override
    public boolean isGameActive() {
        return this.gamePhase != GamePhase.ROUND_COMPLETE && !gameExited;
    }

    // --- UpdateBalanceDataAccessInterface ---
    @Override
    public void save(Player player) { savePlayer(player); }

    @Override
    public Player get(String username) { return getPlayer(username); }
    
    // --- GameStateManager ---
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
        this.gamePhase = GamePhase.WAITING_FOR_BET;
    }

    @Override
    public String getGameStatus() { return getGameState(); }

    // --- BalanceUpdater ---
    @Override
    public boolean deductBalance(String playerId, double amount, String reason) {
        if (amount <= 0) return true;
        if (player.getBalance() >= amount) {
            player.adjustBalance(-amount);
            savePlayer(player);
            return true;
        }
        return false;
    }

    @Override
    public double addBalance(String playerId, double amount, String reason) {
        if (amount > 0) {
            player.adjustBalance(amount);
            savePlayer(player);
        }
        return player.getBalance();
    }

    @Override
    public double getBalance(String playerId) {
        return player.getBalance();
    }
    
    public GameRules getGameRules() {
        return gameRules;
    }
}