public enum Color {
    RED, BLUE, YELLOW, GREEN, WILD, ORANGE, TEAL, PURPLE, PINK;


    //Helper functions
    public boolean isLight() {
        return this == BLUE || this == GREEN || this == RED || this == YELLOW;
    }

    public boolean isDark() {
        return this == PINK || this == TEAL || this == ORANGE || this == PURPLE;
    }
}


