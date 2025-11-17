package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Homepage extends JFrame {
    public Homepage() {
        setTitle("gui.Homepage");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
        backgroundPanel.setLayout(new GridBagLayout());

        // Blackjack logo
        ImageIcon blackjackIcon = new ImageIcon("BlackjackLogo.png");
        ImageIcon blackjackIconLarge = new ImageIcon("BlackjackLogoLarge.png");
        Image scaledImage = blackjackIcon.getImage().getScaledInstance(getWidth() - 350, getHeight() - 350
                , Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        add(backgroundPanel);
        backgroundPanel.add(imageLabel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Homepage();
    }
}
