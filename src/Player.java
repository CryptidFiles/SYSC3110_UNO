import java.util.*;

public class Player {
    private String name;
    private List<Card> hand;
    private int score;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void drawCard(Card drawnCard) {
        hand.add(drawnCard);
    }

    public Card playCard(int cardNumber) {
        return hand.get(cardNumber);
    }

}
