import java.util.*;

public class Deck {

    private ArrayList<Card> light_deck;
    private ArrayList<Card> dark_deck;

    public Deck() {
        light_deck = new ArrayList<>();
        dark_deck = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for (CardType type : CardType.values()) {

            //Light Side
            if (type.isNumberCard()) {
                // Number cards
                for (Color color : Color.values()) {
                    if (color.isLight()) {
                        light_deck.add(new Card(color, type));
                        light_deck.add(new Card(color, type));
                    }
                }
            }

            // Light action cards
            if (type == CardType.DRAW_ONE || type == CardType.LIGHT_REVERSE ||
                    type == CardType.SKIP || type == CardType.FLIP) {

                for (Color color : Color.values()) {
                    if (color.isLight()) {
                        light_deck.add(new Card(color, type));
                        light_deck.add(new Card(color, type));
                    }
                }
            }

            // Light wild cards
            if (type == CardType.WILD || type == CardType.WILD_DRAW_TWO) {
                for (int i = 0; i < 4; i++) {
                    light_deck.add(new Card(Color.WILD, type));
                }
            }

            //Dark Side
            if (type.isNumberCard()) {
                for (Color color : Color.values()) {
                    if (color.isDark()) {
                        dark_deck.add(new Card(color, type));
                        dark_deck.add(new Card(color, type));
                    }
                }
            }

            // Dark action cards
            if (type == CardType.DRAW_FIVE || type == CardType.DARK_REVERSE ||
                    type == CardType.SKIP_EVERYONE || type == CardType.DARK_FLIP) {

                for (Color color : Color.values()) {
                    if (color.isDark()) {
                        dark_deck.add(new Card(color, type));
                        dark_deck.add(new Card(color, type));
                    }
                }
            }

            // Dark wild cards
            if (type == CardType.DARK_WILD || type == CardType.WILD_DRAW_COLOR) {
                for (int i = 0; i < 4; i++) {
                    dark_deck.add(new Card(Color.WILD, type));
                }
            }
        }

        Collections.shuffle(light_deck);
        Collections.shuffle(dark_deck);
    }

    public ArrayList<Card> getLight_Deck() {
        return light_deck;
    }

    public ArrayList<Card> getDark_Deck() {
        return dark_deck;
    }
}