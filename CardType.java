//Type is the action or value written on the card
public enum CardType {
    // Number cards
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,

    // Light Action Cards
    DRAW_ONE, LIGHT_REVERSE, SKIP, WILD, WILD_DRAW_TWO, FLIP,

    // Dark Action Cards
    DRAW_FIVE, DARK_REVERSE, SKIP_EVERYONE, DARK_WILD, WILD_DRAW_COLOR, DARK_FLIP;

    // Helper methods
    public boolean isNumberCard() {
        return this.ordinal() <= NINE.ordinal();
    }

    public boolean isActionCard() {
        return this.ordinal() >= DRAW_ONE.ordinal() && this.ordinal() <= DARK_FLIP.ordinal();
    }

    public boolean isWildCard() {
        return this == WILD || this == WILD_DRAW_TWO || this == DARK_WILD || this == WILD_DRAW_COLOR;
    }

    public int getPointValue() {
        if (this == LIGHT_REVERSE || this == DARK_REVERSE || this == DRAW_FIVE || this == SKIP || this == FLIP || this == DARK_FLIP) {
            return 20;
        }

        if (this == DRAW_ONE) {
            return 10;
        }

        if (this == SKIP_EVERYONE) {
            return 30;
        }

        if (this == WILD_DRAW_TWO) {
            return 50;
        }

        if (this == WILD_DRAW_COLOR) {
            return 60;
        }

        if (this == DARK_WILD || this == WILD) {
            return 40;
        }
        else {
            return this.ordinal() + 1;
        }
    }
}