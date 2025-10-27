import java.util.*;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int score;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
        score = 0;
    }

    public String getName() {
        return name;
    }

    public void drawCard(Card drawnCard) {
        hand.add(drawnCard);
    }

    public Card playCard(int cardNumber) {
        return hand.get(cardNumber - 1);
    }

    public void removeCard(int cardNumber) {
        hand.remove(cardNumber - 1);
    }

    public void printHand() {
        System.out.println(name + "'s hand:");
        int index = 1;
        for (Card card : hand) {
            System.out.print("\t" + index + ". ");
            index++;
            card.printCard();
        }
    }

    public int takeTurn(Scanner input) {
        printHand();
        int chosenCard = 0;
        while (true) {
            System.out.print("Enter Card Number or draw a card (Enter 0): ");
            if (input.hasNextInt()) {
                chosenCard = input.nextInt();
                if (chosenCard >= 0 && chosenCard <= hand.size()) {
                    input.nextLine();
                    break;
                } else {
                    System.out.println("Please enter a valid Card Number");
                }
            } else {
                System.out.println("Please enter an integer");
                input.next();
            }
        }
        return chosenCard;
    }

    public void clearHand() {
        hand.clear();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int handSize() {
        return hand.size();
    }

    public void addScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }
}
