import java.util.*;

public class UNO_Game {
    private ArrayList<Player> players;
    private Player currentPlayer;
    private Deck deck;
    private Direction direction;
    private boolean gameOver;
    private Card topCard;


    public boolean isValidPlay(Card card) {
        return card.playableOnTop(topCard);
    }

    public void nextTurn() {
    }

    public void handleSpecialCard(Card card) {
        switch(card.getValue()) {
            case SKIP -> nextTurn(); // Skip next player

            case REVERSE ->
                    this.direction = (this.direction == Direction.CLOCKWISE)
                    ? Direction.COUNTERCLOCKWISE
                    : Direction.CLOCKWISE;

            case DRAW_TWO -> {
                // Next player draws 2 cards
                //Player nextPlayer;
                //nextPlayer.drawCardToHand(deck.drawCard());
                //nextPlayer.drawCardToHand(deck.drawCard());
                //nextTurn(); // Skip their turn
            }
            case WILD_DRAW_FOUR -> {
                // Repeat Draw Two code but also change top card's color
                // Ensure the color can be chosen by player
            }
        }
    }
}
