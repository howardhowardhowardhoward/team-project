package interface_adapter.PlaceBetAndDeal;

import usecase.PlaceBetAndDeal.PlaceBetAndDealInputBoundary;
import usecase.PlaceBetAndDeal.PlaceBetAndDealInputData;

/**
 * Controller for placing a bet and dealing initial cards.
 */
public class PlaceBetAndDealController {
    
    private final PlaceBetAndDealInputBoundary interactor;

    public PlaceBetAndDealController(PlaceBetAndDealInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(double betAmount) {
        PlaceBetAndDealInputData inputData = new PlaceBetAndDealInputData(betAmount);
        interactor.execute(inputData);
    }
}