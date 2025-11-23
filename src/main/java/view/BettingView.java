package view;

import interface_adapter.update_balance.UpdateBalanceController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BettingView extends JFrame {

    private final UpdateBalanceController controller;
    private final JLabel balanceLabel;
    private final JTextField betAmountField;

    public BettingView(UpdateBalanceController controller) {
        this.controller = controller;

        this.setTitle("Blackjack - Place Your Bet");
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JPanel logoPanel = new JPanel();
        java.net.URL imgUrl = getClass().getResource("/BlackjackLogo.png");
        if (imgUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imgUrl);
            Image image = originalIcon.getImage();
            Image newimg = image.getScaledInstance(300, 150,  java.awt.Image.SCALE_SMOOTH);
            logoPanel.add(new JLabel(new ImageIcon(newimg)));
        } else {
            System.out.println("Error: Logo image not found in resources!");
            logoPanel.add(new JLabel("BlackJack Game"));
        }
        this.add(logoPanel);

        JPanel balancePanel = new JPanel();
        balanceLabel = new JLabel("Current Balance: $1000");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balancePanel.add(balanceLabel);
        this.add(balancePanel);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Bet Amount:"));
        betAmountField = new JTextField(10);
        betAmountField.setText("0");
        inputPanel.add(betAmountField);
        this.add(inputPanel);

        JPanel chipsPanel = new JPanel();
        createChipButton(chipsPanel, 1);
        createChipButton(chipsPanel, 5);
        createChipButton(chipsPanel, 25);
        createChipButton(chipsPanel, 100);
        this.add(chipsPanel);

        // --- 5. DEAL 按钮 ---
        JPanel buttonPanel = new JPanel();
        JButton dealButton = new JButton("DEAL");
        dealButton.setFont(new Font("Arial", Font.BOLD, 16));
        dealButton.setBackground(Color.GREEN);
        dealButton.setOpaque(true);
        dealButton.setBorderPainted(false);

        dealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeal();
            }
        });
        buttonPanel.add(dealButton);
        this.add(buttonPanel);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void createChipButton(JPanel panel, int value) {
        JButton btn = new JButton("$" + value);
        btn.addActionListener(e -> {
            try {
                int currentBet = Integer.parseInt(betAmountField.getText());
                int newBet = currentBet + value;
                betAmountField.setText(String.valueOf(newBet));
            } catch (NumberFormatException ex) {
                betAmountField.setText(String.valueOf(value));
            }
        });
        panel.add(btn);
    }

    private void handleDeal() {
        try {
            double amount = Double.parseDouble(betAmountField.getText());
            controller.execute("Howard", -amount);
            JOptionPane.showMessageDialog(this, "Bet placed: $" + amount + "\n(Check console for logic output)");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
    }
}