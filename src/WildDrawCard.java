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
                }

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
                }
            }
        }

        //if(isLightSideActive){

        //}
        //next person picks up
    }

    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
        return true;
    }

}
