package usecase.StartGame;

import entities.*;
import frameworks_and_drivers.DeckApiService;
import frameworks_and_drivers.DeckProvider;
import frameworks_and_drivers.Deck; // 新增：导入 Deck 类

import java.util.List;

public class StartGameInteractor implements StartGameInputBoundary {

    private final StartGameOutputBoundary presenter;
    private final Deck gameDeck;
    private final Player player; 
    private final Dealer dealer; 

    public StartGameInteractor(StartGameOutputBoundary presenter,
                               DeckProvider deckService,
                               Player player, 
                               Dealer dealer) { 
        this.presenter = presenter;
        this.gameDeck = new Deck(deckService); 
        this.player = player;
        this.dealer = dealer;
    }
    
    // 兼容旧签名
    public StartGameInteractor(StartGameOutputBoundary presenter,
                               DeckApiService deckService,
                               Game game) { 
        this(presenter, deckService, 
             game != null ? game.getPlayer() : new Player(1000), 
             game != null ? game.getDealer() : new Dealer());
    }

    @Override
    public void execute(StartGameInputData inputData) {
        try {
            player.clearHands();
            dealer.getHand().clear();
            gameDeck.shuffle();

            List<Card> initialPlayerCards = gameDeck.drawCards(2);
            List<Card> initialDealerCards = gameDeck.drawCards(2);

            Hand playerhand = player.getHand1();
            Hand dealerhand = dealer.getHand();

            for (int i = 0; i <= 1; i++){
                playerhand.addCard(initialPlayerCards.get(i));
                dealerhand.addCard(initialDealerCards.get(i));
            }

            int playerTotal = playerhand.getTotalPoints();
            int dealerVisibleTotal = initialDealerCards.get(0).getValue();
            boolean playerBlackjack = player.isBlackjack();

            StartGameOutputData outputData = new StartGameOutputData(
                    initialPlayerCards,
                    initialDealerCards,
                    playerTotal,
                    dealerVisibleTotal,
                    playerBlackjack
            );

            presenter.present(outputData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}