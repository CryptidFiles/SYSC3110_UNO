/**
 * Represents the Draw One / Draw Five action card in UNO Flip.
 * This card makes the next player draw cards and skip their turn.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 1.0
 */
public class DrawXCard extends Card {

    /**
     * Constructs a DrawXCard with the specified light and dark colors.
     * On the light side, it acts as a Draw One card; on the dark side, it acts as a Draw Five card.
     *
     * @param light_color The color of the card on the light side.
     * @param dark_color The color of the card on the dark side.
     */
    public DrawXCard(Color light_color, Color dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.DRAW_ONE;
        this.darkType = CardType.DRAW_FIVE;
    }

    /**
     * Executes the card’s effect when played.
     * On the light side, the next player draws one card; on the dark side, they draw five.
     * The affected player then loses their turn.
     *
     * @param model  The current UNO game instance managing the round.
     * @param player The player who played this card.
     * @return boolean. true if the action was successfully executed; false if the deck was empty.
     */
    @Override
    public boolean action(UNO_Game model, Player player){
        if(isLightSideActive) {
            // Light side: DRAW_ONE - next player draws one card and loses turn
            Player nextPlayer = model.getNextPlayer(player);

            Card card = model.getPlayDeck().drawCard();
            if (card == null) {
                System.out.println("No more cards left!");
                return false;
            }

            System.out.print(nextPlayer.getName() + " draws ");
            card.printCard();

            nextPlayer.drawCard(card);
            System.out.println(nextPlayer.getName() + " draws 1 card and loses their turn!");

            // Skip the next player's turn
            model.addSkip(1);

        } else {
            // Dark side: DRAW_FIVE - next player draws five cards and loses turn
            Player nextPlayer = model.getNextPlayer(player);

            for(int i = 0; i < 5; i++){
                Card card = model.getPlayDeck().drawCard();

                if (card == null) {
                    System.out.println("No more cards left!");
                    return false;
                }

                System.out.print(nextPlayer.getName() + " draws ");
                card.printCard();

                nextPlayer.drawCard(card);
            }
            System.out.println(nextPlayer.getName() + " draws 5 cards and loses their turn!");

            // Skip the next player's turn
            model.addSkip(1);
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
