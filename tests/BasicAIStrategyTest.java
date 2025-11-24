import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 * Tests BasicAIStrategy decision logic for choosing cards and colors.
 */
public class BasicAIStrategyTest {

    private BasicAIStrategy strategy;
    private Player player;
    private UNO_Model model;

    @Before
    public void setUp() {
        strategy = new BasicAIStrategy(500);

        ArrayList<String> names = new ArrayList<>(Arrays.asList("AI"));
        ArrayList<Boolean> aiFlags = new ArrayList<>(Arrays.asList(true));
        model = new UNO_Model(1, names, aiFlags);

        player = model.getPlayers().get(0);
        player.clearHand();
    }

    /**
     * Ensures the AI returns zero when no playable card exists.
     */
    @Test
    public void testChooseCardReturnsZeroWhenNoPlayableCard() {
        Card top = new TestCard(CardColor.RED, CardType.ONE);

        // hand has only non matching colors
        player.drawCardToHand(new TestCard(CardColor.BLUE, CardType.TWO));
        player.drawCardToHand(new TestCard(CardColor.GREEN, CardType.THREE));

        int result = strategy.chooseCard(player, top, model);
        assertEquals(0, result);
    }

    /**
     * Ensures the AI selects the first playable card in the hand.
     */
    @Test
    public void testChooseCardReturnsFirstPlayable() {
        Card top = new TestCard(CardColor.RED, CardType.TWO);

        player.drawCardToHand(new TestCard(CardColor.GREEN, CardType.FOUR)); // not playable
        player.drawCardToHand(new TestCard(CardColor.RED, CardType.SEVEN));  // playable at index 1

        int result = strategy.chooseCard(player, top, model);
        assertEquals(2, result); // second card, 1 based index is 2
    }

    /**
     * Ensures wild color choice matches the most common color in the hand.
     */
    @Test
    public void testChooseWildColorPicksMostFrequent() {
        // light side colors
        player.drawCardToHand(new TestCard(CardColor.RED, CardType.THREE));
        player.drawCardToHand(new TestCard(CardColor.BLUE, CardType.FIVE));
        player.drawCardToHand(new TestCard(CardColor.RED, CardType.NINE));

        CardColor chosen = strategy.chooseWildColor(player, true);
        assertEquals(CardColor.RED, chosen);
    }

    /**
     * Ensures the fallback default color is used when no colored cards exist.
     */
    @Test
    public void testChooseWildColorUsesDefaultWhenOnlyWildCards() {
        player.drawCardToHand(new TestCard(CardColor.WILD, CardType.WILD));
        player.drawCardToHand(new TestCard(CardColor.WILD, CardType.WILD));

        CardColor chosen = strategy.chooseWildColor(player, true);
        assertEquals(CardColor.RED, chosen); // first light side color
    }

    /**
     * Ensures delay get and set methods behave correctly.
     */
    @Test
    public void testDelayMillisecondsGetterSetter() {
        strategy.setDelayMilliseconds(2500);
        assertEquals(2500, strategy.getDelayMilliseconds());
    }

    /**
     * Helper TestCard to avoid using actual game cards.
     */
    private static class TestCard extends Card {
        public TestCard(CardColor color, CardType type) {
            this.isLightSideActive = true;
            this.lightColor = color;
            this.lightType = type;
            this.darkColor = color;
            this.darkType = type;
        }

        @Override
        public boolean action(UNO_Model model, Player player) {
            return true;
        }

        @Override
        public boolean playableOnTop(Card other) {
            return this.getColor() == other.getColor() || this.getType() == other.getType();
        }
    }
}