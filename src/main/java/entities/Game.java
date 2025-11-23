package entities;

import frameworks_and_drivers.DeckApiService;

public class Game {
    private Player player;
    private Dealer dealer;
    private Deck gameDeck;
    private int balance;
    private int currentBet;
    private boolean roundActive;

    // Default constructor - creates new DeckApiService (backward compatibility)
    public Game() {
        this(new DeckApiService());
    }

    // IMPROVED: Constructor with dependency injection for better testability
    public Game(DeckProvider deckProvider) {
        this.gameDeck = new Deck(deckProvider);
        this.player = new Player(1000);
        this.dealer = new Dealer(gameDeck);
        this.balance = 1000; // starting balance
        this.roundActive = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public int getBalance() {
        return balance;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public boolean isRoundActive() {
        return roundActive;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setCurrentBet(int bet) {
        this.currentBet = bet;
    }

    public void setRoundActive(boolean active) {
        this.roundActive = active;
    }


    // ============================
    //       GAME MANAGEMENT
    // ============================

    /** Reset the table for a new round */
    public void reset() {
        player.clearHands();
        currentBet = 0;
        roundActive = true;
    }

    public void applyWinnings(int amount) {
        balance += amount;
    }

    public boolean placeBet(int amount) {
        if (amount > balance || amount <= 0) {
            return false;
        }
        balance -= amount;
        currentBet = amount;
        return true;
    }

    // ============================
    //       BLACKJACK CHECKS
    // ============================

    /** Check initial blackjack case for player or dealer */
    public boolean checkInitialBlackjack() {
        return player.isBlackjack();
    }

}

