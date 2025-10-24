import java.util.*;

public class Deck {

    private final ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for (CardType type : CardType.values()) {

            //Light Side
            if (type.isNumberCard()) {
                // Number cards
                for (Color color : Color.values()) {
                    if (color.isLight()) {
                        deck.add(new Card(color, type));
                        deck.add(new Card(color, type));
                    }
                }
            }

            // Light action cards
            if (type == CardType.DRAW_ONE || type == CardType.LIGHT_REVERSE ||
                    type == CardType.SKIP || type == CardType.FLIP) {

                for (Color color : Color.values()) {
                    if (color.isLight()) {
                        deck.add(new Card(color, type));
                        deck.add(new Card(color, type));
                    }
                }
            }

            // Light wild cards
            if (type == CardType.WILD || type == CardType.WILD_DRAW_TWO) {
                for (int i = 0; i < 4; i++) {
                    deck.add(new Card(null, type));
                }
            }

            //Dark Side
            if (type.isNumberCard()) {
                for (Color color : Color.values()) {
                    if (color.isDark()) {
                        deck.add(new Card(color, type));
                        deck.add(new Card(color, type));
                    }
                }
            }

            // Dark action cards
            if (type == CardType.DRAW_FIVE || type == CardType.DARK_REVERSE ||
                    type == CardType.SKIP_EVERYONE || type == CardType.DARK_FLIP) {

                for (Color color : Color.values()) {
                    if (color.isDark()) {
                        deck.add(new Card(color, type));
                        deck.add(new Card(color, type));
                    }
                }
            }

            // Dark wild cards
            if (type == CardType.DARK_WILD || type == CardType.WILD_DRAW_COLOR) {
                for (int i = 0; i < 4; i++) {
                    deck.add(new Card(null, type));
                }
            }
        }

        Collections.shuffle(deck);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}