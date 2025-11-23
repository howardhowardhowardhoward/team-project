package usecase.PlayerActions;

import entities.*;
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
    private Deck deck;
    private Map<Integer, Double> betAmounts;
    private Map<Integer, Boolean> handCompletion;
    private String gameState;

    public SimpleGameDataAccess(Player player, Dealer dealer, Deck deck) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
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
        return deck.drawCard();
    }

    @Override
    public double getBetAmount(int handIndex) {
        return betAmounts.getOrDefault(handIndex, 100.0);  // Default $100 bet
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
        for (int i = 0; i < player.getHands().size(); i++) {
            if (!isHandComplete(i)) {
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