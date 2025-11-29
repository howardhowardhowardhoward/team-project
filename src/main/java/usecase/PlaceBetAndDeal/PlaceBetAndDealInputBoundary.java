package usecase.PlaceBetAndDeal;
import entities.*;

public interface PlaceBetAndDealInputBoundary {
    void execute(PlaceBetAndDealInputData inputData);
    void addReservedBet(double amount);
    void clearReservedBet();
    void allIn();
    void restartGame();
    Deck getDeck();
    Dealer getDealer();
    Player getPlayer();
}
