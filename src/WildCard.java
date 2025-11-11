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
 * @version 2.0, November 10, 2025
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
        model.triggerColorSelection();
        return true;
    }

    /**
     * Applies the chosen color to this wild card
     * This should be called by the controller after color selection
     */
    public void applyChosenColor(CardColor chosenColor, boolean isLightSide) {
        if (isLightSide) {
            this.lightColor = chosenColor;
            this.darkColor = chosenColor.getDarkCounterpart();
        } else {
            this.darkColor = chosenColor;
            this.lightColor = chosenColor.getLightCounterpart();
        }
    }



    /**
     * Determines whether this card can be legally played on top of another card.
     * Wild cards can always be played, regardless of color or type.
     *
     * @param otherCard The card currently on top of the play pile.
     * @return boolean. true since Wild cards are always playable.
     */
    @Override
    public boolean playableOnTop(Card otherCard){
        return true;
    }

}
