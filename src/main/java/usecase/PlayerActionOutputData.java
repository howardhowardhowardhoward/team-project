package usecase;

import java.util.List;

/**
 * Output data from player action execution.
 * Contains the results and state after a player action.
 *
 * @author Wentai Zhang (eurekoko)
 */
public class PlayerActionOutputData {
    private final boolean success;
    private final String message;
    private final int handTotal;
    private final boolean isBust;
    private final boolean isBlackjack;
    private final List<String> availableActions;
    private final boolean gameComplete;
    private final String gameResult;  // For STAND: "WIN", "LOSE", "PUSH", or null if game ongoing
    private final int dealerTotal;    // Dealer's total after STAND, -1 if not applicable

    /**
     * Constructor for ongoing game actions (HIT, partial STAND)
     */
    public PlayerActionOutputData(boolean success, String message, int handTotal,
                                  boolean isBust, boolean isBlackjack,
                                  List<String> availableActions) {
        this(success, message, handTotal, isBust, isBlackjack, availableActions,
                false, null, -1);
    }

    /**
     * Full constructor including game completion data (for final STAND)
     */
    public PlayerActionOutputData(boolean success, String message, int handTotal,
                                  boolean isBust, boolean isBlackjack,
                                  List<String> availableActions, boolean gameComplete,
                                  String gameResult, int dealerTotal) {
        this.success = success;
        this.message = message;
        this.handTotal = handTotal;
        this.isBust = isBust;
        this.isBlackjack = isBlackjack;
        this.availableActions = availableActions;
        this.gameComplete = gameComplete;
        this.gameResult = gameResult;
        this.dealerTotal = dealerTotal;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getHandTotal() {
        return handTotal;
    }

    public boolean isBust() {
        return isBust;
    }

    public boolean isBlackjack() {
        return isBlackjack;
    }

    public List<String> getAvailableActions() {
        return availableActions;
    }

    public boolean isGameComplete() {
        return gameComplete;
    }

    public String getGameResult() {
        return gameResult;
    }

    public int getDealerTotal() {
        return dealerTotal;
    }
}