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
    private final JButton betButton; // Deal button
    private final JButton leaveButton; // Changed name to "Leave" for clarity
    private final JButton restartButton;
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

        // Top Left: Small Logo and Dealer Score
        JPanel dealerInfoBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dealerInfoBox.setOpaque(false);
        
        JLabel smallLogoLabel;
        if (iconUrl != null) {
            Image smallImg = new ImageIcon(iconUrl).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            smallLogoLabel = new JLabel(new ImageIcon(smallImg));
        } else {
            smallLogoLabel = new JLabel("[BJ]");
            smallLogoLabel.setForeground(Color.YELLOW);
        }
        
        dealerScoreLabel = new JLabel("  Dealer"); 
        dealerScoreLabel.setForeground(Color.WHITE);
        dealerScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));

        dealerInfoBox.add(smallLogoLabel);
        dealerInfoBox.add(dealerScoreLabel);
        
        dealerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        dealerCardsPanel.setOpaque(false);

        topPanel.add(dealerInfoBox, BorderLayout.WEST);
        topPanel.add(dealerCardsPanel, BorderLayout.CENTER);
        topPanel.add(Box.createHorizontalStrut(100), BorderLayout.EAST); 

        // --- Center: Message Area ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(0, 100, 0)); 
        messageLabel = new JLabel("Welcome! Place your bet to start.");
        messageLabel.setForeground(new Color(255, 223, 0)); // Gold text
        messageLabel.setFont(new Font("Arial", Font.BOLD, 32));
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
        playerScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        playerScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerScoreLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

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
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));

        betLabel = new JLabel("Bet:");
        betLabel.setForeground(Color.WHITE);
        betLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        betField = new JTextField("50", 5);
        betField.setFont(new Font("Arial", Font.PLAIN, 18));
        
        // Create Buttons
        betButton = createGameButton("Deal", new Color(34, 139, 34)); // Green
        hitButton = createGameButton("Hit", new Color(70, 130, 180)); // Blue
        standButton = createGameButton("Stand", new Color(205, 92, 92)); // Red
        doubleButton = createGameButton("Double", new Color(218, 165, 32)); // Gold
        splitButton = createGameButton("Split", new Color(255, 140, 0)); // Orange
        
        restartButton = createGameButton("Restart", new Color(147, 112, 219)); // Purple
        leaveButton = createGameButton("Leave", Color.GRAY); // "Leave" button to go back to menu

        // Initially disable game buttons
        enableGameButtons(false);

        controlsPanel.add(balanceLabel);
        controlsPanel.add(Box.createHorizontalStrut(20));
        controlsPanel.add(betLabel);
        controlsPanel.add(betField);
        controlsPanel.add(betButton);
        controlsPanel.add(Box.createHorizontalStrut(20));
        controlsPanel.add(hitButton);
        controlsPanel.add(standButton);
        controlsPanel.add(doubleButton);
        controlsPanel.add(splitButton);
        controlsPanel.add(Box.createHorizontalStrut(20));
        controlsPanel.add(restartButton);
        controlsPanel.add(leaveButton); // Added Leave button here

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
        
        // Fix for Mac/Linux transparency issues
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(90, 40));
        return btn;
    }

    // --- UI Update Methods ---

    public void updateDealerCards(List<Card> cards, int score) {
        dealerCardsPanel.removeAll();
        boolean hasHidden = false;
        for (Card card : cards) {
            if (card == null) {
                dealerCardsPanel.add(createCardLabel(null)); // Hidden
                hasHidden = true;
            } else {
                dealerCardsPanel.add(createCardLabel(card.toString()));
            }
        }
        if (hasHidden) {
            dealerScoreLabel.setText("Dealer: ?");
        } else {
            dealerScoreLabel.setText("Dealer: " + score); 
        }
        
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
        // Disable bet button while game is active
        betButton.setEnabled(!enabled);
        betField.setEnabled(!enabled);
        // Restart and Leave are always active
    }

    public String getBetAmount() {
        return betField.getText();
    }

    private JLabel createCardLabel(String text) {
        JLabel label;
        if (text == null) {
            // Card Back
            label = new JLabel("?");
            label.setOpaque(true);
            label.setBackground(new Color(139, 0, 0));
            label.setForeground(Color.WHITE);
            label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        } else {
            // Card Face
            label = new JLabel("<html><center>" + text.replace("\n", "<br>") + "</center></html>");
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            if (text.contains("♥") || text.contains("♦") || text.contains("Hearts") || text.contains("Diamonds")) {
                label.setForeground(Color.RED);
            } else {
                label.setForeground(Color.BLACK);
            }
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
        label.setFont(new Font("Dialog", Font.BOLD, 22));
        label.setPreferredSize(new Dimension(90, 130));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    // --- Listeners ---
    public void addBetListener(ActionListener l) { betButton.addActionListener(l); }
    public void addHitListener(ActionListener l) { hitButton.addActionListener(l); }
    public void addStandListener(ActionListener l) { standButton.addActionListener(l); }
    public void addDoubleListener(ActionListener l) { doubleButton.addActionListener(l); }
    public void addSplitListener(ActionListener l) { splitButton.addActionListener(l); }
    public void addLeaveListener(ActionListener l) { leaveButton.addActionListener(l); } // Changed to Leave
    public void addRestartListener(ActionListener l) { restartButton.addActionListener(l); }
}