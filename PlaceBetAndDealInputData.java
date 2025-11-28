package usecase.PlaceBetAndDeal;

public class PlaceBetAndDealInputData {
    private final double betAmount;

    public PlaceBetAndDealInputData(double betAmount) {
        this.betAmount = betAmount;
    }

    public double getBetAmount() {
        return betAmount;
    }
}
