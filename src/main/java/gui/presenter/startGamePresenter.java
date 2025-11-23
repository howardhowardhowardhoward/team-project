package gui.presenter;
import gui.GameView;
import usecase.StartGame.StartGameOutputBoundary;
import usecase.StartGame.StartGameOutputData;

public class startGamePresenter implements StartGameOutputBoundary {

    private final GameView view;

    public startGamePresenter(GameView view) {
        this.view = view;
    }

    @Override
    public void present(StartGameOutputData data) {
        view.updateBalance(data.getPlayerTotal());

        view.updatePlayerHand(
                data.getPlayerCards(),
                data.getPlayerCards().stream().mapToInt(c -> c.getValue()).sum() // better: use Hand
        );

        view.updateDealerHand(
                data.getDealerCards(),
                data.getDealerCards().get(0).getValue() // show only first card
        );

        view.showMessage("Round started!");

        // After dealing:
        view.setActionButtonsEnabled(true, true, data.isPlayerBlackjack(), true);
    }

    @Override
    public void presentBetError(String message) {
        view.showMessage(message);
    }
}