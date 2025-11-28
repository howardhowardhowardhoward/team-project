package frameworks_and_drivers;

import entities.*;
import usecase.DeckProvider; // Fixed Import
import usecase.PlayerActions.BalanceUpdater;
import usecase.PlayerActions.GameDataAccess;
import usecase.updatebalance.UpdateBalanceDataAccessInterface;
import usecase.ExitRestartGame.ExitRestartGameDataAccess;
import usecase.ExitRestartGame.GameStateManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryGameDataAccess implements 
        GameDataAccess, 
        ExitRestartGameDataAccess, 
        UpdateBalanceDataAccessInterface,
        GameStateManager,
        BalanceUpdater {

    private Player player;
    private final Dealer dealer;
    private final DeckProvider deck; // Use Interface
    private final Map<Integer, Double> betAmounts = new HashMap<>(); 
    private final Map<Integer, Boolean> handCompletion = new HashMap<>();
    private String gameState = "WAITING";
    private boolean gameExited = false;
    
    private static final String PLAYER_ID = "mainPlayer"; 
    private static final double INITIAL_BALANCE = 1000.0;
    private final GameRules gameRules;

    public InMemoryGameDataAccess(DeckProvider deckProvider, GameRules gameRules) {
        this.deck = deckProvider;
        this.dealer = new Dealer();
        this.player = new Player(INITIAL_BALANCE);
        this.gameRules = gameRules;
    }

    // ... Implementation of interfaces ...
    
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
    public void markHandComplete(int handIndex) {
        handCompletion.put(handIndex, true);
    }

    @Override
    public boolean isHandComplete(int handIndex) {
        return handCompletion.getOrDefault(handIndex, false);
    }

    @Override
    public boolean allHandsComplete() {
        return isHandComplete(0) && (!player.hasSplit() || isHandComplete(1));
    }

    @Override
    public String getGameState() { return gameState; }

    @Override
    public void setGameState(String state) { this.gameState = state; }
    
    @Override
    public void setBetAmount(int handIndex, double amount) {
        betAmounts.put(handIndex, amount);
    }

    @Override
    public void addHandBet(int handIndex, double amount) {
        double currentBet = betAmounts.getOrDefault(handIndex, 0.0);
        betAmounts.put(handIndex, currentBet + amount);
    }

    @Override
    public String getCurrentPlayerId() { return PLAYER_ID; }

    @Override
    public void savePlayer(Player player) { this.player = player; }

    @Override
    public boolean isGameActive() { return !gameExited; }

    @Override
    public void save(Player player) { savePlayer(player); }

    @Override
    public Player get(String username) { return getPlayer(username); }
    
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