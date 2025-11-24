package gui;

import usecase.PlaceBetAndDeal.PlaceBetAndDealController;
import entities.Game;

import javax.swing.*;
import java.awt.*;

public class BetView extends JPanel {

    private double currentBet = 0;
    private JLabel betLabel;
    private JLabel balanceLabel;

    public BetView(BlackjackGUI app,
                   PlaceBetAndDealController dealController,
                   Game game) {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Place Your Bet");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        // CENTER PANEL
        JPanel center = new JPanel(new GridLayout(3, 1));

        balanceLabel = new JLabel("Balance: $" + game.getPlayer().getBalance());
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 22));

        betLabel = new JLabel("Current Bet: $0");
        betLabel.setHorizontalAlignment(SwingConstants.CENTER);
        betLabel.setFont(new Font("Arial", Font.BOLD, 26));

        center.add(balanceLabel);
        center.add(betLabel);

        // CHIP BUTTONS
        JPanel chips = new JPanel(new FlowLayout());
        int[] amounts = {1,5,25,50,100,500};

        for (int amt : amounts) {
            JButton chip = new JButton("$" + amt);
            chip.addActionListener(e -> {
                currentBet += amt;
                betLabel.setText("Current Bet: $" + currentBet);
            });
            chips.add(chip);
        }

        center.add(chips);
        add(center, BorderLayout.CENTER);

        // BOTTOM: CLEAR + DEAL
        JPanel bottom = new JPanel();

        JButton clear = new JButton("CLEAR");
        clear.addActionListener(e -> {
            currentBet = 0;
            betLabel.setText("Current Bet: $0");
        });

        JButton deal = new JButton("DEAL");
        deal.setFont(new Font("Arial", Font.BOLD, 20));
        deal.addActionListener(e -> {
            dealController.execute(currentBet);
            app.showGame();
        });

        bottom.add(clear);
        bottom.add(deal);

        add(bottom, BorderLayout.SOUTH);
    }
}
