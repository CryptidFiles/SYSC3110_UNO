import java.util.Scanner;

/**
 * Represents the Wild card in UNO Flip.
 * When played, this card allows the player to choose the next active color for the game.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 1.0
 */
public class WildCard extends Card {
    private Scanner color_input;

    /**
     * Constructs a WildCard with default wild colors for both light and dark sides.
     * The player chooses the active color when the card is played.
     */
    public WildCard() {
        isLightSideActive = true;
        this.lightColor = CardColor.WILD;
        this.darkColor = CardColor.WILD;
        this.lightType = CardType.WILD;
        this.darkType = CardType.DARK_WILD;

        color_input = new Scanner(System.in);
    }

    /**
     * Executes the Wild card’s action.
     * Prompts the player to choose a new color for the next turn based on the active side.
     *
     * @param model  The current UNO game instance managing the round.
     * @param player The player who played this card.
     * @return boolean. true once a valid color has been selected.
     */
    @Override
    public boolean action(UNO_Game model, Player player){
        System.out.println("What color do you want it to be?");

        while (true) {
            if (isLightSideActive) {
                System.out.print("(Red, Yellow, Blue, Green): ");

                String wildCardColor = color_input.nextLine().trim().toLowerCase();
                switch (wildCardColor) {

                    case "red":
                        this.lightColor = CardColor.RED;
                        this.darkColor = CardColor.RED.getDarkCounterpart();
                        return true;

                    case "yellow":
                        this.lightColor = CardColor.YELLOW;
                        this.darkColor = CardColor.YELLOW.getDarkCounterpart();
                        return true;

                    case "blue":
                        this.lightColor = CardColor.BLUE;
                        this.darkColor = CardColor.BLUE.getDarkCounterpart();
                        return true;

                    case "green":
                        this.lightColor = CardColor.GREEN;
                        this.darkColor = CardColor.GREEN.getDarkCounterpart();
                        return true;

                    default:
                        System.out.println("Invalid move. Try again.\n");
                }

            } else {
                System.out.print("(Teal, Orange, Purple, Pink): ");

                String wildCardColor = color_input.nextLine().trim().toLowerCase();
                switch (wildCardColor) {

                    case "teal":
                        this.lightColor = CardColor.TEAL.getLightCounterpart();
                        this.darkColor = CardColor.TEAL;
                        return true;

                    case "orange":
                        this.lightColor = CardColor.ORANGE.getLightCounterpart();
                        this.darkColor = CardColor.ORANGE;
                        return true;

                    case "purple":
                        this.lightColor = CardColor.PURPLE.getLightCounterpart();
                        this.darkColor = CardColor.PURPLE;
                        return true;

                    case "pink":
                        this.lightColor = CardColor.PINK.getLightCounterpart();
                        this.darkColor = CardColor.PINK;
                        return true;

                    default:
                        System.out.println("Invalid move. Try again.\n");
                }
            }
        }

    }

    /**
     * Determines whether this card can be legally played on top of another card.
     * Wild cards can always be played, regardless of color or type.
     *
     * @param otherCard The card currently on top of the play pile.
     * @return boolean. true since Wild cards are always playable.
     */
    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
        return true;
    }

}
