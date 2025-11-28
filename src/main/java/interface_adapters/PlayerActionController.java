package interface_adapters;

import entities.Deck;
import entities.Player;
import entities.Dealer;
import usecase.PlayerActions.PlayerActionInputBoundary;

public class PlayerActionController {
    private final PlayerActionInputBoundary interactor;

    public PlayerActionController(PlayerActionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void hit() {
        interactor.hit();
    }

    public void stand() {
        interactor.stand();
    }

    public void doubleDown() {
        interactor.doubleDown();
    }

    public void split() {
        interactor.split();
    }

    public void insurance() {
        interactor.insurance();
    }

    public void handleRoundResult() {
        interactor.handleRoundResult();
    }

    public Deck getDeck() {
        return interactor.getDeck();
    }

    public Player getPlayer() {
        return interactor.getPlayer();
    }

    public Dealer getDealer() {
        return interactor.getDealer();
    }
}
