/**
 * Represents all possible UNO Flip card types, including both light and dark side cards.
 * Each card type may correspond to number cards or special action cards with unique effects.
 *
 */
public enum CardType {
    // Number cards
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,

    // Light Action Cards
    DRAW_ONE, LIGHT_REVERSE, SKIP, WILD, WILD_DRAW_TWO, FLIP,

    // Dark Action Cards
    DRAW_FIVE, DARK_REVERSE, SKIP_EVERYONE, DARK_WILD, WILD_DRAW_COLOR, DARK_FLIP;

    /**
     * Determines if the card type is a number card (1–9).
     *
     * @return boolean. true if the card is a number card; false otherwise.
     */
    public boolean isNumberCard() {
        return this.ordinal() <= NINE.ordinal();
    }

    /**
     * Determines if the card type is an action card, which performs a special effect when played.
     *
     * @return boolean. true if the card is an action card; false otherwise.
     */
    public boolean isActionCard() {
        return this.ordinal() >= DRAW_ONE.ordinal() && this.ordinal() <= DARK_FLIP.ordinal();
    }

    /**
     * Determines if the card type is a wild card that allows the player to choose colors or cause unique effects.
     *
     * @return boolean. true if the card is a wild card; false otherwise
     */
    public boolean isWildCard() {
        return this == WILD || this == WILD_DRAW_TWO || this == DARK_WILD || this == WILD_DRAW_COLOR;
    }

    /**
     * Returns the point value associated with this card type, used for scoring after each round.
     *
     * @return int. The point value of the card.
     */
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