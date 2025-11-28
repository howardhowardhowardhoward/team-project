package usecase.ExitRestartGame;

/**
 * Manages game state for Exit/Restart operations
 */
public interface GameStateManager {
    
    /**
     * Set game exited flag
     */
    void setGameExited(boolean exited);
    
    /**
     * Check if game is exited
     */
    boolean isGameExited();
    
    /**
     * Reset game state for restart
     */
    void resetGameState();
    
    /**
     * Get current game status
     */
    String getGameStatus();
}