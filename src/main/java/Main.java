import entities.Player;
import interface_adapter.update_balance.UpdateBalanceController;
import interface_adapter.update_balance.UpdateBalancePresenter;
import usecase.updatebalance.UpdateBalanceDataAccessInterface;
import usecase.updatebalance.UpdateBalanceInteractor;

// Correct import for the moved View class
import gui.BettingView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== System Startup ===");

        // 1. Setup Data Access (Fake implementation for testing)
        // Since we don't have a real database yet, we simulate one here.
        UpdateBalanceDataAccessInterface fakeDataAccess = new UpdateBalanceDataAccessInterface() {
            // Initial player with $1000
            private Player testPlayer = new Player(1000.0);

            @Override
            public Player get(String username) {
                System.out.println("[Database] Finding player: " + username);
                return testPlayer;
            }

            @Override
            public void save(Player player) {
                System.out.println("[Database] Save successful! New Balance: " + player.getBalance());
                this.testPlayer = player;
            }
        };

        // 2. Setup Presenter
        UpdateBalancePresenter presenter = new UpdateBalancePresenter();

        // 3. Setup Interactor
        UpdateBalanceInteractor interactor = new UpdateBalanceInteractor(fakeDataAccess, presenter);

        // 4. Setup Controller
        UpdateBalanceController controller = new UpdateBalanceController(interactor);

        // 5. Launch GUI
        System.out.println("=== Launching GUI ===");
        SwingUtilities.invokeLater(() -> {
            // Create and show the Betting View
            new BettingView(controller);
        });
    }
}
