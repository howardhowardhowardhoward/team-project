package usecase.StartGame;

/**
 * Controller for the StartGame use case.
 */
public class StartGameController {
    
    private final StartGameInputBoundary interactor;

    public StartGameController(StartGameInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Handles the request to start a new game (shuffle and initial deal).
     */
    public void execute() {
        StartGameInputData inputData = new StartGameInputData(0.0); 
        interactor.execute(inputData);
    }
}