package gui.interface_adapters;

import gui.GameView;
import usecase.PlayerAction.PlayerActionOutputBoundary;
import usecase.PlayerAction.PlayerActionResponseModel;

public class PlayerActionPresenter implements PlayerActionOutputBoundary {

    private final GameView view;

    public PlayerActionPresenter(GameView view) {
        this.view = view;
    }

    @Override
    public void present(PlayerActionResponseModel response) {
        view.updatePlayerCards(response.getPlayerCards());

        if (response.isAllHandsComplete()) {
            response.getDealerController().execute();
        }
    }
}
