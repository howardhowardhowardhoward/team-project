package gui;

import entities.Card;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel implements GameView {

    private JLabel playerHandLabel = new JLabel("Player: ");
    private JLabel dealerHandLabel = new JLabel("Dealer: ");
    private JLabel balanceLabel = new JLabel("Balance: ");
    private JLabel messageLabel = new JLabel("Welcome!");

    public JButton bet5 = new JButton("$5");
    public JButton bet10 = new JButton("$10");
    public JButton bet20 = new JButton("$20");
    public JButton bet50 = new JButton("$50");
    public JButton bet100 = new JButton("$100");
    public JButton bet500 = new JButton("$500");

    public JButton startRound = new JButton("Deal");

    public JButton hit = new JButton("Hit");
    public JButton stand = new JButton("Stand");
    public JButton doubleDown = new JButton("Double");
    public JButton split = new JButton("Split");

    public GamePanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(3, 1));
        top.add(playerHandLabel);
        top.add(dealerHandLabel);
        top.add(balanceLabel);

        JPanel chips = new JPanel();
        chips.add(bet5);
        chips.add(bet10);
        chips.add(bet20);
        chips.add(bet50);
        chips.add(bet100);
        chips.add(bet500);
        chips.add(startRound);

        JPanel actions = new JPanel();
        actions.add(hit);
        actions.add(stand);
        actions.add(doubleDown);
        actions.add(split);

        add(top, BorderLayout.NORTH);
        add(chips, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        add(messageLabel, BorderLayout.PAGE_END);

        setActionButtonsEnabled(false, false, false, false);
    }

    @Override
    public void showMessage(String msg) {
        messageLabel.setText(msg);
    }

    @Override
    public void updatePlayerHand(List<Card> cards, int total) {
        playerHandLabel.setText("Player: " + cards + "  Total: " + total);
    }

    @Override
    public void updateDealerHand(List<Card> cards, int visibleTotal) {
        dealerHandLabel.setText("Dealer: " + cards + "  Shown Total: " + visibleTotal);
    }

    @Override
    public void updateBalance(double balance) {
        balanceLabel.setText("Balance: $" + balance);
    }

    @Override
    public void setActionButtonsEnabled(boolean hitEnabled, boolean standEnabled, boolean splitEnabled, boolean doubleEnabled) {
        hit.setEnabled(hitEnabled);
        stand.setEnabled(standEnabled);
        split.setEnabled(splitEnabled);
        doubleDown.setEnabled(doubleEnabled);
    }
}

