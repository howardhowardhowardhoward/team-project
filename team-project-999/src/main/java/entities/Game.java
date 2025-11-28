package entities;

import frameworks_and_drivers.DeckApiService;
import frameworks_and_drivers.DeckProvider;
import frameworks_and_drivers.Deck; // 新增：明确导入 Deck

public class Game {
    private Player player;
    private Dealer dealer;
    private Deck gameDeck;
    private int balance;
    private int currentBet;
    private boolean roundActive;

    // Default constructor
    public Game() {
        this(new DeckApiService());
    }

    // Constructor with dependency injection
    public Game(DeckProvider deckProvider) {
        this.gameDeck = new Deck(deckProvider);
        this.player = new Player(1000);
        this.dealer = new Dealer();
        this.balance = 1000;
        this.roundActive = false;
    }

    public Player getPlayer() { return player; }
    public Dealer getDealer() { return dealer; }
    public int getBalance() { return balance; }
    public int getCurrentBet() { return currentBet; }
    public boolean isRoundActive() { return roundActive; }

    public void setBalance(int balance) { this.balance = balance; }
    public void setCurrentBet(int bet) { this.currentBet = bet; }
    public void setRoundActive(boolean active) { this.roundActive = active; }

    public void reset() {
        player.clearHands();
        currentBet = 0;
        roundActive = true;
    }

    public void applyWinnings(int amount) { balance += amount; }

    public boolean placeBet(int amount) {
        if (amount > balance || amount <= 0) return false;
        balance -= amount;
        currentBet = amount;
        return true;
    }

    public boolean checkInitialBlackjack() { return player.isBlackjack(); }
}