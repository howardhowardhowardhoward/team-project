package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import entities.Card;
import usecase.PlayerActions.*;
import usecase.DealerAction.DealerActionController;

public class GameView extends JPanel {

    private JLabel dealerLabel;
    private JLabel playerLabel;

    private JPanel dealerCardsPanel;
    private JPanel playerCardsPanel;

    private PlayerActionController playerController;
    private DealerActionController dealerController;

    public GameView(BlackjackGUI app) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Blackjack Table");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2,1));

        // Dealer section
        JPanel dealerPanel = new JPanel(new BorderLayout());
        dealerLabel = new JLabel("Dealer");
        dealerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dealerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        dealerCardsPanel = new JPanel();
        dealerPanel.add(dealerLabel, BorderLayout.NORTH);
        dealerPanel.add(dealerCardsPanel, BorderLayout.CENTER);

        // Player section
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerLabel = new JLabel("Player");
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        playerCardsPanel = new JPanel();
        playerPanel.add(playerLabel, BorderLayout.NORTH);
        playerPanel.add(playerCardsPanel, BorderLayout.CENTER);

        center.add(dealerPanel);
        center.add(playerPanel);

        add(center, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel();

        JButton hit = new JButton("HIT");
        hit.addActionListener(e -> playerController.hit(0));

        JButton stand = new JButton("STAND");
        stand.addActionListener(e -> playerController.stand(0));

        JButton split = new JButton("SPLIT");
        split.addActionListener(e -> playerController.split());

        JButton insurance = new JButton("INSURANCE");
        insurance.addActionListener(e -> playerController.insurance());

        buttons.add(hit);
        buttons.add(stand);
        buttons.add(split);
        buttons.add(insurance);

        add(buttons, BorderLayout.SOUTH);
    }

    public void injectControllers(PlayerActionController pc, DealerActionController dc) {
        this.playerController = pc;
        this.dealerController = dc;
    }

    public void updateDealerCards(List<Card> cards) {
        dealerCardsPanel.removeAll();
        for (Card c : cards) dealerCardsPanel.add(new JLabel(c.toString()));
        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();
    }

    public void updatePlayerCards(List<Card> cards) {
        playerCardsPanel.removeAll();
        for (Card c : cards) playerCardsPanel.add(new JLabel(c.toString()));
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    public void showDealerOutcome(int total, boolean bust, boolean blackjack) {
        if (bust) dealerLabel.setText("Dealer BUSTS (" + total + ")");
        else if (blackjack) dealerLabel.setText("Dealer BLACKJACK!");
        else dealerLabel.setText("Dealer Total: " + total);
    }
}