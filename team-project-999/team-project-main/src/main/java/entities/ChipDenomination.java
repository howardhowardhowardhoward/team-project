package entities;

/**
 * Allowed chip denominations for betting.
 * The GUI will map chip buttons to these values.
 */
public enum ChipDenomination {
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500);

    private final int value;

    ChipDenomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

