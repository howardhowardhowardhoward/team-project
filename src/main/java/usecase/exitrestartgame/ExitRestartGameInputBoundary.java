package usecase.exitrestartgame;

import entities.Dealer;
import entities.Deck;
import entities.Player;

/**
 * Input boundary for Exit or Restart Game use case
 */
public interface ExitRestartGameInputBoundary {
    
    /**
     * Execute exit game operation
     * Main Flow: If the user selects the exit button, the game ends and the program stops running
     */
    void executeExit(ExitRestartGameInputData inputData);
    
    /**
     * Execute restart game operation
     * Alternate Flow: The player selects the restart button
     * System updates users current balance to $1,000
     * System resets board and redeals cards
     */
    void executeRestart(ExitRestartGameInputData inputData);

    Deck getDeck();
    Dealer getDealer();
    Player getPlayer();
}
