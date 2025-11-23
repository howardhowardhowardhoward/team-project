package entities;

/**
 * Player Entity.
 * Represents a user in the game, managing their balance and hands.
 * Fixed: Removed duplicate field declarations caused by a bad merge.
 */
public class Player {
    private double balance;

    // Primary hand (always exists)
    /**
     * Constructor.
     * @param initialBalance The starting chips for the player.
     */
    public Player(double initialBalance) {
        this.balance = initialBalance;
        this.hand1 = new Hand();
        this.hand2 = null; // Ensure this is null initially!
    }

    // --- Compatibility Method ---

    /**
     * Gets the primary hand.
     * Returns hand1 so that teammate's code (which expects getHand()) still works.
     */
    public Hand getHand() {
        return this.hand1;
    }

    // --- Accessors ---

    public Hand getHand1() {
        return hand1;
    }

    public Hand getHand2() {
        return hand2;
    }

    public boolean hasSplit() {
        return hand2 != null;
    }

    // --- Betting & Balance Logic ---

    public void adjustBalance(double amount) {
        this.balance += amount;
    }

    public void placeBet(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Not enough balance.");
        }
        this.balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    // --- Game Logic ---

    public boolean isBlackjack() {
        return this.hand1.isBlackjack();
    }

    public void clearHands() {
        this.hand1.clear();
        if (this.hand2 != null) {
            this.hand2.clear();
            this.hand2 = null;
        }
    }

    public void split() {
        if (hasSplit()) {
            throw new IllegalStateException("Cannot split more than once.");
        }
        this.hand2 = new Hand();
        // Logic to move card and deduct bet goes here later
    }
}