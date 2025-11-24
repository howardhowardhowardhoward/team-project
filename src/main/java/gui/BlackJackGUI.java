package gui;

import entities.Game;
import usecase.PlaceBetAndDeal.PlaceBetAndDealController;
import usecase.PlaceBetAndDeal.PlaceBetAndDealInteractor;
import usecase.PlaceBetAndDeal.PlaceBetAndDealPresenter;
import usecase.PlayerActions.PlayerActionController;
import usecase.PlayerActions.PlayerActionInteractor;
import usecase.DealerAction.DealerActionController;
import usecase.DealerAction.DealerActionInteractor;
import gui.interface_adapters.*;
import frameworks_and_drivers.DeckApiService;
import usecase.DeckProvider;

import javax.swing.*;
import java.awt.*;

public class BlackjackGUI extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel screens = new JPanel(cardLayout);

    private StartView startView;
    private BetView betView;
    private GameView gameView;

    public BlackjackGUI() {
        setTitle("Blackjack");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeScreens();

        add(screens);
        setVisible(true);
    }

    private void initializeScreens() {

        // -------------- CORE GAME OBJECT --------------
        Game game = new Game();

        // -------------- DECK --------------
        DeckProvider deck = new DeckApiService();

        // -------------- PRESENTERS --------------
        GameView gameViewRef = new GameView(this);
        PlayerActionPresenter playerPresenter = new PlayerActionPresenter(gameViewRef);
        DealerActionPresenter dealerPresenter = new DealerActionPresenter(gameViewRef);

        // -------------- INTERACTORS --------------
        PlaceBetAndDealInteractor dealInteractor =
                new PlaceBetAndDealInteractor(deck, game.getPlayer(), game.getDealer(),
                        new PlaceBetAndDealPresenter(gameViewRef), game);

        PlayerActionInteractor playerInteractor =
                new PlayerActionInteractor(game, deck, playerPresenter);

        DealerActionInteractor dealerInteractor =
                new DealerActionInteractor(game, deck, dealerPresenter);

        // -------------- CONTROLLERS --------------
        PlaceBetAndDealController placeBetAndDealController =
                new PlaceBetAndDealController(dealInteractor);

        PlayerActionController playerActionController =
                new PlayerActionController(playerInteractor);

        DealerActionController dealerActionController =
                new DealerActionController(dealerInteractor);

        // -------------- VIEWS --------------
        startView = new StartView(this);
        betView = new BetView(this, placeBetAndDealController, game);
        gameViewRef.injectControllers(playerActionController, dealerActionController);

        gameView = gameViewRef;

        screens.add(startView, "start");
        screens.add(betView, "bet");
        screens.add(gameView, "game");
    }

    // NAVIGATION
    public void showStart() { cardLayout.show(screens, "start"); }

    public void showBet() { cardLayout.show(screens, "bet"); }

    public void showGame() { cardLayout.show(screens, "game"); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlackjackGUI::new);
    }
}