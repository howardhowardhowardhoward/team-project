package frameworks_and_drivers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import frameworks_and_drivers.loadsavegame.LoadGameDataAccess;
// Import your ExitRestart classes
import frameworks_and_drivers.exitrestartgame.ExitRestartGame;
import interface_adapters.exitrestartgame.ExitRestartGameController;
import interface_adapters.exitrestartgame.ExitRestartGamePresenter;
import usecase.exitrestartgame.ExitRestartGameInputData;
import usecase.exitrestartgame.ExitRestartGameInteractor;

import interface_adapters.dealeraction.DealerActionController;
import interface_adapters.dealeraction.DealerActionPresenter;
import interface_adapters.dealeraction.DealerActionViewModel;
import interface_adapters.placebetanddeal.PlaceBetAndDealController;
import usecase.placebetanddeal.PlaceBetAndDealInputData;
import interface_adapters.placebetanddeal.PlaceBetAndDealViewModel;
import interface_adapters.playeraction.PlayerActionController;
import usecase.playeraction.PlayerActionInteractor;
import interface_adapters.playeraction.PlayerActionPresenter;
import interface_adapters.playeraction.PlayerActionViewModel;
import usecase.dealeraction.*;

public class BetScreen extends JFrame implements ActionListener {
    private final PlaceBetAndDealController controller;
    private final PlaceBetAndDealViewModel viewModel;
    
    // Add reference to your ExitRestartGameController
    private final ExitRestartGameController exitRestartController;

    private final JLabel balanceLabel;
    private final JLabel betLabel;

    private final JButton dealButton;
    private final JButton clearButton;
    private final JButton allInButton;
    private final JButton saveButton;
    private final JButton restartButton;
    private final JButton exitButton;

    private final JPanel playerCardsPanel = new JPanel();
    private final JPanel dealerCardsPanel = new JPanel();

    public BetScreen(PlaceBetAndDealController controller, PlaceBetAndDealViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        // --- Wiring up the Exit/Restart Use Case locally ---
        // This ensures the Interactor is used and linked to the UI.
        ExitRestartGame exitDataAccess = new ExitRestartGame(
            controller.getPlayer(), 
            controller.getDeck(), 
            controller.getDealer()
        );
        ExitRestartGamePresenter exitPresenter = new ExitRestartGamePresenter(viewModel);
        ExitRestartGameInteractor exitInteractor = new ExitRestartGameInteractor(exitPresenter, exitDataAccess);
        this.exitRestartController = new ExitRestartGameController(exitInteractor);
        // ---------------------------------------------------

        setTitle("Game Screen");
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

        backgroundPanel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new GridLayout(2, 1));

        balanceLabel = new JLabel("Balance: $1000");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        balanceLabel.setForeground(Color.white);
        balanceLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(balanceLabel);

        betLabel = new JLabel("Bet: $0");
        betLabel.setFont(new Font("Arial", Font.BOLD, 20));
        betLabel.setForeground(Color.white);
        betLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(betLabel);

        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        playerCardsPanel.setOpaque(false);
        dealerCardsPanel.setOpaque(false);
        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        tablePanel.setLayout(new GridLayout(2, 1));
        tablePanel.add(dealerCardsPanel);
        tablePanel.add(playerCardsPanel);

        backgroundPanel.add(tablePanel, BorderLayout.CENTER);

        // chips
        JPanel chipsPanel = new JPanel();
        chipsPanel.setOpaque(false);
        chipsPanel.setLayout(new GridLayout(4, 2, 10, 10));

        chipsPanel.add(createChipButton("$1", 1));
        chipsPanel.add(createChipButton("$5", 5));
        chipsPanel.add(createChipButton("$10", 10));
        chipsPanel.add(createChipButton("$25", 25));
        chipsPanel.add(createChipButton("$50", 50));
        chipsPanel.add(createChipButton("$100", 100));
        chipsPanel.add(createChipButton("$500", 500));
        chipsPanel.add(createChipButton("$1,000", 1000));

        // buttons
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setOpaque(false);
        bottomButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        dealButton = new JButton("Deal");
        dealButton.setFont(new Font("Arial", Font.BOLD, 20));
        dealButton.addActionListener(this);
        dealButton.setBackground(Color.BLUE);
        bottomButtonPanel.add(dealButton);

        allInButton = new JButton("All In");
        allInButton.setFont(new Font("Arial", Font.BOLD, 20));
        allInButton.addActionListener(e -> controller.allIn());
        allInButton.setBackground(new Color(220, 20, 60));
        bottomButtonPanel.add(allInButton);

