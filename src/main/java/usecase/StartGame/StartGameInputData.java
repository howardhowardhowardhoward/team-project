package usecase.StartGame;

public class StartGameInputData {
    // You can add fields later if needed
    private final double betAmount;

    public StartGameInputData(double betAmount) {
        this.betAmount = betAmount;
    }

    public double getBetAmount() {
        return betAmount;
    }
}
