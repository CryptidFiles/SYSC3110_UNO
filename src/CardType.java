public enum CardType {
    // Number cards
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
    // Action cards
    SKIP, SKIP_EVERYONE, REVERSE, DRAW_TWO, FLIP,
    // Wild cards
    WILD, WILD_DRAW_FOUR;

    // Helper methods
    public boolean isNumberCard() {
        return this.ordinal() >= ZERO.ordinal() && this.ordinal() <= NINE.ordinal();
    }

    public boolean isActionCard() {
        return this == SKIP || this == REVERSE || this == DRAW_TWO;
    }

    public boolean isWildCard() {
        return this == WILD || this == WILD_DRAW_FOUR;
    }

    public int getPointValue() {
        return 0;

        // REVISIT in future milestone for uno flip point system
        /**return switch (this) {
            case ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE -> ordinal();
            case SKIP, REVERSE, DRAW_TWO -> 20;
            case WILD, WILD_DRAW_FOUR -> 50;
        };*/
    }
}