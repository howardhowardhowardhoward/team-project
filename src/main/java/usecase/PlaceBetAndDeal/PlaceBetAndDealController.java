package usecase.PlaceBetAndDeal;

public class PlaceBetAndDealController {
    private final PlaceBetAndDealInputBoundary interactor;

    public PlaceBetAndDealController(PlaceBetAndDealInteractor interactor) {
        this.interactor = interactor;
    }

    public void execute(double betAmount) {
        PlaceBetAndDealInputData inputData = new PlaceBetAndDealInputData(betAmount);
        interactor.execute(inputData);
    }
}
