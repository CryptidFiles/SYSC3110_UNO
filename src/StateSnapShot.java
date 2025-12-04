public class StateSnapShot {
    private GameEvent previousHand;
    private int currentPlayerIndex;

    public StateSnapShot(GameEvent hand,  int currentPlayerIndex) {
        previousHand = hand;
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public GameEvent getPreviousHand() {
        return previousHand;
    }
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
