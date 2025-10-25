
public class Card {
    private final Color color;
    private final CardType value;

    public Card(Color color, CardType cardType) {
        this.color = color;
        this.value = cardType;
    }

    // Determine if card can be played on the top of the stack
    public boolean playableOnTop(Card other) {
        return this.color == Color.WILD ||
                other.color == Color.WILD ||
                this.color == other.color ||
                this.value == other.value;
    }

    public CardType getValue() {
        return value;
    }

    public void printCard(){
        System.out.println(this.color + " " + this.value);
    }
}
