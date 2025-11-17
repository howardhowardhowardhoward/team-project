package usecase;

/**
 * Input data for player actions in Blackjack.
 * Contains all information needed to execute a player's action.
 *
 * @author Wentai Zhang (eurekoko)
 */
public class PlayerActionInputData {
    private final String playerId;
    private final String action;      // "HIT", "STAND", "DOUBLE", "SPLIT", "INSURANCE"
    private final int handIndex;      // Which hand to act on (0 for first, 1+ for split hands)
    private final double betAmount;   // For DOUBLE and INSURANCE actions

    /**
     * Constructor for basic actions (HIT, STAND)
     */
    public PlayerActionInputData(String playerId, String action, int handIndex) {
        this(playerId, action, handIndex, 0.0);
    }

    /**
     * Constructor for actions requiring bet amount (DOUBLE, INSURANCE)
     */
    public PlayerActionInputData(String playerId, String action, int handIndex, double betAmount) {
        this.playerId = playerId;
        this.action = action.toUpperCase();
        this.handIndex = handIndex;
        this.betAmount = betAmount;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getAction() {
        return action;
    }

    public int getHandIndex() {
        return handIndex;
    }

    public double getBetAmount() {
        return betAmount;
    }
}
