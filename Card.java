package entities;

public class Card {
    private final String code;   // code of card, e.g. "AS" = Ace of Spades
    private final String suit;   // Hearts, Diamonds, Clubs, Spades
    private final String rank;   // point on card
    private final int value;     // Value in game
    private final String image;

    public Card(String code, String suit, String rank, int value, String image) {
        this.code = code;
        this.suit = suit;
        this.rank = rank;
        this.value = value;
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        return value;
    }

    public String getImage() { return image; }

    /** GUI */
    @Override
    public String toString() {
        String suitSymbol = switch (suit.toLowerCase()) {
            case "hearts" -> "♥";
            case "diamonds" -> "♦";
            case "clubs" -> "♣";
            case "spades" -> "♠";
            default -> suit;
        };
        return rank + suitSymbol;
    }

    /** split */
    public boolean sameRank(Card other) {
        return this.rank.equals(other.rank);
    }
}

