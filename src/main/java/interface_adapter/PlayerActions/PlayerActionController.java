package interface_adapter.PlayerActions;

import usecase.PlayerActions.PlayerActionInputBoundary;
import usecase.PlayerActions.PlayerActionInputData;

/**
 * Controller for Player Actions (Hit, Stand, Double, Split).
 * Converts UI events into input data for the Interactor.
 */
public class PlayerActionController {

    private final PlayerActionInputBoundary interactor;

    public PlayerActionController(PlayerActionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String playerId, String action, int handIndex, double betAmount) {
        PlayerActionInputData inputData = new PlayerActionInputData(playerId, action, handIndex, betAmount);
        interactor.execute(inputData);
    }
}