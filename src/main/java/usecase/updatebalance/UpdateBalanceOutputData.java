package usecase.updatebalance;

public class UpdateBalanceOutputData {
    private final String username;
    private final double newBalance;

    public UpdateBalanceOutputData(String username, double newBalance) {
        this.username = username;
        this.newBalance = newBalance;
    }

    public String getUsername() { return username; }
    public double getNewBalance() { return newBalance; }
}