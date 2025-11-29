package usecase.loadsavegame;

public class LoadGameOutputData {
    private final boolean success;
    private final double balance;
    private final String message;

    public LoadGameOutputData(boolean success, double balance, String message) {
        this.success = success;
        this.balance = balance;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public double getBalance() {
        return balance;
    }

    public String getMessage() {
        return message;
    }
}
