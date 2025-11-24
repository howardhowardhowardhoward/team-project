import gui.BlackjackGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Launch the entire Swing GUI safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new BlackjackGUI();
        });
    }
}