package usecase.ExitRestartGame;

import entities.Player;

/**
 * Data access interface for Exit or Restart Game use case
 */
public interface ExitRestartGameDataAccess {
    
    /**
     * Get current player ID
     */
    String getCurrentPlayerId();
    
    /**
     * Get player by ID
     */
    Player getPlayer(String playerId);
    
    /**
     * Save player data
     */
    void savePlayer(Player player);
    
    /**
     * Check if game is active
     */
    boolean isGameActive();
}