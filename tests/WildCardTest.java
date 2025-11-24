import org.junit.*;
import static org.junit.Assert.*;

public class WildCardTest {

    private WildCard wildCard;

    /** Fake model to detect triggerColorSelection() */
    private static class FakeModel extends UNO_Model {
        boolean triggered = false;

        public FakeModel() {
            super(1,
                    new java.util.ArrayList<>(java.util.Arrays.asList("A")),
                    new java.util.ArrayList<>(java.util.Arrays.asList(false))
            );
            getPlayPile().push(new NumberCard(CardColor.RED, CardColor.BLUE, CardType.ONE));
        }

        @Override
        public void triggerColorSelection() {
            triggered = true;
        }
    }

    @Before
    public void setUp() {
        wildCard = new WildCard();
    }

    /**
     * Wild cards are always playable.
     */
    @Test
    public void testPlayableOnTop() {
        Card c = new NumberCard(CardColor.GREEN, CardColor.PINK, CardType.FOUR);
        assertTrue(wildCard.playableOnTop(c));
    }

    /**
     * action() must notify the model that color selection is required.
     */
    @Test
    public void testActionTriggersColorSelection() {
        FakeModel model = new FakeModel();
        Player p = model.getCurrentPlayer();

        assertFalse(model.triggered);
        wildCard.action(model, p);
        assertTrue(model.triggered);
    }

    /**
     * applyChosenColor updates both light and dark colors based on active side.
     */
    @Test
    public void testApplyChosenColorLightSide() {
        wildCard.isLightSideActive = true;

        wildCard.applyChosenColor(CardColor.GREEN, true);

        assertEquals(CardColor.GREEN, wildCard.lightColor);
        assertEquals(CardColor.GREEN.getDarkCounterpart(), wildCard.darkColor);
    }

    /**
     * applyChosenColor updates colors when dark side is active.
     */
    @Test
    public void testApplyChosenColorDarkSide() {
        wildCard.isLightSideActive = false;

        wildCard.applyChosenColor(CardColor.ORANGE, false);

        assertEquals(CardColor.ORANGE, wildCard.darkColor);
        assertEquals(CardColor.ORANGE.getLightCounterpart(), wildCard.lightColor);
    }
}