package entities;

/**
 * Player Entity.
 * Represents a user in the game, managing their balance and hands.
 * * UPDATED DESIGN:
 * Uses specific hand1 and hand2 fields to support the "Split once" rule
 * while maintaining simplicity for the team.
 */
public class Player {
    private double balance;
    private Hand hand;
    private Hand hand2;

    // Primary hand (always exists)
    private Hand hand1;

    // Secondary hand (only exists after splitting, otherwise null)
    private Hand hand2;

    /**
     * Constructor.
     * @param initialBalance The starting chips for the player.
     */
    public Player(double initialBalance) {
        this.balance = initialBalance;
        this.hand1 = new Hand();
        this.hand2 = null; // Initially, there is no second hand
    }

    // --- Compatibility Method (Crucial) ---

    /**
     * Gets the primary hand.
     * This method exists to ensure backward compatibility with teammates' code
     * that expects a single hand (e.g., player.getHand()).
     */
    public Hand getHand() {
        return this.hand1;
    }

    // --- New Hand Accessors ---

    public Hand getHand1() {
        return hand1;
    }

    public Hand getHand2() {
        return hand2;
    }

    /**
     * Checks if the player has split their hand.
     * @return true if hand2 exists.
     */
    public boolean hasSplit() {
        return hand2 != null;
    }

    // --- Betting & Balance Logic ---

    /**
     * Adjusts the player's balance by a specific amount.
     * Used by the UpdateBalance Use Case.
     * @param amount Positive to add (win), negative to subtract (lose/bet).
     */
    public void adjustBalance(double amount) {
        this.balance += amount;
    }

    /**
     * Places a bet.
     * (Logic provided by teammate)
     */
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

    /**
     * Checks if the primary hand is a Blackjack.
     */
    public boolean isBlackjack() {
        return this.hand1.isBlackjack();
    }

    /**
     * Clears hands for a new round.
     * Resets hand1 and removes hand2.
     */
    public void clearHands() {
        this.hand1.clear();
        // If we have a second hand, clear and remove it
        if (this.hand2 != null) {
            this.hand2.clear();
            this.hand2 = null;
        }
    }

    /**
     * Logic to perform a split.
     * (To be fully implemented in the Split Use Case)
     */
    public void split() {
        if (hasSplit()) {
            throw new IllegalStateException("Cannot split more than once.");
        }
        this.hand2 = new Hand();
        // Future logic: Move one card from hand1 to hand2, and deduct bet again.
    }
}