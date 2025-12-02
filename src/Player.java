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
 * @version 3.0, November 24, 2025
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
        this.hand = new ArrayList<>();
        this.score = 0;
        this.isAI = isAI;
    }


    public Player(String name, boolean isAI, AIStrategy strategy) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
        this.isAI = isAI;
        this.aiStrategy = strategy;
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
    public void drawCardToHand(Card drawnCard) {
        hand.add(drawnCard);
    }

    /**
     * Retrieves a card from the player's hand by its position without removing it.
     *
     * @param cardNumber The 1-based position of the card in the player's hand.
     * @return {@link Card}. The selected Card.
     */
    public Card getCardInHand(int cardNumber) {
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


    public boolean isPlayerAI() {
        return isAI;
    }

    public AIStrategy getAIStrategy() {
        return aiStrategy;
    }

    public void setAI(boolean isAI){
        this.isAI = isAI;
    }

    public void setAiStrategy(AIStrategy strategy) {
        this.aiStrategy = strategy;
    }

    /**
     * Resets the player's score to 0
     */
    public void resetScore(){
        this.score = 0;
    }
}
