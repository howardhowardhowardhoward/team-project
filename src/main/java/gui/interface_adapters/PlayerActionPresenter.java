package gui.interface_adapters;

import gui.GameView;
import usecase.PlayerActions.PlayerActionOutputBoundary;
import usecase.PlayerActions.PlayerActionOutputData;

public class PlayerActionPresenter implements PlayerActionOutputBoundary {

    private final GameView view;

    public PlayerActionPresenter(GameView view) {
        this.view = view;
    }

    @Override
    public void present(PlayerActionOutputData response) {

        // Display message (e.g., "Drew 10♣. Total: 20")
        view.displayMessage(response.getMessage());

        // Update player total in GUI
        view.updatePlayerTotal(response.getHandTotal());

        // If bust, notify GUI
        if (response.isBust()) {
            view.showBust();
        }

        // Enable/disable buttons based on available actions
        view.updateAvailableActions(response.getAvailableActions());

        // If round is finished, show results
        if (response.isGameComplete()) {
            view.updateDealerTotal(response.getDealerTotal());
            view.showFinalResult(response.getGameResult());
        }
    }
}