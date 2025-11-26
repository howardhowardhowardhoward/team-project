package interface_adapter.PlayerActions;

import usecase.PlayerActions.PlayerActionOutputBoundary;
import usecase.PlayerActions.PlayerActionOutputData;

/**
 * Presenter for Player Actions.
 * Receives output data from the Interactor and updates the ViewModel.
 */
public class PlayerActionPresenter implements PlayerActionOutputBoundary {
    private final PlayerActionViewModel viewModel;

    public PlayerActionPresenter(PlayerActionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(PlayerActionOutputData outputData) {
        // Update message
        viewModel.setMessage(outputData.getMessage());
        
        // Determine button state: disable if game is over or player busted
        boolean gameEnded = outputData.isGameComplete() || outputData.isBust();
        viewModel.setGameButtonsEnabled(!gameEnded);
        
        // Update player total score
        viewModel.setPlayerTotal(outputData.getHandTotal());

        // If the game is complete (e.g., after Stand), update dealer info
        if (outputData.isGameComplete()) {
             viewModel.setDealerTotal(outputData.getDealerTotal());
        }
        
        // Notify the View to update
        viewModel.fireStateChanged();
    }
}