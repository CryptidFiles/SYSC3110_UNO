import java.util.*;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int score;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
        score = 0; //start score is 0 for all players
    }

    public String getName() {
        return name;
    }

    public void drawCard(Card drawnCard) { //whenever the game tells that player to draw, it adds it to their hand
        hand.add(drawnCard);
    }

    public Card playCard(int cardNumber) { //doesn't remove card yet, so game checks if valid
        return hand.get(cardNumber - 1); //-1 because numbers from 1 to N
    }

    public void removeCard(int cardNumber) { // if card valid, and moved to play pile, it's removed from player's hand
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
        printHand(); //shows the numbered list
        int chosenCard = 0;
        while (true) {
            System.out.print("Enter Card Number: "); //waits for user input (Scanner input comes from UNO game)
            if (input.hasNextInt()) { //if input is integer
                chosenCard = input.nextInt();
                if (chosenCard > 0 && chosenCard <= hand.size()) { //check if its in the range
                    input.nextLine();
                    break;
                } else {
                    System.out.println("Please enter a valid Card Number"); //prompt user to renter a number in range
                }
            } else {
                System.out.println("Please enter an integer");
                input.next();
            }
        }
        return chosenCard;
    }

    public void clearHand() { //used at start of a new round
        hand.clear();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int handSize() { //used to check if won
        return hand.size();
    }

    public void addScore(int points) { //used if this player won, UNO game makes them add the score
        score += points;
    }

    public int getScore() {
        return score;
    }
}
