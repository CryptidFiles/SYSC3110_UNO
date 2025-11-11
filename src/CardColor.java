/**
 * Represents all color variations used in UNO Flip cards, including light, dark, and wild colors.
 * Each color has a corresponding counterpart on the opposite side.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
public enum CardColor {
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
     * @return {@link CardColor} The corresponding dark-side Color.
     */
    public CardColor getDarkCounterpart() {
        return switch (this) {
            case RED -> CardColor.PINK;
            case BLUE -> CardColor.TEAL;
            case YELLOW -> CardColor.ORANGE;
            case GREEN -> CardColor.PURPLE;
            default -> this; // Return self for wild/dark colors
        };
    }

    /**
     * Returns the light-side counterpart of this color.
     * Wild and light colors return themselves.
     *
     * @return {@link CardColor} The corresponding light-side Color.
     */
    public CardColor getLightCounterpart() {
        return switch (this) {
            case PINK -> CardColor.RED;
            case TEAL -> CardColor.BLUE ;
            case ORANGE -> CardColor.YELLOW;
            case PURPLE -> CardColor.GREEN;
            default -> this; // Return self for wild/dark colors
        };
    }
}


