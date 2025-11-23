/**
 * Represents the Reverse card in UNO Flip.
 * When played, this card changes the direction of play between clockwise and counterclockwise.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
public class ReverseCard extends Card {

    /**
     * Constructs a ReverseCard with the specified light and dark colors.
     * On the light side, it acts as a Light Reverse card; on the dark side, as a Dark Reverse card.
     *
     * @param light_color The color of the card on the light side.
     * @param dark_color The color of the card on the dark side.
     */
    public ReverseCard(CardColor light_color, CardColor dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.LIGHT_REVERSE;
        this.darkType = CardType.DARK_REVERSE;
    }

    /**
     * Executes the Reverse card’s action.
     * This flips the game’s direction of play.
     *
     * @param model  The current UNO game instance managing the round.
     * @param player The player who played this card.
     * @return boolean. true once the direction has been successfully flipped.
     */
    @Override
    public boolean action(UNO_Model model, Player player){
        // Special case for 2 players: reverse acts as skip
        if (model.getPlayers().size() == 2) {
            model.prepareEvent(GameEvent.EventType.MESSAGE, "Reverse card with 2 players - skipping opponent!");
            model.notifyViews();
            model.addSkip(1);
        } else {
            // Normal behavior for 3+ players
            model.flipDirection();

            model.prepareEvent(GameEvent.EventType.MESSAGE, "Direction reversed! Now going " +
                    (model.getDirection() == Direction.CLOCKWISE ? "clockwise" : "counter-clockwise"));
            model.notifyViews();
        }
        return true;
    }

    /**
     * Determines whether this card can be legally played on top of another card.
     * The card is playable if it matches either the color or type of the top card.
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
