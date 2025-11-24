/**
 * Represents the possible directions of play in the UNO Flip game.
 * Determines whether turns proceed clockwise or counterclockwise.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 3.0, November 24, 2025
 */
public enum Direction {
    CLOCKWISE, COUNTERCLOCKWISE;

    /**
     * Returns textual representation of the current direction
     *
     * @return {@link String} textual representation of the current direction, Clockwise ↻ or CounterClockwise ↺
     */
    @Override
    public String toString() {
        if (this == CLOCKWISE){
            return "Clockwise ==>";
        }
        else{
            return "Counter Clockwise <==";
        }
    }

}
