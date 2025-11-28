package usecase.ExitRestartGame;

/**
 * Input boundary for Exit or Restart Game use case
 */
public interface ExitRestartGameInputBoundary {
    
    /**
     * Execute exit game operation
     * Main Flow: If the user selects the exit button, the game ends and the program stops running
     */
    void executeExit();
    
    /**
     * Execute restart game operation
     * Alternate Flow: The player selects the restart button
     * System updates users current balance to $1,000
     * System resets board and redeals cards
     */
    void executeRestart();
}
