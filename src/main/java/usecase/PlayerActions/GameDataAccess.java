package usecase.PlayerActions;

import entities.Card;
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

    void setBetAmount(int handIndex, double v);
    void addHandBet(int handIndex, double amount);
}
