package frameworks_and_drivers.exitrestartgame;

import entities.Dealer;
import entities.Deck;
import entities.Player;
import usecase.exitrestartgame.ExitRestartGameDataAccess;

/**
 * Main class for Exit or Restart Game use case
 * Coordinates the use case operations
 */
public class ExitRestartGame implements ExitRestartGameDataAccess {
    private final Player player;
    private final Deck deck;
    private final Dealer dealer;

    public ExitRestartGame(Player player, Deck deck, Dealer dealer) {
        this.player = player;
        this.deck = deck;
        this.dealer = dealer;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Deck getDeck() {
        return deck;
    }

    @Override
    public Dealer getDealer() {
        return dealer;
    }

}
