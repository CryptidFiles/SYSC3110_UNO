import java.io.Serializable;

public class StateSnapShot implements Serializable {
    private GameEvent previousHand;
    private int currentPlayerIndex;
    private GameEvent.EventType actionType;

    public StateSnapShot(GameEvent hand,  int currentPlayerIndex) {
        this.previousHand = hand;
        this.currentPlayerIndex = currentPlayerIndex;
        //this.actionType = actionType;
    }

    public GameEvent getPreviousHand() {
        return previousHand;
    }
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    public GameEvent.EventType getActionType() { return actionType; }
}
