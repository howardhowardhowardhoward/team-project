package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class Homepage extends JFrame {
    private final JButton startButton;
    private final JButton exitButton;

    public Homepage() {
        setTitle("Blackjack - Welcome");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- Integration Point 1: Set Window Icon (Taskbar Icon) ---
        URL iconUrl = getClass().getResource("/BlackjackLogo.png");
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        } else {
            System.err.println("Warning: Window icon /BlackjackLogo.png not found");
        }

        // Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw dark green gradient background
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 80, 0),
                        0, getHeight(), new Color(0, 40, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        // --- Integration Point 2: Main Cover Image ---
        // Try to load large logo, fallback to small logo, then text
        JLabel logoLabel;
        URL largeLogoUrl = getClass().getResource("/BlackjackLogoLarge.png");
        URL smallLogoUrl = getClass().getResource("/BlackjackLogo.png");

        ImageIcon mainIcon = null;
        if (largeLogoUrl != null) {
            mainIcon = new ImageIcon(largeLogoUrl);
        } else if (smallLogoUrl != null) {
            mainIcon = new ImageIcon(smallLogoUrl);
        }

        if (mainIcon != null) {
            // Scale image
            Image img = mainIcon.getImage();
            Image newImg = img.getScaledInstance(500, -1, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(newImg));
        } else {
            // Fallback text if image is missing
            logoLabel = new JLabel("BLACKJACK");
            logoLabel.setFont(new Font("Serif", Font.BOLD, 60));
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        startButton = createStyledButton("START GAME");
        exitButton = createStyledButton("EXIT");

        startButton.setBackground(Color.YELLOW);
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);

        exitButton.setBackground(Color.YELLOW);
        exitButton.setOpaque(true);
        exitButton.setBorderPainted(false);

        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);

        // Assemble Layout
        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(logoLabel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        backgroundPanel.add(buttonPanel);
        backgroundPanel.add(Box.createVerticalGlue());

        add(backgroundPanel);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(180, 50));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(218, 165, 32)); // GoldenRod
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        return btn;
    }

    public void addStartListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void addExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}