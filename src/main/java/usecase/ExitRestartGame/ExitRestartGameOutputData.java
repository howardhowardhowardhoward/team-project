package usecase;

/**
 * Output data for Exit or Restart Game use case
 */
public class ExitRestartGameOutputData {
    private final boolean success;
    private final String message;
    private final double newBalance;
    private final boolean gameExited;
    private final boolean gameRestarted;
    
    public ExitRestartGameOutputData(boolean success, String message, 
                                   double newBalance, boolean gameExited, 
                                   boolean gameRestarted) {
        this.success = success;
        this.message = message;
        this.newBalance = newBalance;
        this.gameExited = gameExited;
        this.gameRestarted = gameRestarted;
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getNewBalance() { return newBalance; }
    public boolean isGameExited() { return gameExited; }
    public boolean isGameRestarted() { return gameRestarted; }
}