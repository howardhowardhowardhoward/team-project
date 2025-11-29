package usecase.PlaceBetAndDeal;

import entities.*;
import frameworks_and_drivers.ExitRestartGame.ExitRestartGame;
import interface_adapters.ExitRestartGame.ExitRestartGameController;
import interface_adapters.ExitRestartGame.ExitRestartGamePresenter;
import interface_adapters.ExitRestartGame.ExitRestartGameViewModel;
import interface_adapters.PlaceBetAndDeal.PlaceBetAndDealViewModel;
import usecase.ExitRestartGame.ExitRestartGameDataAccess;
import usecase.ExitRestartGame.ExitRestartGameInputData;
import usecase.ExitRestartGame.ExitRestartGameInteractor;

import java.util.List;

public class PlaceBetAndDealInteractor implements PlaceBetAndDealInputBoundary {
    private final PlaceBetAndDealDataAccessInterface dataAccess;
    private final PlaceBetAndDealOutputBoundary presenter;

    private double reservedBet = 0;

    public PlaceBetAndDealInteractor(PlaceBetAndDealDataAccessInterface dataAccess,
                                     PlaceBetAndDealOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(PlaceBetAndDealInputData inputData) {
        try {
            Player player = dataAccess.getPlayer();
            Dealer dealer = dataAccess.getDealer();
            Deck deck = dataAccess.getDeck();

            double balance = player.getBalance();
            double betAmount = reservedBet;

            // Place bet
            player.placeBet(betAmount, reservedBet);
            Bet bet = new Bet(betAmount, BetType.MAIN);

            // Reset hands
            player.clearHands();
            dealer.getHand().clear();

            // Draw cards
            List<Card> playerCards = deck.drawCards(2);
            List<Card> dealerCards = deck.drawCards(2);

            Hand playerHand = player.getHand(0);
            Hand dealerHand = dealer.getHand();

            playerHand.addCard(playerCards.get(0));
            playerHand.addCard(playerCards.get(1));
            dealerHand.addCard(dealerCards.get(0)); // hidden
            dealerHand.addCard(dealerCards.get(1)); // visible

            int playerTotal = playerHand.getTotalPoints();
            int dealerVisibleTotal = dealerCards.get(1).getValue();

            boolean playerBlackjack = playerHand.isBlackjack();

            // Output
            PlaceBetAndDealOutputData outputData = new PlaceBetAndDealOutputData(
                    bet, playerCards, dealerCards, playerTotal, dealerVisibleTotal,
                    balance, betAmount, playerBlackjack
            );

            reservedBet = 0;

            presenter.present(outputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addReservedBet(double amount) {
        Player player = dataAccess.getPlayer();

        if (player.getBalance() < amount) {
            presenter.presentError("Insufficient balance");
            return;
        }
        player.adjustBalance(-amount);
        reservedBet += amount;

        presenter.presentBetUpdated(player.getBalance(), reservedBet);
    }

    public void clearReservedBet() {
        Player player = dataAccess.getPlayer();

        if (reservedBet > 0) {
            player.adjustBalance(reservedBet);
            reservedBet = 0;
        }
        presenter.presentBetUpdated(player.getBalance(), reservedBet);
    }

    public void allIn() {
        Player player = dataAccess.getPlayer();

        double amount = player.getBalance();
        if (amount <= 0) {
            presenter.presentError("Insufficient balance");
        }
        player.adjustBalance(-amount);
        reservedBet += amount;
        presenter.presentBetUpdated(player.getBalance(), reservedBet);
    }

    @Override
    public void restartGame() {
        Player player = dataAccess.getPlayer();
        Dealer dealer = dataAccess.getDealer();
        Deck deck = dataAccess.getDeck();

        PlaceBetAndDealViewModel viewModel = new PlaceBetAndDealViewModel();
        ExitRestartGamePresenter restartPresenter = new ExitRestartGamePresenter(viewModel);
        ExitRestartGameDataAccess restartDataAccess = new ExitRestartGame(player, deck, dealer);
        ExitRestartGameInteractor restartInteractor = new  ExitRestartGameInteractor(restartPresenter,
                restartDataAccess);
        ExitRestartGameController restartController = new ExitRestartGameController(restartInteractor);
        restartController.handleRestartGame(new ExitRestartGameInputData());
        reservedBet = 0;

        presenter.presentBetUpdated(player.getBalance(), reservedBet);
    }

    public Deck getDeck() {
        return dataAccess.getDeck();
    }

    public Dealer getDealer() {
        return dataAccess.getDealer();
    }

    public Player getPlayer() {
        return dataAccess.getPlayer();
    }
}
