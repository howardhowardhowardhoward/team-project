package usecase.PlayerActions;

import entities.*;
import frameworks_and_drivers.Deck; // 新增：导入 Deck

import java.util.HashMap;
import java.util.Map;

public class SimpleGameDataAccess implements GameDataAccess {

    private Player player;
    private Dealer dealer;
    private Deck deck; // 使用 frameworks_and_drivers.Deck
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
    public Player getPlayer(String playerId) { return player; }

    @Override
    public Dealer getDealer() { return dealer; }

    @Override
    public Card drawCard() { return deck.drawCard(); }

    @Override
    public double getBetAmount(int handIndex) {
        return betAmounts.getOrDefault(handIndex, 100.0);
    }

    @Override
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
    public void addHandBet(int handIndex, double amount) {
        betAmounts.put(handIndex, amount);
    }

    @Override
    public boolean allHandsComplete() {
        for (int i = 0; i < player.getHands().size(); i++) {
            if (!isHandComplete(i)) return false;
        }
        return true;
    }

    @Override
    public String getGameState() { return gameState; }

    @Override
    public void setGameState(String state) { this.gameState = state; }
}