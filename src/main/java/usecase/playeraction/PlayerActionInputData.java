package usecase.playeraction;

/**
 * Input data for player actions in Blackjack.
 * Contains all information needed to execute a player's action.
 *
 * @author Wentai Zhang (eurekoko)
 */
public class PlayerActionInputData {
    // private final int handIndex;      // Which hand to act on (0 for first, 1+ for split hands)
    private final double betAmount;   // For DOUBLE and INSURANCE actions
    private final double balance;

    /**
     * Constructor for actions requiring bet amount (DOUBLE, INSURANCE)
     */
    public PlayerActionInputData(double betAmount, double balance) {
        this.betAmount = betAmount;
        this.balance = balance;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public double getBalance() {
        return balance;
    }
}
