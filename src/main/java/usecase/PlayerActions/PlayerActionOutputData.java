package usecase.PlayerActions;

import entities.Hand;
import java.util.List;

/**
 * Output data from player action execution.
 * Contains the results and state after a player action.
 */
public class PlayerActionOutputData {

    private final boolean success;
    private final String message;

    private final int handTotal;
    private final boolean isBust;
    private final boolean isBlackjack;

    private final List<String> availableActions;

    private final boolean gameComplete;   // true when dealer turn resolved
    private final String gameResult;      // "WIN", "LOSE", "PUSH", or null
    private final int dealerTotal;        // dealer final total, -1 if NA

    private final Hand playerHand;        // cards for updating GameView

    /**
     * Constructor for ongoing game actions (HIT, SPLIT, DOUBLE, mid-STAND).
     */
    public PlayerActionOutputData(boolean success,
                                  String message,
                                  int handTotal,
                                  boolean isBust,
                                  boolean isBlackjack,
                                  List<String> availableActions,
                                  Hand playerHand) {

        this(success, message, handTotal, isBust, isBlackjack,
                availableActions, false, null, -1, playerHand);
    }

    /**
     * Constructor for final STAND where dealer resolves the round.
     */
    public PlayerActionOutputData(boolean success,
                                  String message,
                                  int handTotal,
                                  boolean isBust,
                                  boolean isBlackjack,
                                  List<String> availableActions,
                                  boolean gameComplete,
                                  String gameResult,
                                  int dealerTotal,
                                  Hand playerHand) {

        this.success = success;
        this.message = message;

        this.handTotal = handTotal;
        this.isBust = isBust;
        this.isBlackjack = isBlackjack;

        this.availableActions = availableActions;

        this.gameComplete = gameComplete;
        this.gameResult = gameResult;
        this.dealerTotal = dealerTotal;

        this.playerHand = playerHand;
    }

    // ======== GETTERS ========

    public boolean isSuccess() { return success; }

    public String getMessage() { return message; }

    public int getHandTotal() { return handTotal; }

    public boolean isBust() { return isBust; }

    public boolean isBlackjack() { return isBlackjack; }

    public List<String> getAvailableActions() { return availableActions; }

    public boolean isGameComplete() { return gameComplete; }

    public String getGameResult() { return gameResult; }

    public int getDealerTotal() { return dealerTotal; }

    public Hand getPlayerHand() { return playerHand; }
}