package gui.view;

import interface_adapter.update_balance.UpdateBalanceController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Betting View (GUI).
 * Based on the design document "UI for black jack.pdf" (Pages 1-2).
 * Features:
 * - Top Bar: Exit button and Balance display[cite: 6, 7].
 * - Center: Large display of current bet amount.
 * - Bottom: Chip buttons (1, 5, 25, 50, 100, 500) and action buttons (Clear, Deal) [cite: 10-18].
 */
public class BettingView extends JFrame {

    private final UpdateBalanceController controller;
    private final JLabel balanceLabel;
    private final JLabel currentBetLabel;

    // Internal state to track the bet before clicking DEAL
    private int currentBetAmount = 0;

    public BettingView(UpdateBalanceController controller) {
        this.controller = controller;

        // 1. Window Setup
        this.setTitle("Blackjack");
        this.setSize(500, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(new Color(0, 100, 0)); // Dark Green background like a table

        // --- TOP PANEL (Exit & Balance) ---
        // Matches PDF Page 1 [cite: 6, 7]
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Transparent to show background
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(e -> System.exit(0)); // Close functionality

        balanceLabel = new JLabel("Balance: $1000");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(Color.WHITE);

        topPanel.add(exitButton, BorderLayout.WEST);
        topPanel.add(balanceLabel, BorderLayout.EAST);
        this.add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (Bet Display) ---
        // Matches PDF Page 1 "Bet: $0"
        JPanel centerPanel = new JPanel(new GridBagLayout()); // Center content
        centerPanel.setOpaque(false);

        currentBetLabel = new JLabel("Bet: $0");
        currentBetLabel.setFont(new Font("Arial", Font.BOLD, 40));
        currentBetLabel.setForeground(Color.YELLOW);

        centerPanel.add(currentBetLabel);
        this.add(centerPanel, BorderLayout.CENTER);

        // --- BOTTOM PANEL (Chips & Actions) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        // 1. Chips Row (1, 5, 25, 50, 100, 500)
        JPanel chipsPanel = new JPanel(new FlowLayout());
        chipsPanel.setOpaque(false);

        int[] chipValues = {1, 5, 25, 50, 100, 500};
        for (int value : chipValues) {
            JButton chipBtn = new JButton("$" + value);
            // Style the chip button (Circular shape simulated)
            chipBtn.setPreferredSize(new Dimension(60, 60));
            chipBtn.setFont(new Font("Arial", Font.BOLD, 12));
            chipBtn.setBackground(Color.WHITE);

            // Logic: Add to current bet
            chipBtn.addActionListener(e -> addToBet(value));
            chipsPanel.add(chipBtn);
        }
        bottomPanel.add(chipsPanel);

        // 2. Action Row (Clear & Deal) [cite: 18, 33]
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setOpaque(false);

        JButton clearButton = new JButton("CLEAR");
        clearButton.setBackground(Color.RED);
        clearButton.setOpaque(true);
        clearButton.setBorderPainted(false);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.addActionListener(e -> clearBet());

        JButton dealButton = new JButton("DEAL");
        dealButton.setBackground(Color.GREEN);
        dealButton.setOpaque(true);
        dealButton.setBorderPainted(false);
        dealButton.setFont(new Font("Arial", Font.BOLD, 14));
        dealButton.setPreferredSize(new Dimension(100, 40));
        dealButton.addActionListener(e -> handleDeal());

        actionPanel.add(clearButton);
        actionPanel.add(Box.createHorizontalStrut(20)); // Spacer
        actionPanel.add(dealButton);

        bottomPanel.add(actionPanel);
        this.add(bottomPanel, BorderLayout.SOUTH);

        // Finalize
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Adds value to the current bet accumulation.
     * Updates the UI label immediately.
     */
    private void addToBet(int amount) {
        currentBetAmount += amount;
        updateBetDisplay();
    }

    /**
     * Resets the bet accumulation to 0.
     * Corresponds to "CLEAR" button.
     */
    private void clearBet() {
        currentBetAmount = 0;
        updateBetDisplay();
    }

    /**
     * Updates the large central text.
     */
    private void updateBetDisplay() {
        currentBetLabel.setText("Bet: $" + currentBetAmount);
    }

    /**
     * Submits the bet to the controller.
     * This triggers the UpdateBalance use case.
     */
    private void handleDeal() {
        if (currentBetAmount <= 0) {
            JOptionPane.showMessageDialog(this, "Please place a bet first!");
            return;
        }

        // Execute the use case (deduct balance)
        // Note: We pass negative amount because betting reduces balance
        controller.execute("Howard", -currentBetAmount);

        JOptionPane.showMessageDialog(this, "Deal! Bet placed: $" + currentBetAmount);

        // TODO: Logic to transition to the Main Game Page would go here
    }
}
