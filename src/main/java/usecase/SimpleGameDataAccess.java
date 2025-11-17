package usecase;

import entities.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory implementation of GameDataAccess for testing.
 *
 * TEMPORARY IMPLEMENTATION - To be replaced when Gopal implements proper Game management
 *
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
        // TODO: This should call Gopal's DeckApiService
        // For now, create a dummy card
        // In real implementation, this would be: deck.drawCard()

        // Temporary: return a random card
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "JACK", "QUEEN", "KING", "ACE"};
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

        String rank = ranks[(int)(Math.random() * ranks.length)];
        String suit = suits[(int)(Math.random() * suits.length)];

        int value;
        if (rank.equals("ACE")) {
            value = 11;
        } else if (rank.equals("JACK") || rank.equals("QUEEN") || rank.equals("KING")) {
            value = 10;
        } else {
            value = Integer.parseInt(rank);
        }

        return new Card(rank.charAt(0) + suit.charAt(0) + "", suit, rank, value);
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