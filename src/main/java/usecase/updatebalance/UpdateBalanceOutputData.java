package usecase.updatebalance;

public class UpdateBalanceOutputData {
    private final double newBalance;

    public UpdateBalanceOutputData(double newBalance) {
        this.newBalance = newBalance;
    }

    public double getNewBalance() {
        return newBalance;
    }
}