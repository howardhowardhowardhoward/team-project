package usecase.dealeraction;

import entities.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class DealerActionInteractor implements DealerActionInputBoundary {
    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private final DealerActionOutputBoundary presenter;

    public DealerActionInteractor(Deck deck, Player player, Dealer dealer,
                                  DealerActionOutputBoundary presenter) {
        this.deck = deck;
        this.player = player;
        this.dealer = dealer;
        this.presenter = presenter;
    }

    @Override
    public void dealerPlay() {
        Hand playerHand = player.getHand(0);
        Hand dealerHand = dealer.getHand();
        // reveal downcard
        List<String> playerImages = new ArrayList<>();
        for (Card c : playerHand.getCards()) {
            playerImages.add(c.getImage());
        }

        List<String> dealerImages = new ArrayList<>();
        for (Card c : dealerHand.getCards()) {
            dealerImages.add(c.getImage());
        }

        DealerActionOutputData outputData = new DealerActionOutputData(
                playerImages,
                dealerImages,
                playerHand.getTotalPoints(),
                dealerHand.getTotalPoints(),
                dealerHand.isBust(),
                dealerHand.isBlackjack(),
                false
        );
        presenter.present(outputData);

        Timer timer = new Timer(1000, null); // 1s delay between drawing cards
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dealerHand.getTotalPoints() < 17) {
                    Card drawnCard = deck.drawCard();
                    dealerHand.addCard(drawnCard);

                    // Build image lists
                    List<String> playerImages = new ArrayList<>();
                    for (Card c : playerHand.getCards()) {
                        playerImages.add(c.getImage());
                    }

                    List<String> dealerImages = new ArrayList<>();
                    for (Card c : dealerHand.getCards()) {
                        dealerImages.add(c.getImage());
                    }

                    DealerActionOutputData outputData = new DealerActionOutputData(
                            playerImages,
                            dealerImages,
                            playerHand.getTotalPoints(),
                            dealerHand.getTotalPoints(),
                            dealerHand.isBust(),
                            dealerHand.isBlackjack(),
                            false
                    );

                    presenter.present(outputData);
                } else {
                    // Final update
                    List<String> playerImages = new ArrayList<>();
                    for (Card c : playerHand.getCards()) {
                        playerImages.add(c.getImage());
                    }

                    List<String> dealerImages = new ArrayList<>();
                    for (Card c : dealerHand.getCards()) {
                        dealerImages.add(c.getImage());
                    }

                    DealerActionOutputData finalOutput = new DealerActionOutputData(
                            playerImages,
                            dealerImages,
                            playerHand.getTotalPoints(),
                            dealerHand.getTotalPoints(),
                            dealerHand.isBust(),
                            dealerHand.isBlackjack(),
                            true
                    );

                    presenter.present(finalOutput);
                    timer.stop();
                }
            }
        });

        timer.start();
    }
}
