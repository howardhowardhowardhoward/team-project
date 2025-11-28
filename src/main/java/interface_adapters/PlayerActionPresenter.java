package interface_adapters;

import usecase.PlayerActions.PlayerActionOutputBoundary;
import usecase.PlayerActions.PlayerActionOutputData;

import javax.swing.*;

public class PlayerActionPresenter implements PlayerActionOutputBoundary {

    private final PlayerActionViewModel viewModel;

    public PlayerActionPresenter(PlayerActionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(PlayerActionOutputData outputData) {
        if (outputData.getNewPlayerCardImages() != null) {
            viewModel.setPlayerCardImages(outputData.getNewPlayerCardImages());
        }
        if (outputData.getDealerCardImages() != null) {
            viewModel.setDealerCardImages(outputData.getDealerCardImages());
        }
        viewModel.setPlayerTotal(outputData.getPlayerTotal());
        viewModel.setDealerVisibleTotal(outputData.getDealerVisibleTotal());
        viewModel.setPlayerBust(outputData.isPlayerBust());
        viewModel.setPlayerBlackjack(outputData.isPlayerBlackjack());
        viewModel.setActionComplete(outputData.isActionComplete());
        viewModel.setBalance(outputData.getBalance());
        viewModel.setBetAmount(outputData.getBetAmount());
    }

    @Override
    public void presentError(String message) {
        viewModel.setError(message);
    }

    @Override
    public void presentResult(String message, double newBalance, double newBet) {
        SwingUtilities.invokeLater(() -> {
            viewModel.setBalance(newBalance);
            viewModel.setBetAmount(newBet);
            viewModel.fireRoundMessage(message);
            viewModel.fireRoundComplete();
        });
    }
}
