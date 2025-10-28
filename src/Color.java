/**
 * Represents all color variations used in UNO Flip cards, including light, dark, and wild colors.
 * Each color has a corresponding counterpart on the opposite side.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 1.0
 *
 */
public enum Color {
    //Light (0-3)
    RED, BLUE, YELLOW, GREEN,

    //Wild (4)
    WILD,

    //Dark (5-8)
    PURPLE, PINK, ORANGE, TEAL;


    /**
     * Determines whether this color belongs to the light side.
     *
     * @return boolean. true if the color is a light color; false otherwise.
     */
    //Helper functions
    public boolean isLight() {
        return this == BLUE || this == GREEN || this == RED || this == YELLOW;
    }

    /**
     * Determines whether this color belongs to the dark side.
     *
     * @return boolean. true if the color is a dark color; false otherwise.
     */
    public boolean isDark() {
        return this == PINK || this == TEAL || this == ORANGE || this == PURPLE;
    }

    /**
     * Returns the dark-side counterpart of this color.
     * Wild and dark colors return themselves.
     *
     * @return {@link Color} The corresponding dark-side Color.
     */
    public Color getDarkCounterpart() {
        return switch (this) {
            case RED -> Color.PINK;
            case BLUE -> Color.TEAL;
            case YELLOW -> Color.ORANGE;
            case GREEN -> Color.PURPLE;
            default -> this; // Return self for wild/dark colors
        };
    }

    /**
     * Returns the light-side counterpart of this color.
     * Wild and light colors return themselves.
     *
     * @return {@link Color} The corresponding light-side Color.
     */
    public Color getLightCounterpart() {
        return switch (this) {
            case PINK -> Color.RED;
            case TEAL -> Color.BLUE ;
            case ORANGE -> Color.YELLOW;
            case PURPLE -> Color.GREEN;
            default -> this; // Return self for wild/dark colors
        };
    }
}


