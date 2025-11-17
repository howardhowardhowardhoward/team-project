package entities;

public class Game {
    private Player player;
    private Player dealer;  // we can reuse Player as dealer, or create Dealer class
    private String deckId;
    private int balance;
    private int currentBet;
    private boolean roundActive;

    public Game() {
        this.player = new Player();
        this.dealer = new Player(); // Dealer also has a Hand
        this.balance = 1000; // starting balance
        this.roundActive = false;
    }

    // ============================
    //          GETTERS
    // ============================

    public Player getPlayer() {
        return player;
    }

    public Player getDealer() {
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

    public String getDeckId() {
        return deckId;
    }

    // ============================
    //          SETTERS
    // ============================

    public void setDeckId(String deckId) {
        this.deckId = deckId;
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
        player.clearHand();
        dealer.clearHand();
        currentBet = 0;
        roundActive = true;
    }

    /** Add winnings to the balance */
    public void applyWinnings(int amount) {
        balance += amount;
    }

    /** Deduct the bet from the balance */
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

    public boolean isPlayerBlackjack() {
        return player.getHand().isBlackjack();
    }

    public boolean isDealerBlackjack() {
        return dealer.getHand().isBlackjack();
    }

    /** Check initial blackjack case for player or dealer */
    public boolean checkInitialBlackjack() {
        return isPlayerBlackjack() || isDealerBlackjack();
    }

}
}
