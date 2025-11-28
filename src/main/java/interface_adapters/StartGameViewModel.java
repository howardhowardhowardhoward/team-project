package interface_adapters.StartGame;

import java.beans.PropertyChangeSupport;

public class StartGameViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private boolean gameReady = false;

    public void setGameReady(boolean gameReady) {
        this.gameReady = gameReady;
        support.firePropertyChange("state", null, this);
    }
}
