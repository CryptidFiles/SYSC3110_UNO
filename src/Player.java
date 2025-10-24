import java.util.*;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int score;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void drawCard(Card drawnCard) {
        hand.add(drawnCard);
    }

    public Card playCard(int cardNumber) {
        return hand.get(cardNumber);
    }

    public void printHand() {
        for (Card card : hand) {
            card.printCard();
        }
    }

    public int takeTurn() {
        printHand();
        Scanner input = new Scanner(System.in);
        int chosenCard = 0;
        while (true) {
            System.out.print("Enter Card Number: ");
            if (input.hasNextInt()) {
                chosenCard = input.nextInt();

                if (chosenCard >= 0 && chosenCard <= hand.size()) {
                    System.out.println("You entered: " + chosenCard);
                    break;
                } else {
                    System.out.println("Please enter a valid Card Number");
                }
            } else {
                System.out.println("Please enter an integer");
                input.next();
            }
        }
        input.close();
        return chosenCard;
    }
}
