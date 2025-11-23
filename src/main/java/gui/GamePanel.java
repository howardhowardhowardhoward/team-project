package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import entities.Card;

public class GamePanel extends JFrame {
    // UI Components
    private final JLabel dealerScoreLabel;
    private final JPanel dealerCardsPanel;
    private final JLabel playerScoreLabel;
    private final JPanel playerCardsPanel;
    private final JLabel messageLabel;
    private final JLabel balanceLabel;
    private final JLabel betLabel;

    // Buttons
    private final JButton hitButton;
    private final JButton standButton;
    private final JButton doubleButton;
    private final JButton splitButton;
    private final JButton betButton;
    private final JButton exitButton;
    private final JButton restartButton; // --- Added Restart Button ---
    private final JTextField betField;

    public GamePanel() {
        setTitle("Blackjack - Game Table");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Set Window Icon ---
        URL iconUrl = getClass().getResource("/BlackjackLogo.png");
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        }

        // --- Top: Dealer Area ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 80, 0)); // Dark Green
        topPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Top left small Logo
        JPanel dealerInfoBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dealerInfoBox.setOpaque(false);
        
        JLabel smallLogoLabel;
        if (iconUrl != null) {
            Image smallImg = new ImageIcon(iconUrl).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            smallLogoLabel = new JLabel(new ImageIcon(smallImg));
        } else {
            smallLogoLabel = new JLabel("[BJ]"); // Fallback
            smallLogoLabel.setForeground(Color.YELLOW);
        }
        
        dealerScoreLabel = new JLabel("  Dealer: 0");
        dealerScoreLabel.setForeground(Color.WHITE);
        dealerScoreLabel.setFont(new Font("Arial", Font.BOLD, 22));

        dealerInfoBox.add(smallLogoLabel);
        dealerInfoBox.add(dealerScoreLabel);
        
        dealerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        dealerCardsPanel.setOpaque(false);

        topPanel.add(dealerInfoBox, BorderLayout.WEST);
        topPanel.add(dealerCardsPanel, BorderLayout.CENTER);
        topPanel.add(Box.createHorizontalStrut(100), BorderLayout.EAST); // Right spacer

        // --- Center: Message Area ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(0, 100, 0)); // Lighter green
        messageLabel = new JLabel("Welcome! Place your bet to start.");
        messageLabel.setForeground(new Color(255, 223, 0)); // Bright Gold
        messageLabel.setFont(new Font("Arial", Font.BOLD, 28));
        centerPanel.add(messageLabel);

        // --- Bottom: Player Area & Controls ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0, 80, 0));
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Player Cards
        JPanel playerInfoPanel = new JPanel(new BorderLayout());
        playerInfoPanel.setOpaque(false);
        
        playerScoreLabel = new JLabel("Player: 0");
        playerScoreLabel.setForeground(Color.WHITE);
        playerScoreLabel.setFont(new Font("Arial", Font.BOLD, 22));
        playerScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerScoreLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        playerCardsPanel.setOpaque(false);

        playerInfoPanel.add(playerScoreLabel, BorderLayout.NORTH);
        playerInfoPanel.add(playerCardsPanel, BorderLayout.CENTER);

        // Control Bar
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlsPanel.setOpaque(false);
        controlsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        balanceLabel = new JLabel("Balance: $1000");
        balanceLabel.setForeground(Color.CYAN);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));

        betLabel = new JLabel("Bet:");
        betLabel.setForeground(Color.WHITE);
        betLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        betField = new JTextField("50", 5);
        betField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Create buttons
        betButton = createGameButton("Deal", new Color(34, 139, 34)); // Forest Green
        hitButton = createGameButton("Hit", new Color(70, 130, 180)); // Steel Blue
        standButton = createGameButton("Stand", new Color(205, 92, 92)); // Indian Red
        doubleButton = createGameButton("Double", new Color(218, 165, 32)); // GoldenRod
        splitButton = createGameButton("Split", new Color(255, 140, 0)); // Dark Orange
        
        // --- Added Restart Button (to reset balance) ---
        restartButton = createGameButton("Restart", new Color(147, 112, 219)); // Medium Purple
        
        exitButton = createGameButton("Exit", Color.GRAY);

        // Initially disable game buttons
        enableGameButtons(false);

        // Add components to panel
        controlsPanel.add(balanceLabel);
        controlsPanel.add(Box.createHorizontalStrut(20));
        controlsPanel.add(betLabel);
        controlsPanel.add(betField);
        controlsPanel.add(betButton);
        controlsPanel.add(Box.createHorizontalStrut(30));
        controlsPanel.add(hitButton);
        controlsPanel.add(standButton);
        controlsPanel.add(doubleButton);
        controlsPanel.add(splitButton);
        controlsPanel.add(Box.createHorizontalStrut(30));
        controlsPanel.add(restartButton); // Add Restart
        controlsPanel.add(exitButton);

        bottomPanel.add(playerInfoPanel, BorderLayout.CENTER);
        bottomPanel.add(controlsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createGameButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    // --- UI Update Methods ---

    public void updateDealerCards(List<Card> cards, int score) {
        dealerCardsPanel.removeAll();
        for (Card card : cards) {
            dealerCardsPanel.add(createCardLabel(card.toString()));
        }
        dealerScoreLabel.setText("  Dealer: " + score);
        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();
    }

    public void updatePlayerCards(List<Card> cards, int score) {
        playerCardsPanel.removeAll();
        for (Card card : cards) {
            playerCardsPanel.add(createCardLabel(card.toString()));
        }
        playerScoreLabel.setText("Player: " + score);
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    public void updateMessage(String msg) {
        messageLabel.setText(msg);
    }

    public void updateBalance(double balance) {
        balanceLabel.setText(String.format("Balance: $%.2f", balance));
    }

    public void enableGameButtons(boolean enabled) {
        hitButton.setEnabled(enabled);
        standButton.setEnabled(enabled);
        doubleButton.setEnabled(enabled);
        splitButton.setEnabled(enabled); 
        betButton.setEnabled(!enabled);
        betField.setEnabled(!enabled);
        // Restart and Exit always available, or available during betting phase
        restartButton.setEnabled(!enabled); 
    }

    public String getBetAmount() {
        return betField.getText();
    }

    // Helper method: Create styled card label
    private JLabel createCardLabel(String text) {
        JLabel label = new JLabel("<html><center>" + text.replace("\n", "<br>") + "</center></html>");
        label.setFont(new Font("Dialog", Font.BOLD, 20));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        
        // Set color based on suit
        if (text.contains("♥") || text.contains("♦") || text.contains("HEARTS") || text.contains("DIAMONDS")) {
            label.setForeground(Color.RED);
        } else {
            label.setForeground(Color.BLACK);
        }
        
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true)); 
        label.setPreferredSize(new Dimension(80, 110));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    // --- Listener Bindings ---
    public void addBetListener(ActionListener l) { betButton.addActionListener(l); }
    public void addHitListener(ActionListener l) { hitButton.addActionListener(l); }
    public void addStandListener(ActionListener l) { standButton.addActionListener(l); }
    public void addDoubleListener(ActionListener l) { doubleButton.addActionListener(l); }
    public void addSplitListener(ActionListener l) { splitButton.addActionListener(l); }
    public void addExitListener(ActionListener l) { exitButton.addActionListener(l); }
    public void addRestartListener(ActionListener l) { restartButton.addActionListener(l); }
}