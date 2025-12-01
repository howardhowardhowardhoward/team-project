package entities;

public class Game {
    private final Player player;
    private final Dealer dealer;
    private final Deck gameDeck;
    private int balance;

    public Game(Deck gameDeck) {
        this.gameDeck = gameDeck;
        this.player = new Player(1000);
        this.dealer = new Dealer(gameDeck); // Dealer also has a Hand
        this.balance = 1000; // starting balance
    }

    public Game(Deck gameDeck, double loadedBalance) {
        this.gameDeck = gameDeck;
        this.player = new Player(loadedBalance);
        this.dealer = new Dealer(gameDeck);
        this.balance = (int) loadedBalance;
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

    public Deck getDeck() {
        return gameDeck;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    // ============================
    //       GAME MANAGEMENT
    // ============================

    /** Reset the table for a new round */
    public void reset() {
        player.clearHands();
    }
}

