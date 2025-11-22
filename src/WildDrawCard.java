/**
 * The WildCard class represents the special dual-sided "Wild Draw" card
 * in the UNO Flip game.
 * On the light side, this card acts as a Wild Draw Two, allowing the player
 * to choose a new color and forcing the next player to draw two cards and lose their turn.
 * On the dark side, it acts as a Wild Draw Color, where the next player keeps
 * drawing cards until they draw one that matches the chosen color.
 * This class extends {@link Card} and implements both sides’ effects
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
public class WildDrawCard extends Card {
    /**
     * Constructs a WildDrawCard with predefined light and dark side properties.
     */
    public WildDrawCard() {
        isLightSideActive = true;
        this.lightColor = CardColor.WILD;
        this.darkColor = CardColor.WILD;
        this.lightType = CardType.DRAW_TWO;
        this.darkType = CardType.DRAW_COLOR;
    }

    /**
     * Executes the initial action when this card is played.
     * Since this is a wild card, it first triggers color selection and then
     * the action effect after the player has chosen a color.
     *
     * @param model  the {@link UNO_Model} model managing the game state
     * @param player the {@link Player} who played the card
     * @return boolean. always true, as the color selection phase follows immediately
     */
    @Override
    public boolean action(UNO_Model model, Player player) {
        // Trigger color selection
        model.triggerColorSelection();
        return true; // Draw action will happen after color selection
    }

    /**
     * Executes the draw action after color is chosen
     */
    public void executeDrawAction(CardColor chosenColor, boolean isLightSide, UNO_Model model, Player player) {
        if (isLightSide) {
            this.lightColor = chosenColor;
            this.darkColor = chosenColor.getDarkCounterpart();

            // Light side: next player picks up two cards
            Player nextPlayer = model.getNextPlayer(player);
            model.notifyMessage(nextPlayer.getName() + " draws 2 cards and lost a turn!");

            for(int i = 0; i < 2; i++){
                Card card = model.getPlayDeck().drawCardFromDeck();
                if (card == null) break;
                nextPlayer.drawCardToHand(card);
            }

            model.addSkip(1); // Skip the next player

        } else {
            this.darkColor = chosenColor;
            this.lightColor = chosenColor.getLightCounterpart();

            // Dark side: next player keeps drawing until they get the chosen color
            Player nextPlayer = model.getNextPlayer(player);
            boolean foundColor = false;
            int cardsDrawn = 0;

            while (!foundColor) {
                Card drawnCard = model.getPlayDeck().drawCardFromDeck();
                if (drawnCard == null) {
                    model.notifyMessage("No more cards in deck!");
                    break;
                }

                cardsDrawn++;
                nextPlayer.drawCardToHand(drawnCard);

                if (drawnCard.getColor() == this.darkColor) {
                    foundColor = true;
                    model.notifyMessage(nextPlayer.getName() + " found the chosen color after drawing " + cardsDrawn + " cards and lost a turn!");
                }
            }

            model.addSkip(1); // Skip the next player
        }
    }

    /**
     * Determines whether this card can be legally played on top of another card.
     * Wild cards can always be played, regardless of the top card's color or type.
     *
     * @param otherCard the {@link Card} currently on top of the play pile
     * @return boolean. true always
     */
    @Override
    public boolean playableOnTop(Card otherCard) {
        return true;
    }
}