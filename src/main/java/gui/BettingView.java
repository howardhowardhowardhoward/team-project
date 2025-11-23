package gui;

import interface_adapter.update_balance.UpdateBalanceController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * The Betting View for the Blackjack game.
 * This class handles the UI for placing bets using Java Swing.
 */
public class BettingView extends JFrame {

    private final UpdateBalanceController controller;
    private final JLabel balanceLabel;
    private final JTextField betAmountField;

    /**
     * Constructor.
     * @param controller The controller to handle the "Deal" action.
     */
    public BettingView(UpdateBalanceController controller) {
        this.controller = controller;

        // 1. Basic Window Settings
        this.setTitle("Blackjack - Place Your Bet");
        this.setSize(400, 550); // Increased height slightly for the logo
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        // 2. Logo Panel
        JPanel logoPanel = new JPanel();
        URL imgUrl = getClass().getResource("/BlackjackLogo.png");
        if (imgUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imgUrl);
            Image image = originalIcon.getImage();
            // Scale the image to fit the window
            Image newImg = image.getScaledInstance(300, 150, java.awt.Image.SCALE_SMOOTH);
            logoPanel.add(new JLabel(new ImageIcon(newImg)));
        } else {
            // Fallback text if image is missing
            System.err.println("Error: Logo image not found in resources!");
            logoPanel.add(new JLabel("BlackJack Game"));
        }
        this.add(logoPanel);

        // 3. Balance Display Panel
        JPanel balancePanel = new JPanel();
        balanceLabel = new JLabel("Current Balance: $1000");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balancePanel.add(balanceLabel);
        this.add(balancePanel);

        // 4. Input Panel (Text Field)
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Bet Amount:"));
        betAmountField = new JTextField(10);
        betAmountField.setText("0");
        inputPanel.add(betAmountField);
        this.add(inputPanel);

        // 5. Chips Buttons Panel
        JPanel chipsPanel = new JPanel();
        createChipButton(chipsPanel, 1);
        createChipButton(chipsPanel, 5);
        createChipButton(chipsPanel, 25);
        createChipButton(chipsPanel, 100);
        this.add(chipsPanel);

        // 6. Deal Button Panel
        JPanel buttonPanel = new JPanel();
        JButton dealButton = new JButton("DEAL");
        dealButton.setFont(new Font("Arial", Font.BOLD, 16));
        dealButton.setBackground(Color.GREEN);
        dealButton.setOpaque(true);
        dealButton.setBorderPainted(false); // Required for background color on Mac

        dealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeal();
            }
        });
        buttonPanel.add(dealButton);
        this.add(buttonPanel);

        // Center the window and make it visible
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Helper method to create chip buttons.
     * Adds the chip value to the current bet amount when clicked.
     *
     * @param panel The panel to add the button to.
     * @param value The value of the chip.
     */
    private void createChipButton(JPanel panel, int value) {
        JButton btn = new JButton("$" + value);
        btn.addActionListener(e -> {
            try {
                int currentBet = Integer.parseInt(betAmountField.getText());
                int newBet = currentBet + value;
                betAmountField.setText(String.valueOf(newBet));
            } catch (NumberFormatException ex) {
                // Reset to the chip value if the text field contains invalid input
                betAmountField.setText(String.valueOf(value));
            }
        });
        panel.add(btn);
    }

    /**
     * Handles the logic when the DEAL button is clicked.
     * Validates input and calls the controller.
     */
    private void handleDeal() {
        try {
            double amount = Double.parseDouble(betAmountField.getText());

            // Validate negative or zero bets locally (optional UI validation)
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount greater than 0!");
                return;
            }

            // Call the controller.
            // Note: We pass a negative amount because betting reduces the balance.
            controller.execute("Howard", -amount);

            JOptionPane.showMessageDialog(this, "Bet placed: $" + amount + "\n(See console for backend output)");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
    }
}