        clearButton = new JButton("Clear Bet");
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));
        clearButton.addActionListener(e -> controller.clearBet());
        clearButton.setBackground(Color.GRAY);
        bottomButtonPanel.add(clearButton);

        saveButton = new JButton("Save Game");
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.addActionListener(e -> saveGame());
        saveButton.setBackground(Color.GREEN);
        bottomButtonPanel.add(saveButton);

        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        // Use YOUR controller here
        restartButton.addActionListener(e -> performRestart());
        restartButton.setBackground(Color.GRAY);
        bottomButtonPanel.add(restartButton);

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        // Use YOUR controller here
        exitButton.addActionListener(e -> performExit());
        exitButton.setBackground(Color.RED);
        bottomButtonPanel.add(exitButton);

        backgroundPanel.add(chipsPanel, BorderLayout.WEST);
        backgroundPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

        add(backgroundPanel);

        viewModel.addPropertyChangeListener(e -> {
            if ("cardsDealt".equals(e.getPropertyName())) {
                SwingUtilities.invokeLater(this::updateCardsOnScreen);
            }
            if ("balance".equals(e.getPropertyName())) {
                SwingUtilities.invokeLater(this::updateBalance);
            }
            if ("bet".equals(e.getPropertyName())) {
                SwingUtilities.invokeLater(this::updateBet);
            }
            if ("error".equals(e.getPropertyName())) {
                JOptionPane.showMessageDialog(this, "Insufficient balance");
            }
        });
        updateBalance();
        updateBet();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (viewModel.getBetAmount() <= 0) {
            JOptionPane.showMessageDialog(this, "Please place a bet first.");
            return;
        }
        PlaceBetAndDealInputData inputData = new PlaceBetAndDealInputData(viewModel.getBetAmount());
        controller.execute(inputData);

        DealerActionViewModel daViewModel = new DealerActionViewModel();
        DealerActionPresenter daPresenter = new DealerActionPresenter(daViewModel);
        DealerActionInteractor daInteractor = new DealerActionInteractor(controller.getDeck(),
                controller.getPlayer(), controller.getDealer(), daPresenter);
        DealerActionController daController = new DealerActionController(daInteractor);


        PlayerActionViewModel paViewModel = new PlayerActionViewModel();
        paViewModel.setPlayerCardImages(viewModel.getPlayerCardImages());
        paViewModel.setDealerCardImages(viewModel.getDealerCardImages());
        paViewModel.setPlayerTotal(viewModel.getPlayerTotal());
        paViewModel.setDealerVisibleTotal(viewModel.getDealerVisibleTotal());
        paViewModel.setBalance(viewModel.getBalance());
        paViewModel.setBetAmount(viewModel.getBetAmount());
        daViewModel.setPlayerCardImages(viewModel.getPlayerCardImages());
        daViewModel.setDealerCardImages(viewModel.getDealerCardImages());
        daViewModel.setPlayerTotal(viewModel.getPlayerTotal());
        daViewModel.setDealerTotal(viewModel.getDealerVisibleTotal());
        PlayerActionPresenter paPresenter = new PlayerActionPresenter(paViewModel);
        PlayerActionInteractor paInteractor = new PlayerActionInteractor(controller.getDeck(),
                controller.getPlayer(), controller.getDealer(), paPresenter,
                daInteractor);
        PlayerActionController paController = new PlayerActionController(paInteractor);

        dispose();
        new PlayerActionScreen(paViewModel, paController, daViewModel, daController);
    }

    private JButton createChipButton(String label, int value) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.addActionListener(e -> controller.addChip(value));
        if (value == 1) button.setBackground(Color.WHITE);
        else if (value == 5) button.setBackground(Color.RED);
        else if (value == 10) button.setBackground(new Color(1, 150, 255));
        else if (value == 25) button.setBackground(Color.GREEN);
        else if (value == 50) button.setBackground(new Color(255, 140, 0));
        else if (value == 100) {
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
        }
        else if (value == 500) {
            button.setBackground(new Color(75, 0, 100));
            button.setForeground(Color.WHITE);
        }
        else button.setBackground(new Color(218, 165, 32));
        return button;
    }

    private void updateBalance() {
        balanceLabel.setText("Balance: $" + (int) viewModel.getBalance());
        checkAutoRestart();
    }

    private void updateBet() {
        int bet = (int) viewModel.getBetAmount();
        betLabel.setText("Bet: $" + bet);
        saveButton.setEnabled(bet == 0);
    }

    private void updateCardsOnScreen() {
        playerCardsPanel.removeAll();
        dealerCardsPanel.removeAll();
        for (String url : viewModel.getPlayerCardImages()) {
            playerCardsPanel.add(createCardImageLabel(url));
        }
        for (String url : viewModel.getDealerCardImages()) {
            dealerCardsPanel.add(createCardImageLabel(url));
        }
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();
    }

    private JLabel createCardImageLabel(String url) {
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(url));
            Image image = icon.getImage().getScaledInstance(120, 175, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(image));
        } catch (Exception e) {
            return new JLabel("Error loading card image");
        }
    }

    private void saveGame() {
        double currentBalance = viewModel.getBalance();
        LoadGameDataAccess dataAccess = new LoadGameDataAccess();
        dataAccess.saveBalance(currentBalance);
        JOptionPane.showMessageDialog(this,
                "Game saved! Current balance: $" + (int) currentBalance);
    }

    // --- New Method using ExitRestartGameController ---
    private void performRestart() {
        ExitRestartGameInputData inputData = new ExitRestartGameInputData();
        exitRestartController.handleRestartGame(inputData);
        // The View (this screen) updates via ViewModel property changes, 
        // OR we can show a confirmation here if the presenter/viewmodel wiring isn't updating immediate UI
        JOptionPane.showMessageDialog(this, "Game restarted! Balance: $1,000");
    }

    // --- New Method using ExitRestartGameController ---
    private void performExit() {
        ExitRestartGameInputData inputData = new ExitRestartGameInputData();
        exitRestartController.handleExitGame(inputData);
        System.exit(0);
    }

    private void checkAutoRestart() {
        int balance = (int) viewModel.getBalance();
        int bet =  (int) viewModel.getBetAmount();

        if (balance == 0 && bet == 0) {
            JOptionPane.showMessageDialog(this, "You lost all your money!\n" +
                    "Please don't go to a casino in real life. Restarting with $1,000...");
            performRestart(); // Use the new controller here too
        }
    }
}
