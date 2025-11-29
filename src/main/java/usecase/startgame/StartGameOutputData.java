package usecase.startgame;

public class StartGameOutputData {
    private final boolean gameReady;

    public StartGameOutputData(boolean gameReady) {
        this.gameReady = gameReady;
    }

    public boolean isGameReady() {
        return gameReady;
    }
}
