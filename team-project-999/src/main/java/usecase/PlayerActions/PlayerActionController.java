package usecase.PlayerActions;

/**
 * Controller for Player Actions (Hit, Stand, Double, Split, Insurance).
 * Routes user input to the Interactor.
 */
public class PlayerActionController {

    private final PlayerActionInputBoundary interactor;

    public PlayerActionController(PlayerActionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes a player action.
     * @param playerId The ID of the player.
     * @param action The action command (e.g., "HIT", "STAND").
     * @param handIndex The index of the hand being played (0 for main, 1 for split).
     * @param betAmount The bet amount (used for Double/Split/Insurance).
     */
    public void execute(String playerId, String action, int handIndex, double betAmount) {
        PlayerActionInputData inputData = new PlayerActionInputData(playerId, action, handIndex, betAmount);
        interactor.execute(inputData);
    }
}