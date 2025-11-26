package entities;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    /** Adds a card to the hand */
    public void addCard(Card card) {
        cards.add(card);
    }

    /** Returns all cards in the hand */
    public List<Card> getCards() {
        return cards;
    }

    /** Clears the hand (for new rounds) */
    public void clear() {
        cards.clear();
    }

    /** Calculates total Blackjack points, accounting for Ace as 1 or 11 */
    public int getTotalPoints() {
        int total = 0;
        int aceCount = 0;

        for (Card card : cards) {
            total += card.getValue();
            if (card.getRank().equalsIgnoreCase("ACE")) {
                aceCount++;
            }
        }

        // Adjust Aces: count some as 1 instead of 11 if total exceeds 21
        while (total > 21 && aceCount > 0) {
            total -= 10;  // converting an Ace from 11 → 1
            aceCount--;
        }

        return total;
    }

    /** Checks if the hand is a Blackjack (Ace + 10-value card on first deal) */
    public boolean isBlackjack() {
        return cards.size() == 2 && getTotalPoints() == 21;
    }

    /** Checks if the hand has exceeded 21 (bust) */
    public boolean isBust() {
        return getTotalPoints() > 21;
    }

    /** Returns true if the first two cards have the same rank (used for splitting) */
    public boolean canSplit() {
        return cards.size() == 2 && cards.get(0).sameRank(cards.get(1));
    }

    /** GUI-friendly text representation of the hand */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.toString()).append(" ");
        }
        sb.append("(Total: ").append(getTotalPoints()).append(")");
        return sb.toString();
    }
}
