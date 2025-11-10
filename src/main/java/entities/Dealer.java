package entities;

public class Dealer {
    private Hand hand;
    private Deck deck;
    private int score;

    public Dealer(Deck deck){
        this.hand = new Hand();
        this.deck = deck;
        this.score = 0;
    }

    public void draw(Card card){
        this.hand.addCard(card);
        this.score += card.getValue();
    }

    public int GetDealerScore(){
        return this.score;
    }

    public Hand getHand(){
        return this.hand;
    }

    public void play(){
        while (this.score < 17){
            Card newcard = this.deck.drawCard();
            this.draw(newcard);
        }
    }

    public boolean isBust(){
        return this.score > 21;
    }

    public boolean isBlackJack(){
        return this.score == 21;
    }
}
