package usecase.ExitRestartGame;

/**
 * Output boundary for Exit or Restart Game use case
 */
public interface ExitRestartGameOutputBoundary {
    
    /**
     * Present the result of exit operation
     */
    void presentExitResult(ExitRestartGameOutputData outputData);
    
    /**
     * Present the result of restart operation
     */
    void presentRestartResult(ExitRestartGameOutputData outputData);
    
    /**
     * Present error message if operation fails
     */
    void presentError(String errorMessage);
}