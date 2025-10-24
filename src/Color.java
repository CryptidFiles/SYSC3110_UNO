public enum Color {
    //Light (0-3)
    RED, BLUE, YELLOW, GREEN,

    //Wild (4)
    WILD,

    //Dark (5-8)
    ORANGE, TEAL, PURPLE, PINK;


    //Helper functions
    public boolean isLight() {
        return this == BLUE || this == GREEN || this == RED || this == YELLOW;
    }

    public boolean isDark() {
        return this == PINK || this == TEAL || this == ORANGE || this == PURPLE;
    }
}


