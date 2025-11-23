import java.util.HashMap;
import java.util.Map;

/**
 * Represents a game state change event that gets passed from Model to View.
 */
public class GameEvent {
    public enum EventType {
        GAME_STATE_CHANGED,      // General state update
        CARD_PLAYED,             // A card was played
        CARD_DRAWN,              // A card was voluntarily drawn
        ROUND_WON,               // Round ended with winner
        GAME_WON,                // Game ended with winner
        MESSAGE,                 // Status message to display
        COLOR_SELECTION_NEEDED,  // Wild card color selection
        PLAYER_CHANGED,          // Current player changed
        DIRECTION_FLIPPED,       // Play direction changed
        SCORES_UPDATED           // Scores were updated
    }

    private final EventType type;
    private final Player currentPlayer;
    private final Player winningPlayer;
    private final Card card;
    private final String message;
    private final Direction direction;
    private final boolean enableNextPlayer;
    private final boolean enableDrawButton;
    private final Map<String, Object> data;

    public GameEvent(EventType type, Player currentPlayer, Player winningPlayer,
                     Card card, String message, Direction direction,
                     boolean enableNextPlayer, boolean enableDrawButton) {
        this.type = type;
        this.currentPlayer = currentPlayer;
        this.winningPlayer = winningPlayer;
        this.card = card;
        this.message = message;
        this.direction = direction;
        this.enableNextPlayer = enableNextPlayer;
        this.enableDrawButton = enableDrawButton;
        this.data = new HashMap<>();
    }

    // Getters
    public EventType getType() { return type; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getWinningPlayer() { return winningPlayer; }
    public Card getCard() { return card; }
    public String getMessage() { return message; }
    public Direction getDirection() { return direction; }
    public boolean isEnableNextPlayer() { return enableNextPlayer; }
    public boolean isEnableDrawButton() { return enableDrawButton; }

    // Data methods for additional properties
    public Object getData(String key) { return data.get(key); }
    public void setData(String key, Object value) { data.put(key, value); }
    public boolean getBooleanData(String key) {
        return data.containsKey(key) && (Boolean) data.get(key);
    }
}