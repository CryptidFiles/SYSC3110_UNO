import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class WildDrawCardTest {

    private WildDrawCard card;

    /** Minimal fake model to track calls */
    private static class FakeModel extends UNO_Model {

        boolean triggered = false;
        boolean skipAdded = false;
        Player p1;
        Player p2;

        public FakeModel() {
            super(2,
                    new ArrayList<>(Arrays.asList("A", "B")),
                    new ArrayList<>(Arrays.asList(false, false))
            );

            p1 = getPlayers().get(0);
            p2 = getPlayers().get(1);

            // Ensure play pile has a valid card
            getPlayPile().push(new NumberCard(CardColor.RED, CardColor.BLUE, CardType.ONE));
        }

        @Override
        public void triggerColorSelection() {
            triggered = true;
        }

        @Override
        public void addSkip(int count) {
            skipAdded = true;
            super.addSkip(count);
        }
    }

    @Before
    public void setUp() {
        card = new WildDrawCard();
    }

    /**
     * Wild Draw cards are always playable.
     */
    @Test
    public void testPlayableOnTop() {
        Card c = new NumberCard(CardColor.RED, CardColor.BLUE, CardType.FOUR);
        assertTrue(card.playableOnTop(c));
    }

    /**
     * action() must trigger color selection phase.
     */
    @Test
    public void testActionTriggersColorSelection() {
        FakeModel model = new FakeModel();
        Player p = model.getCurrentPlayer();

        assertFalse(model.triggered);
        card.action(model, p);
        assertTrue(model.triggered);
    }

    /**
     * Light side: next player draws exactly 2 cards and is skipped.
     */
    @Test
    public void testExecuteDrawActionLightSide() {
        FakeModel model = new FakeModel();
        Player current = model.getPlayers().get(0);
        Player next = model.getPlayers().get(1);

        model.getPlayDeck().getDeck().clear();
        model.getPlayDeck().addCard(new WildDrawCard()); // Dummy draw
        model.getPlayDeck().addCard(new WildDrawCard()); // Same as above

        int before = next.handSize();

        card.executeDrawAction(CardColor.GREEN, true, model, current);

        assertEquals(before + 2, next.handSize());
        assertTrue(model.skipAdded);
        assertEquals(CardColor.GREEN, card.lightColor);
        assertEquals(CardColor.GREEN.getDarkCounterpart(), card.darkColor);
    }

    /**
     * Dark side: next player draws until matching color appears.
     */
    @Test
    public void testExecuteDrawActionDarkSide() {
        FakeModel model = new FakeModel();
        Player current = model.getPlayers().get(0);
        Player next = model.getPlayers().get(1);

        model.getPlayDeck().getDeck().clear();

        Card nonMatching = new NumberCard(CardColor.RED, CardColor.BLUE, CardType.FOUR);
        Card matching = new NumberCard(CardColor.PINK, CardColor.PURPLE, CardType.SEVEN);

        // Matching card must be added first so it is drawn second
        model.getPlayDeck().addCard(matching);
        model.getPlayDeck().addCard(nonMatching);

        int before = next.handSize();

        card.executeDrawAction(CardColor.PINK, false, model, current);

        assertEquals(before + 2, next.handSize());
        assertTrue(model.skipAdded);

        assertEquals(CardColor.PINK, card.darkColor);
        assertEquals(CardColor.PINK.getLightCounterpart(), card.lightColor);
    }
}