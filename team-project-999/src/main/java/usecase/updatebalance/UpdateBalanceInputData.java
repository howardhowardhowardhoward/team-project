package usecase.updatebalance;

public class UpdateBalanceInputData {
    private final String username;
    private final double amount;

    public UpdateBalanceInputData(String username, double amount) {
        this.username = username;
        this.amount = amount;
    }

    public String getUsername() { return username; }
    public double getAmount() { return amount; }
}