package usecase;

/**
 * Interface for updating player balance.
 *
 * TO BE IMPLEMENTED BY: Eden (UpdateBalance Use Case)
 *
 * This interface allows PlayerActionInteractor to update player balance
 * when actions like DOUBLE, SPLIT, or final STAND occur.
 *
 * @author Wentai Zhang (eurekoko) - Interface Definition
 */
public interface BalanceUpdater {

    /**
     * Deduct money from player's balance (for bets)
     *
     * @param playerId the player's unique identifier
     * @param amount the amount to deduct (positive number)
     * @param reason the reason for deduction (e.g., "DOUBLE_DOWN", "SPLIT_BET", "INSURANCE")
     * @return true if successful, false if insufficient funds
     */
    boolean deductBalance(String playerId, double amount, String reason);

    /**
     * Add money to player's balance (for winnings)
     *
     * @param playerId the player's unique identifier
     * @param amount the amount to add (positive number)
     * @param reason the reason for addition (e.g., "WIN", "PUSH", "BLACKJACK", "INSURANCE_WIN")
     * @return the new balance after addition
     */
    double addBalance(String playerId, double amount, String reason);

    /**
     * Get current player balance
     *
     * @param playerId the player's unique identifier
     * @return the current balance
     */
    double getBalance(String playerId);
}
