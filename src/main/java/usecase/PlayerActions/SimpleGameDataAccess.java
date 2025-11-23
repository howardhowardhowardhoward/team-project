package usecase.PlayerActions;

import entities.*;
import usecase.DeckProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory implementation of GameDataAccess for testing.
 * TEMPORARY IMPLEMENTATION - To be replaced when Gopal implements proper Game management
 * This allows you to test your PlayerAction code independently.
 *
 * @author Wentai Zhang (eurekoko) - Temporary Implementation
 */
public class SimpleGameDataAccess implements GameDataAccess {

    private Player player;
    private Dealer dealer;
    private DeckProvider deckProvider;
    private Map<Integer, Double> betAmounts;
    private Map<Integer, Boolean> handCompletion;
    private String gameState;

    public SimpleGameDataAccess(Player player, Dealer dealer, DeckProvider deckProvider) {
        this.player = player;
        this.dealer = dealer;
        this.deckProvider = deckProvider;
        this.betAmounts = new HashMap<>();
        this.handCompletion = new HashMap<>();
        this.gameState = "PLAYER_TURN";
    }

    @Override
    public Player getPlayer(String playerId) {
        return player;
    }

    @Override
    public Dealer getDealer() {
        return dealer;
    }

    @Override
    public Card drawCard() {
        return deckProvider.drawCard();
    }

    @Override
    public double getBetAmount(int handIndex) {
        if (!betAmounts.containsKey(handIndex)) {
            System.err.println("WARNING: Bet not set for hand " + handIndex + ", using default 100.0");
        }
        return betAmounts.getOrDefault(handIndex, 100.0);
    }

    public void setBetAmount(int handIndex, double amount) {
        betAmounts.put(handIndex, amount);
    }

    @Override
    public void markHandComplete(int handIndex) {
        handCompletion.put(handIndex, true);
    }

    @Override
    public boolean isHandComplete(int handIndex) {
        return handCompletion.getOrDefault(handIndex, false);
    }

    public void addHandBet(int handIndex, double amount) {
        betAmounts.put(handIndex, amount);
    }

    @Override
    public boolean allHandsComplete() {
        if (!isHandComplete(0)) {
            return false;
        }
        if (player.hasSplit()) {
            if (!isHandComplete(1)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getGameState() {
        return gameState;
    }

    @Override
    public void setGameState(String state) {
        this.gameState = state;
    }
}