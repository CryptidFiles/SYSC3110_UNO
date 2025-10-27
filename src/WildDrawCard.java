import java.util.Scanner;

public class WildDrawCard extends Card {
    private Scanner color_input;


    public WildDrawCard() {
        isLightSideActive = true;
        this.lightColor = Color.WILD;
        this.darkColor = Color.WILD;
        this.lightType = CardType.WILD_DRAW_TWO;
        this.darkType = CardType.WILD_DRAW_COLOR;

        color_input = new Scanner(System.in);
    }

    @Override
    public boolean action(UNO_Game model, Player player){
        System.out.println("What color do you want it to be?");

        while (true) {
            if (isLightSideActive) {
                System.out.print("(Red, Yellow, Blue, Green): ");

                String wildCardColor = color_input.nextLine().trim().toLowerCase();
                switch (wildCardColor) {
                    case "red":
                        this.lightColor = Color.RED;
                        this.darkColor = Color.RED.getDarkCounterpart();
                        break;

                    case "yellow":
                        this.lightColor = Color.YELLOW;
                        this.darkColor = Color.YELLOW.getDarkCounterpart();
                        break;

                    case "blue":
                        this.lightColor = Color.BLUE;
                        this.darkColor = Color.BLUE.getDarkCounterpart();
                        break;

                    case "green":
                        this.lightColor = Color.GREEN;
                        this.darkColor = Color.GREEN.getDarkCounterpart();
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
                        this.lightColor = Color.TEAL.getLightCounterpart();
                        this.darkColor = Color.TEAL;
                        break;

                    case "orange":
                        this.lightColor = Color.ORANGE.getLightCounterpart();
                        this.darkColor = Color.ORANGE;
                        break;

                    case "purple":
                        this.lightColor = Color.PURPLE.getLightCounterpart();
                        this.darkColor = Color.PURPLE;
                        break;

                    case "pink":
                        this.lightColor = Color.PINK.getLightCounterpart();
                        this.darkColor = Color.PINK;
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


    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
        return true;
    }

}
