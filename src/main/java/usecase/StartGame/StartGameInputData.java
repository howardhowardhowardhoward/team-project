package usecase.StartGame;

public class StartGameInputData {
    private final double betAmount;

    public StartGameInputData(double betAmount) {
        this.betAmount = betAmount;
    }

    public double getBetAmount() {
        return betAmount;
    }
}