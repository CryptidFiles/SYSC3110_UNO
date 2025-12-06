import java.io.Serializable;
import java.util.*;

/**
 * Represents an immutable snapshot of the game state used for the undo/redo system.
 * A snapshot stores copies of all necessary game elements such as players, hands,
 * deck, play pile, turn order, direction, and event-related flags.
 * This allows the {@link UNO_Model} to fully restore the game to a previous point
 * without affecting ongoing gameplay.
 * Each snapshot contains a deep copy of all mutable game components.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 4.0, December 05, 2025
 */
public class StateSnapShot implements Serializable {
    private GameEvent previousHand;
    private int currentPlayerIndex;
    private List<Player> playersState; // Store player hands
    private Deck playDeckState; // Store deck state
    private Stack<Card> playPileState; // Store play pile
    private Direction direction;
    private int skipCount;
    private boolean hasActedThisTurn;
    private CardColor wildColorChoice;
    private boolean waitingForColorSelection;
    private boolean isLightSide;

    /**
     * Constructs a complete snapshot of the current game state.
     * This constructor performs deep copies of players, their hands,
     * the draw deck, the play pile, and all game-flow variables.
     *
     * @param hand the {@link GameEvent} associated with the moment of the snapshot
     * @param currentPlayerIndex the index of the active player at the time of the snapshot
     * @param players the list of players whose hands and scores will be copied
     * @param deck the current draw deck whose cards will be copied
     * @param pile the current play pile whose cards will be copied
     * @param dir the current play direction (clockwise/counterclockwise)
     * @param skip number of pending skips to apply on the next turn
     * @param acted whether the current player has already taken their action this turn
     * @param wildColor the currently selected wild color, if any
     * @param waiting whether the game is waiting for a wild-color selection
     */
    public StateSnapShot(GameEvent hand, int currentPlayerIndex,
                         List<Player> players, Deck deck, Stack<Card> pile,
                         Direction dir, int skip, boolean acted,
                         CardColor wildColor, boolean waiting) {
        this.previousHand = hand;
        this.currentPlayerIndex = currentPlayerIndex;

        // Deep copy of players (including their hands)
        this.playersState = new ArrayList<>();
        for (Player p : players) {
            Player copy = new Player(p.getName(), p.isPlayerAI(), p.getAIStrategy());
            copy.addScore(p.getScore());
            for (Card c : p.getHand()) {
                copy.drawCardToHand(c);
            }
            this.playersState.add(copy);
        }

        // Deep copy of deck
        this.playDeckState = new Deck();
        for (Card c : deck.getDeck()) {
            this.playDeckState.addCard(c);
        }

        // Deep copy of play pile
        this.playPileState = new Stack<>();
        for (Card c : pile) {
            this.playPileState.push(c);
        }

        this.direction = dir;
        this.skipCount = skip;
        this.hasActedThisTurn = acted;
        this.wildColorChoice = wildColor;
        this.waitingForColorSelection = waiting;
        this.isLightSide = hand.getCard().getActiveSide();
    }

    /**
     * Returns the saved {@link GameEvent} representing the state at this snapshot.
     *
     * @return the stored GameEvent
     */
    public GameEvent getPreviousHand() {
        return previousHand;
    }

    /**
     * Returns the index of the current player at the moment this snapshot was taken.
     *
     * @return the current player index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Returns a copied list of players and their hands.
     *
     * @return the saved player list
     */
    public List<Player> getPlayersState() {
        return playersState;
    }

    /**
     * Returns a deep-copied version of the play deck.
     *
     * @return the saved deck state
     */
    public Deck getPlayDeckState() {
        return playDeckState;
    }

    /**
     * Returns a copied version of the play pile.
     *
     * @return the saved play pile stack
     */
    public Stack<Card> getPlayPileState() {
        return playPileState;
    }

    /**
     * Returns the direction of play stored in this snapshot.
     *
     * @return the saved direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns how many players were scheduled to be skipped.
     *
     * @return the stored skip count
     */
    public int getSkipCount() {
        return skipCount;
    }

    /**
     * Returns whether the current player had already acted at the time of snapshot.
     *
     * @return true if the player acted, false otherwise
     */
    public boolean getHasActedThisTurn() {
        return hasActedThisTurn;
    }

    /**
     * Returns the selected wild color active at the time of this snapshot.
     *
     * @return the chosen wild color
     */
    public CardColor getWildColorChoice() {
        return wildColorChoice;
    }

    /**
     * Returns whether the game was waiting for a color selection.
     *
     * @return true if waiting, false otherwise
     */
    public boolean getWaitingForColorSelection() {
        return waitingForColorSelection;
    }

    /**
     * Returns whether the light side of cards was active when this snapshot was taken.
     *
     * @return true if light side was active
     */
    public boolean getIsLightSide() {
        return isLightSide;
    }

}
