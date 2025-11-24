package usecase.PlayerActions;

/**
 * Controller for Player Actions (Hit, Stand, Double, Split, Insurance).
 * Translates GUI button presses into InputData and calls the Interactor.
 */
public class PlayerActionController {

    private final PlayerActionInputBoundary interactor;

    private final String playerId = "PLAYER_1"; // or inject later if multi-player

    public PlayerActionController(PlayerActionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /** Wrapper for HIT */
    public void hit(int handIndex) {
        interactor.execute(new PlayerActionInputData(playerId, "HIT", handIndex));
    }

    /** Wrapper for STAND */
    public void stand(int handIndex) {
        interactor.execute(new PlayerActionInputData(playerId, "STAND", handIndex));
    }

    /** Wrapper for SPLIT */
    public void split() {
        interactor.execute(new PlayerActionInputData(playerId, "SPLIT", 0));
    }

    /** Wrapper for DOUBLE — GUI must supply bet amount */
    public void doubleDown(int handIndex, double betAmount) {
        interactor.execute(new PlayerActionInputData(playerId, "DOUBLE", handIndex, betAmount));
    }

    /** Wrapper for INSURANCE — GUI must supply bet amount */
    public void insurance(int handIndex, double betAmount) {
        interactor.execute(new PlayerActionInputData(playerId, "INSURANCE", handIndex, betAmount));
    }
}