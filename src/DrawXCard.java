/**
 * This class represents the UNO Flip! draw card, which has
 * different effects depending on the active side of the deck. On the light side,
 * it forces the next player to draw one card, and on the dark side it makes them
 * draw five cards. In both cases, the affected player also loses their turn.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 3.0, November 24, 2025
 */
public class DrawXCard extends Card {

    /**
     * Creates a DrawXCard with separate light-side and dark-side colors.
     * The light side behaves as a DRAW_ONE card, while the dark side behaves
     * as a DRAW_FIVE card.
     *
     * @param light_color the color of the card on the light side
     * @param dark_color  the color of the card on the dark side
     */
    public DrawXCard(CardColor light_color, CardColor dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.DRAW_ONE;
        this.darkType = CardType.DRAW_FIVE;
    }

    /**
     * Executes the effect of the DrawX card based on the currently active side.
     * Light side: the next player draws one card and loses their turn.
     * Dark side: the next player draws five cards and loses their turn.
     * In both cases, the method updates the game state, notifies the views,
     * and schedules a skip of the next player's turn.
     *
     * @param model  the UNO game model managing state and rules
     * @param player the player who played this card
     * @return true if the action was completed successfully, false if the deck ran out of cards
     */
    @Override
    public boolean action(UNO_Model model, Player player){
        if(isLightSideActive) {
            // Light side: DRAW_ONE - next player draws one card and loses turn
            Player nextPlayer = model.getNextPlayer(player);

            Card card = model.getPlayDeck().drawCardFromDeck();
            if (card == null) {
                model.prepareEvent(GameEvent.EventType.MESSAGE, "No more cards left!");
                model.notifyViews();
                return false;
            }

            nextPlayer.drawCardToHand(card);
            model.prepareEvent(GameEvent.EventType.MESSAGE, nextPlayer.getName() +" draws 1 card and loses their turn!");
            model.notifyViews();

            // Skip the next player's turn
            model.addSkip(1);

        } else {
            // Dark side: DRAW_FIVE - next player draws five cards and loses turn
            Player nextPlayer = model.getNextPlayer(player);

            for(int i = 0; i < 5; i++){
                Card card = model.getPlayDeck().drawCardFromDeck();

                if (card == null) {
                    model.prepareEvent(GameEvent.EventType.MESSAGE, "No more cards left after drawing " + i + " cards!");
                    model.notifyViews();
                    return false;
                }

                nextPlayer.drawCardToHand(card);
            }
            model.prepareEvent(GameEvent.EventType.MESSAGE, nextPlayer.getName() + " draws 5 cards and loses their turn!");
            model.notifyViews();

            // Skip the next player's turn
            model.addSkip(1);
        }

        return true;
    }

    /**
     * Determines whether this DrawX card can be played on top of the given card.
     * The card is playable if it matches the top card by color or by type,
     * following standard UNO rules.
     *
     * @param otherCard the card currently on top of the play pile
     * @return true if this card is playable on top of the given card, false otherwise
     */
    @Override
    public boolean playableOnTop(Card otherCard){
        return getColor() == otherCard.getColor() || getType() == otherCard.getType();
    }

}
