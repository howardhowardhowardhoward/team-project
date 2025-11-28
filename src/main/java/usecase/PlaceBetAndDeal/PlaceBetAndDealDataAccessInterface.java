package usecase.PlaceBetAndDeal;
import entities.*;

public interface PlaceBetAndDealDataAccessInterface {
    Player getPlayer();
    Deck getDeck();
    Dealer getDealer();
}
