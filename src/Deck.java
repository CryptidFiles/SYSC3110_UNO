import java.util.*;

public class Deck {

    private ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for (CardType type : CardType.values()) {

            //Number cards
            if (type.isNumberCard()) {

                // For each color of specific number card
                for (Color color : Color.values()) {
                    if (color.isLight()) {
                        deck.add(new NumberCard(color, color.getDarkCounterpart(), type));
                        deck.add(new NumberCard(color, color.getDarkCounterpart(), type));
                    }
                }
            }

            // Reverse Card
            if (type.isActionCard()) {
                if (type == CardType.DRAW_ONE) {
                    for (Color color : Color.values()) {
                        deck.add(new DrawXCard(color, color.getDarkCounterpart()));
                        deck.add(new DrawXCard(color, color.getDarkCounterpart()));
                    }
                }

                if (type == CardType.LIGHT_REVERSE) {
                    for (Color color : Color.values()) {
                        deck.add(new ReverseCard(color, color.getDarkCounterpart()));
                        deck.add(new ReverseCard(color, color.getDarkCounterpart()));
                    }
                }

                if (type == CardType.SKIP) {
                    for (Color color : Color.values()) {
                        deck.add(new SkipCard(color, color.getDarkCounterpart()));
                        deck.add(new SkipCard(color, color.getDarkCounterpart()));
                    }
                }

                if (type == CardType.WILD) {
                    for (int i = 0; i < 4; i++) {
                        deck.add(new WildCard());
                        deck.add(new WildCard());
                    }
                }

                if (type == CardType.WILD_DRAW_TWO) {
                    for (int i = 0; i < 4; i++) {
                        deck.add(new WildDrawCard());
                        deck.add(new WildDrawCard());
                    }
                }
            }
        }

        Collections.shuffle(deck);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    // Basic drawCard method
    public Card drawCard() {
        if (deck.isEmpty()) {
            return null; // or handle reshuffling from discard pile
        }
        return deck.removeLast(); // Draw from top
    }

}