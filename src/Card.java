/**
 * Represents an abstract UNO Flip card with light and dark sides, each having its own color and type.
 * Subclasses define specific card behaviors and playable rules.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 1.0
 *
 */
abstract public class Card {
    protected boolean isLightSideActive;

    // Attributes shared by ALL cards. Each card object has both a color and a type for each side
    protected Color lightColor;
    protected Color darkColor;

    protected CardType lightType;
    protected CardType darkType;

    /**
     * Defines the specific action performed when this card is played.
     *
     * @param model The current UNO game instance managing the round.
     * @param player The player who played this card.
     * @return boolean. true if the action was successfully executed; false otherwise.
     */
    public abstract boolean action(UNO_Game model, Player player);

    /**
     * Determines whether this card can legally be played on top of another card.
     *
     * @param otherCard The card currently on top of the play pile.
     * @return boolean. true if this card is playable on the given card; false otherwise.
     */
    public abstract boolean playableOnTop(Card otherCard);


    /**
     * Returns the active card type based on the currently active side.
     *
     * @return {@link CardType}. The CardType (Numbered of action card) corresponding to the active side.
     */
    public CardType getType() {
        if (isLightSideActive) {
            return lightType;
        } else {
            return darkType;
        }
    }

    /**
     * Returns the active color of the card based on the current side.
     *
     * @return {@link Color}. The Color corresponding to the active side.
     */
    public Color getColor() {
        if (isLightSideActive) {
            return lightColor;
        } else {
            return darkColor;
        }
    }

    /**
     * Switches the card to its opposite side (light or dark).
     */
    public void flip() {
        isLightSideActive = !isLightSideActive;
    }

    /**
     * Returns whether the card is currently on its light or dark side.
     *
     * @return boolean. true if the card is on the light side; false if on the dark side.
     */
    public boolean getActiveSide() {
        return isLightSideActive;
    }


    /**
     * Prints the card’s color and type to the console, depending on the active side.
     */
    public void printCard(){
        if(isLightSideActive){
            System.out.println(this.lightColor + "  " + this.lightType);
        } else if (!isLightSideActive) {
            System.out.println(this.darkColor + "  " + this.darkType);
        }
    }
}
