package usecase.exitrestartgame;

import entities.Dealer;
import entities.Deck;
import entities.Player;

/**
 * Data access interface for Exit or Restart Game use case
 */
public interface ExitRestartGameDataAccess {
    Player getPlayer();
    Deck getDeck();
    Dealer getDealer();
}
