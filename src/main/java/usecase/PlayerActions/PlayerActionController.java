package usecase.PlayerActions;

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