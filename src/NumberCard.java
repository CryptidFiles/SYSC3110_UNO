/**
 * Represents a numbered card in UNO Flip.
 * Number cards have no special actions but can be played when matching in color or number.
 */
public class NumberCard extends Card {

    /**
     * Constructs a NumberCard with the specified light and dark colors and number type.
     * Both sides share the same number value but differ in color.
     *
     * @param light_color The color of the card on the light side.
     * @param dark_color The color of the card on the dark side.
     * @param number The CardType representing the card's number.
     */
    public NumberCard(Color light_color, Color dark_color, CardType number) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = number;
        this.darkType = number;
    }

    /**
     * Executes the number card’s action.
     * Number cards have no special effect when played.
     *
     * @param model  The current UNO game instance managing the round.
     * @param player The player who played this card.
     * @return boolean. true since number cards always perform successfully.
     */
    @Override
    public boolean action(UNO_Game model, Player player){
        //Numbered Card have no special effect
        return true;
    }


    /**
     * Determines whether this card can be legally played on top of another card.
     * The card is playable if it shares the same color or number as the top card.
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
