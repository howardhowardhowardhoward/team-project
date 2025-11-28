package interface_adapters.PlaceBetAndDeal;

import entities.Card;
import usecase.PlaceBetAndDeal.PlaceBetAndDealOutputBoundary;
import usecase.PlaceBetAndDeal.PlaceBetAndDealOutputData;

import java.util.List;
import java.util.ArrayList;

public class PlaceBetAndDealPresenter implements PlaceBetAndDealOutputBoundary {
    private final PlaceBetAndDealViewModel viewModel;

    public PlaceBetAndDealPresenter(PlaceBetAndDealViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(PlaceBetAndDealOutputData outputData) {
        viewModel.setPlayerCards(outputData.getPlayerCards());
        viewModel.setDealerCards(outputData.getDealerCards());
        viewModel.setPlayerTotal(outputData.getPlayerTotal());
        viewModel.setDealerVisibleTotal(outputData.getDealerVisibleTotal());
        viewModel.setBalance(outputData.getBalance());
        viewModel.setBetAmount(outputData.getBet().getAmount());
        // convert player cards to images
        List<String> playerImages = new ArrayList<>();
        for (Card card : outputData.getPlayerCards()) {
            playerImages.add(card.getImage());
        }
        List<String> dealerImages = new ArrayList<>();
        List<Card> dealerCards = outputData.getDealerCards();
        if (!dealerCards.isEmpty()) {
            // face-down card URL
            dealerImages.add("https://deckofcardsapi.com/static/img/back.png");
        }
        if (dealerCards.size() > 1) {
            dealerImages.add(dealerCards.get(1).getImage());
        }
        viewModel.setCards(playerImages, dealerImages);

        viewModel.fireStateChanged();
    }
    public void updateBalance(double balance) {
        viewModel.setBalance(balance);
    }

    public void updateBet(double bet) {
        viewModel.setBetAmount(bet);
    }

    public void presentError(String message) {
        viewModel.fireError(message);
    }

    public void presentBetUpdated(double balance, double reservedBet) {
        viewModel.setBalance(balance);
        viewModel.setBetAmount(reservedBet);
    }
}
