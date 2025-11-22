/**
 * The Card class serves as an abstract base class representing a dual-sided UNO card.
 * Each card has a light side and a dark side, each with its own {@link CardColor} and {@link CardType}.
 * The card can flip between sides and perform actions when played
 * This class provides core functionality for handling card attributes leaving specific gameplay logic
 * to be implemented by its subclasses
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
abstract public class Card {
    protected boolean isLightSideActive;

    // Attributes shared by ALL cards. Each card object has both a color and a type for each side
    protected CardColor lightColor;
    protected CardColor darkColor;

    protected CardType lightType;
    protected CardType darkType;

    /**
     * Defines the effect or action that this card performs when played.
     *
     * @param model  the {@link UNO_Model} model managing the game state
     * @param player the {@link Player} who played the card
     * @return boolean. true if the action was successfully performed, false otherwise
     */
    public abstract boolean action(UNO_Model model, Player player);


    /**
     * Determines whether this card can legally be played on top of another card.
     *
     * @param otherCard the {@link Card} currently on top of the play pile
     * @return boolean. true if this card can be played on top of otherCard, false otherwise
     */
    public abstract boolean playableOnTop(Card otherCard);


    /**
     * Returns the card typeof the currently active side.
     *
     * @return the {@link CardType} for the active side, either light or dark
     */
    public CardType getType() {
        if (isLightSideActive) {
            return lightType;
        } else {
            return darkType;
        }
    }

    /**
     * Returns the Card color of the currently active side.
     *
     * @return the {@link CardColor} for the active side, light or dark
     */
    public CardColor getColor() {
        if (isLightSideActive) {
            return lightColor;
        } else {
            return darkColor;
        }
    }


    /**
     * Flips the card, switching between the light and dark sides.
     * If the light side is active, it becomes dark, and vice versa.
     */
    public void flip() {
        isLightSideActive = !isLightSideActive;
    }


    /**
     * Returns whether the card is currently showing its light side.
     *
     * @return boolean. true if the light side is active; false if otherwise (darkside activated)
     */
    public boolean getActiveSide() {
        return isLightSideActive;
    }


    /**
     * Prints a textual representation of the card to the console.
     * Prints the color and type of the currently activated side
     */
    public void printCard(){
        if(isLightSideActive){
            System.out.println(this.lightColor + "  " + this.lightType);
        } else if (!isLightSideActive) {
            System.out.println(this.darkColor + "  " + this.darkType);
        }
    }
}
