
package entities;

public class Dealer {
    private Hand hand;
    public Dealer() {
        this.hand = new Hand();
    }

    public void draw(Card card) {
        this.hand.addCard(card);
        // SIMPLIFIED: Removed manual score tracking, use hand.getTotalPoints() instead
    }

    public int GetDealerScore() {
        // FIXED: Use Hand's getTotalPoints() which properly handles Ace value adjustments
        return this.hand.getTotalPoints();
    }

    public Hand getHand() {
        return this.hand;
    }

    public boolean isBust() {
        // FIXED: Delegate to Hand class method
        return this.hand.isBust();
    }

    public boolean isBlackJack() {
        // FIXED: Use Hand's isBlackjack() which correctly checks for 2 cards totaling 21
        // Previous implementation (score == 21) was incorrect - any 21 is not a blackjack
        return this.hand.isBlackjack();
    }
}