/**
 * Represents the Flip card in UNO Flip.
 * When played, it reverses the game’s active side (light ↔ dark) for all cards.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
public class FlipCard extends Card {

    /**
     * Constructs a FlipCard with the specified light and dark colors.
     * On the light side, it acts as a regular Flip card; on the dark side, as a Dark Flip card.
     *
     * @param light_color The color of the card on the light side.
     * @param dark_color The color of the card on the dark side.
     */
    public FlipCard(CardColor light_color, CardColor dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.FLIP;
        this.darkType = CardType.DARK_FLIP;
    }

    /**
     * Executes the Flip card’s action.
     * This flips the active side for all cards in the game (light to dark or vice versa).
     *
     * @param model  The current UNO game instance managing the round.
     * @param player The player who played this card.
     * @return boolean. true once the game side has been flipped successfully.
     */
    @Override
    public boolean action(UNO_Model model, Player player){
        // Flip the rest of the cards
        model.flipGameSide();

        // Notify view of which side game has been flipped to
        model.notifyMessage("Game flipped to " + (isLightSideActive ? "LIGHT" : "DARK") + " SIDE");
        return true;
    }

    /**
     * Determines whether this card can be legally played on top of another card.
     * The card is playable if it shares the same color or type as the top card.
     *
     * @param otherCard The card currently on top of the play pile.
     * @return boolean. true if this card can be played; false otherwise.
     */
    // Card can be played if top card is same type or same color
    @Override
    public boolean playableOnTop(Card otherCard){
        return getColor() == otherCard.getColor() || getType() == otherCard.getType();
    }

}
