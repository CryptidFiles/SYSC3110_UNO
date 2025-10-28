/**
 * Represents the Skip card in UNO Flip.
 * When played, this card causes one or all players to lose their turn depending on the active side.
 */
public class SkipCard extends Card {

    /**
     * Constructs a SkipCard with the specified light and dark colors.
     * On the light side, it acts as a regular Skip card; on the dark side, it acts as a Skip Everyone card.
     *
     * @param light_color The color of the card on the light side.
     * @param dark_color The color of the card on the dark side.
     */
    public SkipCard(Color light_color, Color dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.SKIP;
        this.darkType = CardType.SKIP_EVERYONE;
    }

    /**
     * Executes the Skip card’s action.
     * On the light side, the next player’s turn is skipped; on the dark side, all other players are skipped.
     *
     * @param model  The current UNO game instance managing the round.
     * @param player The player who played this card.
     * @return boolean. true after the skip action has been successfully applied.
     */
    @Override
    public boolean action(UNO_Game model, Player player){
        if (isLightSideActive) {
            // Light side: Regular Skip - skip next player
            Player nextPlayer = model.getNextPlayer(player);
            System.out.println("Skipping " + nextPlayer.getName() + "!");
            model.addSkip(1);

        } else {
            // Dark side: Skip All - skip all other players
            System.out.println("Skipping all other players!");
            model.skipAllPlayers();
        }

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
