package usecase.ExitRestartGame;

/**
 * Input boundary for Exit or Restart Game use case
 */
public interface ExitRestartGameInputBoundary {
    
    /**
     * Execute exit game operation
     */
    void executeExit();
    
    /**
     * Execute restart game operation
     */
    void executeRestart();
}