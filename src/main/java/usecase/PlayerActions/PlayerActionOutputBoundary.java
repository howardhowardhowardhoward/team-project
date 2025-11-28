package usecase.PlayerActions;

public interface PlayerActionOutputBoundary {
    void present(PlayerActionOutputData outputData);
    void presentError(String message);
    void presentResult(String message, double newBalance, double newBet);
}
