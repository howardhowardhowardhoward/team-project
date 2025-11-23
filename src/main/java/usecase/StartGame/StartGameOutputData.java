package usecase.StartGame;
import entities.Card;
import java.util.List;
import entities.GamePhase;

    public class StartGameOutputData {
        private final List<Card> playerCards;
        private final List<Card> dealerCards;
        private final int playerTotal;
        private final int dealerVisibleTotal;
        private final boolean dealerBlackjack;
        private final boolean playerBlackjack;

        public StartGameOutputData(List<Card> playerCards,
                                   List<Card> dealerCards,
                                   int playerTotal,
                                   int dealerVisibleTotal,
                                   boolean playerBlackjack,
                                   boolean dealerBlackjack) {
            this.playerCards = playerCards;
            this.dealerCards = dealerCards;
            this.playerTotal = playerTotal;
            this.dealerVisibleTotal = dealerVisibleTotal;
            this.dealerBlackjack = dealerBlackjack;
            this.playerBlackjack = playerBlackjack;

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

        public boolean isPlayerBlackjack() { return playerBlackjack; }

        public boolean isDealerBlackjack() { return dealerBlackjack; }
    }