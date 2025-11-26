package interface_adapter.StartGame;

import usecase.StartGame.StartGameInputBoundary;
import usecase.StartGame.StartGameInputData;

/**
 * Controller for starting a new game session.
 */
public class StartGameController {

    private final StartGameInputBoundary interactor;

    public StartGameController(StartGameInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        // 0.0 is a placeholder for initial bet or signal
        StartGameInputData inputData = new StartGameInputData(0.0); 
        interactor.execute(inputData);
    }
}