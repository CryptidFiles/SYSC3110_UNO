import java.util.Scanner;

/**
 * Represents the Wild card in UNO Flip.
 * When played, this card allows the player to choose the next active color for the game.
 */
public class WildCard extends Card {
    private Scanner color_input;

    /**
     * Constructs a WildCard with default wild colors for both light and dark sides.
     * The player chooses the active color when the card is played.
     */
    public WildCard() {
        isLightSideActive = true;
        this.lightColor = Color.WILD;
        this.darkColor = Color.WILD;
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
                        this.lightColor = Color.RED;
                        this.darkColor = Color.RED.getDarkCounterpart();
                        return true;

                    case "yellow":
                        this.lightColor = Color.YELLOW;
                        this.darkColor = Color.YELLOW.getDarkCounterpart();
                        return true;

                    case "blue":
                        this.lightColor = Color.BLUE;
                        this.darkColor = Color.BLUE.getDarkCounterpart();
                        return true;

                    case "green":
                        this.lightColor = Color.GREEN;
                        this.darkColor = Color.GREEN.getDarkCounterpart();
                        return true;

                    default:
                        System.out.println("Invalid move. Try again.\n");
                }

            } else {
                System.out.print("(Teal, Orange, Purple, Pink): ");

                String wildCardColor = color_input.nextLine().trim().toLowerCase();
                switch (wildCardColor) {

                    case "teal":
                        this.lightColor = Color.TEAL.getLightCounterpart();
                        this.darkColor = Color.TEAL;
                        return true;

                    case "orange":
                        this.lightColor = Color.ORANGE.getLightCounterpart();
                        this.darkColor = Color.ORANGE;
                        return true;

                    case "purple":
                        this.lightColor = Color.PURPLE.getLightCounterpart();
                        this.darkColor = Color.PURPLE;
                        return true;

                    case "pink":
                        this.lightColor = Color.PINK.getLightCounterpart();
                        this.darkColor = Color.PINK;
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
