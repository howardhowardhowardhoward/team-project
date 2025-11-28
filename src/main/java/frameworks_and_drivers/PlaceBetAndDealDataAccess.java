package frameworks_and_drivers;
import entities.*;
import usecase.PlaceBetAndDeal.PlaceBetAndDealDataAccessInterface;

public class PlaceBetAndDealDataAccess implements PlaceBetAndDealDataAccessInterface {
    private final Player player;
    private final Deck deck;
    private final Dealer dealer;

    public PlaceBetAndDealDataAccess(Player player, Deck deck, Dealer dealer) {
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
