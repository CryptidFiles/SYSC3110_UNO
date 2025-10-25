public enum Color {
    //Light (0-3)
    RED, BLUE, YELLOW, GREEN,

    //Wild (4)
    WILD,

    //Dark (5-8)
    PURPLE, PINK, ORANGE, TEAL;


    //Helper functions
    public boolean isLight() {
        return this == BLUE || this == GREEN || this == RED || this == YELLOW;
    }

    public boolean isDark() {
        return this == PINK || this == TEAL || this == ORANGE || this == PURPLE;
    }

    public Color getDarkCounterpart() {
        return switch (this) {
            case RED -> Color.PINK;
            case BLUE -> Color.TEAL;
            case YELLOW -> Color.ORANGE;
            case GREEN -> Color.PURPLE;
            default -> this; // Return self for wild/dark colors
        };
    }
}


