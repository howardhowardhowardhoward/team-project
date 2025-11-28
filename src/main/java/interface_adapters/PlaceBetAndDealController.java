package interface_adapters;

import entities.*;
import usecase.PlaceBetAndDeal.PlaceBetAndDealInputBoundary;
import usecase.PlaceBetAndDeal.PlaceBetAndDealInputData;

public class PlaceBetAndDealController {
    private final PlaceBetAndDealInputBoundary interactor;

    public PlaceBetAndDealController(PlaceBetAndDealInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(PlaceBetAndDealInputData inputData) {
        interactor.execute(inputData);
    }

    public void addChip(double amount) {
        interactor.addReservedBet(amount);
    }

    public void allIn() {
        interactor.allIn();
    }

    public void clearBet() {
        interactor.clearReservedBet();
    }

    public Deck getDeck() {
        return interactor.getDeck();
    }

    public Dealer getDealer() {
        return interactor.getDealer();
    }

    public Player getPlayer() {
        return interactor.getPlayer();
    }
}
