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
        // StartGameInputData is an empty marker class for now
        StartGameInputData inputData = new StartGameInputData(); 
        interactor.execute(inputData);
    }
}