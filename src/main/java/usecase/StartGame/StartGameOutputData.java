package usecase.StartGame;
import entities.Card;
import java.util.List;

    public class StartGameOutputData {
        private final List<Card> playerCards;
        private final List<Card> dealerCards;
        private final int playerTotal;
        private final int dealerVisibleTotal;
        private final boolean blackjack;

        public StartGameOutputData(List<Card> playerCards,
                                   List<Card> dealerCards,
                                   int playerTotal,
                                   int dealerVisibleTotal,
                                   boolean blackjack) {
            this.playerCards = playerCards;
            this.dealerCards = dealerCards;
            this.playerTotal = playerTotal;
            this.dealerVisibleTotal = dealerVisibleTotal;
            this.blackjack = blackjack;
        }

        public List<Card> getPlayerCards() {
            return playerCards;
        }

        public List<Card> getDealerCards() {
            return dealerCards;
        }

        public int getPlayerTotal() {
            return playerTotal;
        }

        public int getDealerVisibleTotal() {
            return dealerVisibleTotal;
        }

        public boolean isBlackjack() {
            return blackjack;
        }
    }

}
