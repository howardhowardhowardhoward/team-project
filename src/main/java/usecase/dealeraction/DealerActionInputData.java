package usecase.dealeraction;

/**
 * Input data for DealerAction use case.
 */
public class DealerActionInputData {
    private final String playerId; 
    
    public DealerActionInputData(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}