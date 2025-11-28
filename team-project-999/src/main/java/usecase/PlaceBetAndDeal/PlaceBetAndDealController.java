package usecase.PlaceBetAndDeal;

public class PlaceBetAndDealController {
    
    // Use interface type
    private final PlaceBetAndDealInputBoundary interactor;

    // Constructor receives interface type
    public PlaceBetAndDealController(PlaceBetAndDealInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(double betAmount) {
        PlaceBetAndDealInputData inputData = new PlaceBetAndDealInputData(betAmount);
        interactor.execute(inputData);
    }
}