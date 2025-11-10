import java.util.Scanner;

/**
 * Represents the Wild Draw card in UNO Flip.
 * When played, this card lets the player choose the next color and forces the next player
 * to draw cards based on the active side.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 1.0
 */
public class WildDrawCard extends Card {
    private Scanner color_input;


    /**
     * Constructs a WildDrawCard with default wild colors for both sides.
     * On the light side, it acts as a Wild Draw Two card; on the dark side, as a Wild Draw Color card.
     */
    public WildDrawCard() {
        isLightSideActive = true;
        this.lightColor = CardColor.WILD;
        this.darkColor = CardColor.WILD;
        this.lightType = CardType.WILD_DRAW_TWO;
        this.darkType = CardType.WILD_DRAW_COLOR;

        color_input = new Scanner(System.in);
    }

    /**
     * Executes the Wild Draw card’s action.
     * On the light side, the next player draws two cards. On the dark side,
     * the next player draws until they find a card matching the chosen color.
     *
     * @param model  The current UNO game instance managing the round.
     * @param player The player who played this card.
     * @return boolean. true once the chosen color has been set and the draw action completed.
     */
    @Override
    public boolean action(UNO_Game model, Player player){
        System.out.println("What color do you want it to be?");

        while (true) {
            if (isLightSideActive) {
                System.out.print("(Red, Yellow, Blue, Green): ");

                String wildCardColor = color_input.nextLine().trim().toLowerCase();
                switch (wildCardColor) {
                    case "red":
                        this.lightColor = CardColor.RED;
                        this.darkColor = CardColor.RED.getDarkCounterpart();
                        break;

                    case "yellow":
                        this.lightColor = CardColor.YELLOW;
                        this.darkColor = CardColor.YELLOW.getDarkCounterpart();
                        break;

                    case "blue":
                        this.lightColor = CardColor.BLUE;
                        this.darkColor = CardColor.BLUE.getDarkCounterpart();
                        break;

                    case "green":
                        this.lightColor = CardColor.GREEN;
                        this.darkColor = CardColor.GREEN.getDarkCounterpart();
                        break;

                    default:
                        System.out.println("Invalid move. Try again.\n");
                        continue;
                }

                // Light side: next player picks up two cards
                Player nextPlayer = model.getNextPlayer(player);
                System.out.println(nextPlayer.getName() + " draws 2 cards!");

                for(int i = 0; i < 2; i++){
                    Card card = model.getPlayDeck().drawCard();

                    if (card == null) break;

                    System.out.print(nextPlayer.getName() + " draws ");
                    card.printCard();

                    nextPlayer.drawCard(card);
                }

                return true; // Add this to exit the method after successful light side action

            } else {
                System.out.print("(Teal, Orange, Purple, Pink): ");

                String wildCardColor = color_input.nextLine().trim().toLowerCase();
                switch (wildCardColor) {

                    case "teal":
                        this.lightColor = CardColor.TEAL.getLightCounterpart();
                        this.darkColor = CardColor.TEAL;
                        break;

                    case "orange":
                        this.lightColor = CardColor.ORANGE.getLightCounterpart();
                        this.darkColor = CardColor.ORANGE;
                        break;

                    case "purple":
                        this.lightColor = CardColor.PURPLE.getLightCounterpart();
                        this.darkColor = CardColor.PURPLE;
                        break;

                    case "pink":
                        this.lightColor = CardColor.PINK.getLightCounterpart();
                        this.darkColor = CardColor.PINK;
                        break;

                    default:
                        System.out.println("Invalid move. Try again.\n");
                        continue;
                }


                // Dark side: next player keeps drawing until they get the chosen color
                Player nextPlayer = model.getNextPlayer(player);
                boolean foundColor = false;
                int cardsDrawn = 0;

                while (!foundColor) {
                    Card drawnCard = model.getPlayDeck().drawCard(); // Assuming there's a method to draw one card
                    cardsDrawn++;
                    System.out.print(nextPlayer.getName() + " draws a ");
                    drawnCard.printCard();

                    // Check if the drawn card matches the chosen color
                    if (drawnCard.getColor() == this.darkColor) {
                        foundColor = true;
                        System.out.println(nextPlayer.getName() + " found the chosen color after drawing " + cardsDrawn + " cards!");
                    }
                }

                return true; // Add this to exit the method after successful dark side action
            }
        }

    }


    /**
     * Determines whether this card can be legally played on top of another card.
     * Wild Draw cards can always be played regardless of color or type.
     *
     * @param otherCard The card currently on top of the play pile.
     * @return boolean. true since Wild Draw cards are always playable.
     */
    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
        return true;
    }

}
