package gui;

import entities.Card;
import java.util.List;

public interface GameView {

    void showMessage(String msg);

    void updatePlayerHand(List<Card> cards, int total);
    void updateDealerHand(List<Card> cards, int visibleTotal);

    void updateBalance(double balance);

    void setActionButtonsEnabled(boolean hit, boolean stand, boolean split, boolean doubleDown);
}

