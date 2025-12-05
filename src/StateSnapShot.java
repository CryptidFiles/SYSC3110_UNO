import java.io.Serializable;
import java.util.*;

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

    public GameEvent getPreviousHand() {
        return previousHand;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public List<Player> getPlayersState() {
        return playersState;
    }

    public Deck getPlayDeckState() {
        return playDeckState;
    }

    public Stack<Card> getPlayPileState() {
        return playPileState;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public boolean getHasActedThisTurn() {
        return hasActedThisTurn;
    }

    public CardColor getWildColorChoice() {
        return wildColorChoice;
    }

    public boolean getWaitingForColorSelection() {
        return waitingForColorSelection;
    }

    public boolean getIsLightSide() {
        return isLightSide;
    }

}
