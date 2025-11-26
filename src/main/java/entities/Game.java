package entities;

/**
 * Represents the core game entity.
 * Contains the player, dealer, and current round state.
 */
public class Game {
    private Player player;
    private Dealer dealer;
    private int currentBet;
    private boolean roundActive;

    /**
     * Constructor allowing injection of existing Player and Dealer entities.
     * This ensures synchronization with the Data Access layer.
     */
    public Game(Player player, Dealer dealer) {
        this.player = player;
        this.dealer = dealer;
        this.roundActive = false;
    }

    /**
     * Default constructor for testing purposes.
     */
    public Game() {
        this.player = new Player(1000);
        this.dealer = new Dealer();
    }

    public Player getPlayer() { return player; }
    public Dealer getDealer() { return dealer; }
    public int getBalance() { return (int) player.getBalance(); }
    public int getCurrentBet() { return currentBet; }
    public boolean isRoundActive() { return roundActive; }

    public void setCurrentBet(int bet) { this.currentBet = bet; }
    public void setRoundActive(boolean active) { this.roundActive = active; }

    public void reset() {
        player.clearHands();
        dealer.getHand().clear();
        currentBet = 0;
        roundActive = true;
    }

    public boolean placeBet(int amount) {
        if (amount > player.getBalance() || amount <= 0) return false;
        currentBet = amount;
        return true;
    }

    public boolean checkInitialBlackjack() { return player.isBlackjack(); }
}