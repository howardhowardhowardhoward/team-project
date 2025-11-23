package usecase.StartGame;

public interface StartGameOutputBoundary {
    void present(StartGameOutputData outputData);
    void presentBetError(String message);
}
