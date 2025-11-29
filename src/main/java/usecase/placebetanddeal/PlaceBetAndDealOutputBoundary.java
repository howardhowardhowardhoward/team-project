package usecase.placebetanddeal;

public interface PlaceBetAndDealOutputBoundary {
    void present(PlaceBetAndDealOutputData outputData);
    void presentError(String message);
    void presentBetUpdated(double newBalance, double newBetAmount);
}
