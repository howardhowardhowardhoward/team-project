package frameworks_and_drivers;

import interface_adapters.*;
import usecase.PlaceBetAndDeal.*;
import entities.Deck;
import entities.Dealer;
import entities.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlayerActionScreen extends JFrame {

    private final PlayerActionViewModel playerViewModel;
    private final PlayerActionController playerController;
    private final DealerActionViewModel dealerViewModel;
    private final DealerActionController dealerController;


    private final JLabel playerTotal;
    private final JLabel dealerTotal;

    private final JLabel balanceLabel;
    private final JLabel betLabel;

    private final JPanel playerCardsPanel = new JPanel();
    private final JPanel dealerCardsPanel = new JPanel();
    private final JPanel actionPanel = new JPanel();
    private final JPanel scorePanel = new JPanel();

    private final JButton hitButton = new JButton("Hit");
    private final JButton standButton = new JButton("Stand");
    private final JButton doubleButton = new JButton("Double");
    private final JButton splitButton = new JButton("Split");
    private final JButton insuranceButton = new JButton("Insurance");
    private final JButton playAgainButton = new JButton("Play Again");

    private final String buttonFont = "Arial";

    public PlayerActionScreen(PlayerActionViewModel playerViewModel, PlayerActionController playerController,
                              DealerActionViewModel dealerViewModel, DealerActionController dealerController) {
        this.playerViewModel = playerViewModel;
        this.playerController = playerController;
        this.dealerViewModel = dealerViewModel;
        this.dealerController = dealerController;

        setTitle("Player Action");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 50, 0)
                        ,0, getHeight(), new Color(0, 100, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        playerCardsPanel.setOpaque(false);
        dealerCardsPanel.setOpaque(false);
        JPanel tablePanel = new JPanel(new GridLayout(2, 1));
        tablePanel.setOpaque(false);
        tablePanel.add(dealerCardsPanel);
        tablePanel.add(playerCardsPanel);
        backgroundPanel.add(tablePanel, BorderLayout.CENTER);

        actionPanel.setLayout(new GridLayout(1, 6, 10, 10));
        actionPanel.setOpaque(false);
        actionPanel.add(hitButton);
        actionPanel.add(standButton);
        actionPanel.add(doubleButton);
        actionPanel.add(splitButton);
        actionPanel.add(insuranceButton);
        actionPanel.add(playAgainButton);
        hitButton.addActionListener(e -> playerController.hit());
        standButton.addActionListener(e -> playerController.stand());
        doubleButton.addActionListener(e -> playerController.doubleDown());
        splitButton.addActionListener(e -> playerController.split());
        insuranceButton.addActionListener(e -> playerController.insurance());
        playAgainButton.addActionListener(e -> playAgain());
        playAgainButton.setEnabled(false);
        hitButton.setBackground(new Color(70, 130, 180));
        standButton.setBackground(new Color(205, 92, 92));
        doubleButton.setBackground(new Color(218, 165, 32));
        splitButton.setBackground(new Color(255, 140, 0));
        insuranceButton.setBackground(Color.GREEN);
        playAgainButton.setBackground(new Color(200, 200, 200));
        hitButton.setFont(new Font(buttonFont, Font.BOLD, 25));
        standButton.setFont(new Font(buttonFont, Font.BOLD, 25));
        doubleButton.setFont(new Font(buttonFont, Font.BOLD, 25));
        splitButton.setFont(new Font(buttonFont, Font.BOLD, 25));
        insuranceButton.setFont(new Font(buttonFont, Font.BOLD, 25));
        playAgainButton.setFont(new Font(buttonFont, Font.BOLD, 25));
        backgroundPanel.add(actionPanel, BorderLayout.SOUTH);

        scorePanel.setOpaque(false);
        scorePanel.setLayout(new GridLayout(2, 2));
        playerTotal = new JLabel("Player: " + playerViewModel.getPlayerTotal());
        playerTotal.setForeground(Color.white);
        playerTotal.setFont(new Font(buttonFont, Font.BOLD, 30));
        dealerTotal = new JLabel("Dealer: " + playerViewModel.getDealerVisibleTotal());
        dealerTotal.setForeground(Color.white);
        dealerTotal.setFont(new Font(buttonFont, Font.BOLD, 30));
        balanceLabel = new JLabel("Balance: $" + (int) playerViewModel.getBalance());
        balanceLabel.setForeground(Color.white);
        balanceLabel.setFont(new Font(buttonFont, Font.BOLD, 30));
        balanceLabel.setHorizontalAlignment(JLabel.RIGHT);
        betLabel = new JLabel("Bet: $" + (int) playerViewModel.getBetAmount());
        betLabel.setForeground(Color.white);
        betLabel.setFont(new Font(buttonFont, Font.BOLD, 30));
        betLabel.setHorizontalAlignment(JLabel.RIGHT);
        scorePanel.add(playerTotal);
        scorePanel.add(balanceLabel);
        scorePanel.add(dealerTotal);
        scorePanel.add(betLabel);
        backgroundPanel.add(scorePanel, BorderLayout.NORTH);

        add(backgroundPanel);

        playerViewModel.addPropertyChangeListener(e -> {
            switch (e.getPropertyName()) {
                case "playerCards" -> SwingUtilities.invokeLater(this::updatePlayerCards);
                case "playerTotal" -> SwingUtilities.invokeLater(() ->
                        playerTotal.setText("Player: " + playerViewModel.getPlayerTotal()));
                case "playerActionComplete" -> {
                    if (playerViewModel.isActionComplete()) {
                        JOptionPane.showMessageDialog(this, "Player action complete");
                        if (playerViewModel.isPlayerBust()) {
                            playerController.handleRoundResult();
                            enablePlayAgain();
                        }
                    }
                }
                case "roundMessage" -> {
                    JOptionPane.showMessageDialog(this, playerViewModel.getRoundMessage());
                }
                case "roundComplete" -> {
                    JOptionPane.showMessageDialog(this, "Round complete");
                    updateBalance();
                    updateBet();
                }
                case "error" -> {
                    JOptionPane.showMessageDialog(this, playerViewModel.getErrorMessage());
                }
            }
        });

        dealerViewModel.addPropertyChangeListener(e -> {
            switch (e.getPropertyName()) {
                case "dealerCards" -> SwingUtilities.invokeLater(this::updateDealerCards);
                case "dealerTotal" -> SwingUtilities.invokeLater(() ->
                        dealerTotal.setText("Dealer: " + dealerViewModel.getDealerTotal()));
                case "dealerActionComplete" -> {
                    if (dealerViewModel.isActionComplete()) {
                        JOptionPane.showMessageDialog(this, "Dealer turn complete");
                        playerController.handleRoundResult();
                        enablePlayAgain();
                    }
                }
                case "error" -> JOptionPane.showMessageDialog(this, dealerViewModel.getErrorMessage());
            }
        });
        updatePlayerCards();
        updateDealerCards();

        setVisible(true);
    }

    private void updatePlayerCards() {
        playerCardsPanel.removeAll();
        List<String> urls = playerViewModel.getPlayerCardImages();
        for (String url : urls) {
            playerCardsPanel.add(createCardLabel(url));
        }
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    private void updateDealerCards() {
        dealerCardsPanel.removeAll();
        List<String> urls = dealerViewModel.getDealerCardImages();
        for (String url : urls) {
            dealerCardsPanel.add(createCardLabel(url));
        }
        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();
    }

    private JLabel createCardLabel(String url) {
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(url));
            Image image = icon.getImage().getScaledInstance(120, 175, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(image));
        } catch (Exception e) {
            return new JLabel("Error loading card");
        }
    }

    private void updateBalance() {
        balanceLabel.setText("Balance: $" + (int) playerViewModel.getBalance());
    }

    private void updateBet() {
        betLabel.setText("Bet: $" + (int) playerViewModel.getBetAmount());
    }

    private void playAgain() {
        PlaceBetAndDealViewModel dealViewModel = new PlaceBetAndDealViewModel();
        PlaceBetAndDealPresenter dealPresenter = new PlaceBetAndDealPresenter(dealViewModel);
        Deck deck = playerController.getDeck();
        Player player = playerController.getPlayer();
        player.getHands().clear();
        Dealer dealer =  playerController.getDealer();
        dealer.getHand().clear();
        PlaceBetAndDealDataAccess dealDataAccess = new PlaceBetAndDealDataAccess(player, deck, dealer);
        PlaceBetAndDealInteractor dealInteractor = new PlaceBetAndDealInteractor(
                dealDataAccess, dealPresenter
        );
        PlaceBetAndDealController dealController = new PlaceBetAndDealController(dealInteractor);
        dealViewModel.setBalance(playerViewModel.getBalance());
        dealViewModel.setBetAmount(playerViewModel.getBetAmount());
        dispose();
        new BetScreen(dealController, dealViewModel);
    }

    private void enablePlayAgain() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);
        insuranceButton.setEnabled(false);
        playAgainButton.setEnabled(true);
    }
}
