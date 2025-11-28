package entities;

import java.util.List;

public class GameRules {
    private final double maxBet;
    private final double minBet;
    private final double blackjackPayout; // e.g., 1.5 for 3:2
    private final boolean allowDoubleDown;
    private final boolean allowSplit;
    private final boolean allowInsurance;
    private final List<ChipDenomination> allowedChips;

    public GameRules(double minBet,
                     double maxBet,
                     double blackjackPayout,
                     boolean allowDoubleDown,
                     boolean allowSplit,
                     boolean allowInsurance,
                     List<ChipDenomination> allowedChips) {

        if (minBet <= 0 || maxBet <= 0 || maxBet < minBet) {
            throw new IllegalArgumentException("Invalid bet limits.");
        }
        this.minBet = minBet;
        this.maxBet = maxBet;
        this.blackjackPayout = blackjackPayout;
        this.allowDoubleDown = allowDoubleDown;
        this.allowSplit = allowSplit;
        this.allowInsurance = allowInsurance;
        this.allowedChips = List.copyOf(allowedChips);
    }

    public double getMaxBet() {
        return maxBet;
    }

    public double getMinBet() {
        return minBet;
    }

    public double getBlackjackPayout() {
        return blackjackPayout;
    }

    public boolean isAllowDoubleDown() {
        return allowDoubleDown;
    }

    public boolean isAllowSplit() {
        return allowSplit;
    }

    public boolean isAllowInsurance() {
        return allowInsurance;
    }

    public List<ChipDenomination> getAllowedChips() {
        return allowedChips;
    }
}

