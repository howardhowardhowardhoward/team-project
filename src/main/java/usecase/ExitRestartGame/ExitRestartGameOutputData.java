package usecase.ExitRestartGame;

/**
 * Output data for Exit or Restart Game use case
 */
public class ExitRestartGameOutputData {
    private final String message;
    private final double newBalance;
    private final double newBetAmount;
    
    public ExitRestartGameOutputData(String message, double newBalance, double newBetAmount) {
        this.message = message;
        this.newBalance = newBalance;
        this.newBetAmount = newBetAmount;
    }
    
    // Getters
    public String getMessage() { return message; }
    public double getNewBalance() { return newBalance; }
    public double  getNewBetAmount() { return newBetAmount; }
}
