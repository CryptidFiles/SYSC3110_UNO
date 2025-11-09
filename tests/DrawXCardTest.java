import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link DrawXCard} in isolation, without other classes.
 */
public class DrawXCardTest {

    private DrawXCard drawCard;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting isolated DrawXCard tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished isolated DrawXCard tests.");
    }

    @Before
    public void setUp() {
        drawCard = new DrawXCard(Color.RED, Color.PINK);
    }

    @After
    public void tearDown() {
        System.out.println("Test finished.");
    }

    @Test
    public void testConstructorSetsColorsAndTypes() {
        assertEquals(Color.RED, drawCard.lightColor);
        assertEquals(Color.PINK, drawCard.darkColor);
        assertEquals(CardType.DRAW_ONE, drawCard.lightType);
        assertEquals(CardType.DRAW_FIVE, drawCard.darkType);
        assertTrue(drawCard.isLightSideActive);
    }

    @Test
    public void testPlayableOnTopSameColor() {
        DrawXCard other = new DrawXCard(Color.RED, Color.PINK);
        assertTrue(drawCard.playableOnTop(other));
    }

    @Test
    public void testPlayableOnTopSameType() {
        DrawXCard other = new DrawXCard(Color.BLUE, Color.TEAL);
        assertTrue(drawCard.playableOnTop(other)); // same type DRAW_ONE
    }

    @Test
    public void testPlayableOnTopDifferentColorAndType() {
        DrawXCard other = new DrawXCard(Color.GREEN, Color.PURPLE) {
            @Override
            public CardType getType() { return CardType.SKIP; }
        };
        assertFalse(drawCard.playableOnTop(other));
    }

    @Test
    public void testSwitchLightDarkSide() {
        drawCard.isLightSideActive = false;
        assertFalse(drawCard.isLightSideActive);
        drawCard.isLightSideActive = true;
        assertTrue(drawCard.isLightSideActive);
    }
}
