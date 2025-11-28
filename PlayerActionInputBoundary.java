package usecase.PlayerActions;

import entities.Deck;
import entities.Player;
import entities.Dealer;
import usecase.PlaceBetAndDeal.PlaceBetAndDealInputData;

public interface PlayerActionInputBoundary {
    void hit();
    void stand();
    void doubleDown();
    void split();
    void insurance();
    void handleRoundResult();
    Player getPlayer();
    Deck getDeck();
    Dealer getDealer();
}
