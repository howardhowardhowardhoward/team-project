package frameworks_and_drivers;

import javax.swing.*;
import java.awt.*;

import frameworks_and_drivers.placebetanddeal.PlaceBetAndDealDataAccess;
import interface_adapters.loadsavegame.LoadGameController;
import interface_adapters.loadsavegame.LoadGameViewModel;
import interface_adapters.placebetanddeal.PlaceBetAndDealController;
import interface_adapters.placebetanddeal.PlaceBetAndDealPresenter;
import interface_adapters.placebetanddeal.PlaceBetAndDealViewModel;
import interface_adapters.startgame.StartGameController;
import interface_adapters.startgame.StartGameViewModel;
import usecase.placebetanddeal.*;
import usecase.loadsavegame.*;
import entities.*;

public class Homepage extends JFrame {
    private final StartGameController startGameController;
    private final StartGameViewModel startGameViewModel;
    private final LoadGameController loadGameController;
    private final LoadGameViewModel loadGameViewModel;
    private final LoadGameDataAccessInterface loadGameDataAccess;

    public Homepage(StartGameController startGameController, StartGameViewModel startGameViewModel,
                    LoadGameController loadGameController, LoadGameViewModel loadGameViewModel,
                    LoadGameDataAccessInterface loadGameDataAccess) {
        this.startGameController = startGameController;
        this.startGameViewModel = startGameViewModel;
        this.loadGameController = loadGameController;
        this.loadGameViewModel = loadGameViewModel;
        this.loadGameDataAccess = loadGameDataAccess;

        setTitle("Homepage");
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
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;


        // Blackjack logo
        ImageIcon blackjackIcon = new ImageIcon("BlackjackLogoDesign.png");
        Image scaledImage = blackjackIcon.getImage().getScaledInstance(600, 300
                , Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundPanel.add(imageLabel, c);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));

        // Start button
        JButton startButton = new JButton("Start New Game");
        startButton.setBackground(new Color(218, 165, 32));
        startButton.setForeground(Color.BLACK);
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        buttonPanel.add(startButton);
        startButton.addActionListener(e -> startNewGame());

        // Load saved game button
        JButton loadButton = new JButton("Load Saved Game");
        loadButton.setBackground(new Color(218, 165, 32));
        loadButton.setForeground(Color.BLACK);
        loadButton.setFont(new Font("Arial", Font.BOLD, 20));
        buttonPanel.add(loadButton);
        loadButton.addActionListener(e -> loadSavedGame());

        c.gridy = 5;
        backgroundPanel.add(buttonPanel, c);

        add(backgroundPanel);

        loadGameViewModel.addPropertyChangeListener(e -> {
            switch (e.getPropertyName()) {
                case "message" -> JOptionPane.showMessageDialog(this, loadGameViewModel.getMessage());
            }
        });

        setVisible(true);
    }

    private void startNewGame() {
        // Overwrite the current saved game with starting balance = 1000
        loadGameDataAccess.saveBalance(1000);

        startGameController.startGame();
        PlaceBetAndDealViewModel betViewModel = new PlaceBetAndDealViewModel();
        PlaceBetAndDealPresenter betPresenter = new PlaceBetAndDealPresenter(betViewModel);
        Deck deck = startGameController.getDeck();
        Player player = startGameController.getPlayer();
        Dealer dealer = startGameController.getDealer();
        PlaceBetAndDealDataAccess betDataAccess = new PlaceBetAndDealDataAccess(player, deck, dealer);
        PlaceBetAndDealInteractor betInteractor = new PlaceBetAndDealInteractor(
                betDataAccess, betPresenter
        );
        PlaceBetAndDealController betController = new PlaceBetAndDealController(betInteractor);
        betPresenter.presentBetUpdated(player.getBalance(), 0);
        dispose();
        new BetScreen(betController, betViewModel);
    }

    private void loadSavedGame() {
        Game savedGame = loadGameController.loadGame();

        PlaceBetAndDealViewModel betViewModel = new PlaceBetAndDealViewModel();
        PlaceBetAndDealPresenter betPresenter = new PlaceBetAndDealPresenter(betViewModel);
        Deck deck = savedGame.getDeck();
        Player player = savedGame.getPlayer();
        Dealer dealer = savedGame.getDealer();
        PlaceBetAndDealDataAccess betDataAccess = new PlaceBetAndDealDataAccess(player, deck, dealer);
        PlaceBetAndDealInteractor betInteractor = new PlaceBetAndDealInteractor(
                betDataAccess, betPresenter
        );
        PlaceBetAndDealController betController = new PlaceBetAndDealController(betInteractor);
        betPresenter.presentBetUpdated(player.getBalance(), 0);
        dispose();
        new BetScreen(betController, betViewModel);
    }
}
