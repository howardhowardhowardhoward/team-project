package gui;

import javax.swing.*;
import java.awt.*;

public class StartView extends JPanel {

    public StartView(BlackjackGUI app) {
        setLayout(new BorderLayout());

        // Load logo
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setIcon(new ImageIcon(getClass().getResource("/BlackjackLogoLarge.png")));
        add(logo, BorderLayout.CENTER);

        JButton start = new JButton("START GAME");
        start.setFont(new Font("Arial", Font.BOLD, 22));
        start.addActionListener(e -> app.showBet());

        JPanel bottom = new JPanel();
        bottom.add(start);

        add(bottom, BorderLayout.SOUTH);
    }
}