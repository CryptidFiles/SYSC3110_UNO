import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for FlipCard in isolation, without other classes.
 */
public class FlipCardTest {

    private FlipCard flipCard;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting FlipCard tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished FlipCard tests.");
    }

    @Before
    public void setUp() {
        flipCard = new FlipCard(CardColor.BLUE, CardColor.TEAL);
    }

    @After
    public void tearDown() {
        System.out.println("Test finished.");
    }

    /**
     * Tests that the constructor correctly sets light and dark colors and types.
     */
    @Test
    public void testConstructorSetsColorsAndTypes() {
        assertEquals(CardColor.BLUE, flipCard.getColor());      // light side active
        assertEquals(CardType.FLIP, flipCard.getType());
        flipCard.flip();
        assertEquals(CardColor.TEAL, flipCard.getColor());      // dark side active
        assertEquals(CardType.DARK_FLIP, flipCard.getType());
    }

    /**
     * Tests that playableOnTop returns true for cards with the same color.
     */
    @Test
    public void testPlayableOnTopSameColor() {
        FlipCard other = new FlipCard(CardColor.BLUE, CardColor.PURPLE);
        assertTrue(flipCard.playableOnTop(other));
    }

    /**
     * Tests that playableOnTop returns true for cards with the same type.
     */
    @Test
    public void testPlayableOnTopSameType() {
        FlipCard other = new FlipCard(CardColor.GREEN, CardColor.PURPLE);
        assertTrue(flipCard.playableOnTop(other)); // same type FLIP
    }

    /**
     * Tests that playableOnTop returns false for cards with different color and type.
     */
    @Test
    public void testPlayableOnTopDifferentColorAndType() {
        FlipCard other = new FlipCard(CardColor.RED, CardColor.PINK) {
            @Override
            public CardType getType() { return CardType.SKIP; }
        };
        assertFalse(flipCard.playableOnTop(other));
    }

    /**
     * Tests manual toggling of the light/dark side without calling action().
     */
    @Test
    public void testManualLightDarkSideToggle() {
        assertTrue(flipCard.getActiveSide());
        flipCard.flip();
        assertFalse(flipCard.getActiveSide());
        flipCard.flip();
        assertTrue(flipCard.getActiveSide());
    }
}
