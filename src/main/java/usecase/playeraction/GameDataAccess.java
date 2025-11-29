package usecase.playeraction;

import entities.Card;
import entities.Dealer;
import entities.Player;

public interface GameDataAccess {

    Player getPlayer(String playerId);

    Dealer getDealer();

    Card drawCard();

    double getBetAmount(int handIndex);

    void markHandComplete(int handIndex);

    boolean isHandComplete(int handIndex);

    boolean allHandsComplete();

    String getGameState();

    void setGameState(String state);
}
