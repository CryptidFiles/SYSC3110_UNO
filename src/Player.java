import java.util.*;

/**
 * Represents a player in the UNO Flip game.
 * Each player has a name, a hand of cards, and a running score.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int score;
    private AIStrategy aiStrategy;
    private boolean isAI;

    /**
     * Constructs a new player with the specified name and an empty hand.
     *
     * @param name The name of the player.
     */
    public Player(String name, boolean isAI) {
        this.name = name;
        hand = new ArrayList<>();
        score = 0;
        this.isAI = isAI;
    }

    /**
     * Returns the player's name.
     *
     * @return String. The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a card to the player's hand when drawn from the deck.
     *
     * @param drawnCard The Card drawn and added to the player's hand.
     */
    public void drawCard(Card drawnCard) {
        hand.add(drawnCard);
    }

    /**
     * Retrieves a card from the player's hand by its position without removing it.
     *
     * @param cardNumber The 1-based position of the card in the player's hand.
     * @return {@link Card}. The selected Card.
     */
    public Card playCard(int cardNumber) {
        return hand.get(cardNumber - 1);
    }

    /**
     * Removes a card from the player's hand after it has been played.
     *
     * @param cardNumber The 1-based position of the card to remove.
     */
    public void removeCard(int cardNumber) {
        hand.remove(cardNumber - 1);
    }

    /**
     * Prints all the cards in the player's current hand to the console.
     */
    public void printHand() {
        System.out.println(name + "'s hand:");
        int index = 1;
        for (Card card : hand) {
            System.out.print("\t" + index + ". ");
            index++;
            card.printCard();
        }
    }

    /**
     * Handles the player's turn input by displaying their hand and prompting for a card choice.
     * The player may choose to play a card or draw a new one.
     *
     * @param input The {@link Scanner} used to capture player input.
     * @return int. The chosen card number, or 0 if the player chooses to draw a card.
     */
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

    /**
     * Removes all cards from the player's hand.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Returns all cards currently held by the player.
     *
     * @return An {@link ArrayList} containing the player's hand of cards.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Returns the number of cards currently in the player's hand.
     *
     * @return The size of the player's hand.
     */
    public int handSize() {
        return hand.size();
    }

    /**
     * Adds the specified number of points to the player's score.
     *
     * @param points The number of points to add to the score.
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Returns the player's total score.
     *
     * @return The player's cumulative score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the player's total score.
     *
     * @return
     */
    public AIStrategy getAIStrategy() {
        return aiStrategy;
    }

    public void setAIStrategy(AIStrategy strategy) {
        this.aiStrategy = strategy;
        this.isAI = true;
    }
}
