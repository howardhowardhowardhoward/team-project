package frameworks_and_drivers.ExitRestartGame;

import entities.Dealer;
import entities.Deck;
import entities.Player;
import interface_adapters.ExitRestartGame.ExitRestartGameController;
import usecase.ExitRestartGame.ExitRestartGameDataAccess;
import usecase.ExitRestartGame.ExitRestartGameOutputBoundary;
import usecase.ExitRestartGame.GameStateManager;

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
