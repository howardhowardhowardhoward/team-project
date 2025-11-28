package usecase.ExitRestartGame;

/**
 * Input data for Exit or Restart Game use case
 */
public class ExitRestartGameInputData {
    private final String playerId;
    private final String operation; // "EXIT" or "RESTART"
    
    public ExitRestartGameInputData(String playerId, String operation) {
        this.playerId = playerId;
        this.operation = operation;
    }
    
    public String getPlayerId() {
        return playerId;
    }
    
    public String getOperation() {
        return operation;
    }
}