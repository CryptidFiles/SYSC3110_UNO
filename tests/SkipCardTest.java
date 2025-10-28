import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for SkipCard in isolation, without other classes.
 */
public class SkipCardTest {

    private SkipCard skipCard;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting SkipCard tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished SkipCard tests.");
    }

    @Before
    public void setUp() {
        skipCard = new SkipCard(Color.YELLOW, Color.ORANGE);
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
        assertEquals(Color.YELLOW, skipCard.lightColor);
        assertEquals(Color.ORANGE, skipCard.darkColor);
        assertEquals(CardType.SKIP, skipCard.lightType);
        assertEquals(CardType.SKIP_EVERYONE, skipCard.darkType);
        assertTrue(skipCard.isLightSideActive);
    }

    /**
     * Tests playableOnTop() returns true for cards with the same color.
     */
    @Test
    public void testPlayableOnTopSameColor() {
        SkipCard other = new SkipCard(Color.YELLOW, Color.PURPLE);
        assertTrue(skipCard.playableOnTop(other));
    }

    /**
     * Tests playableOnTop() returns true for cards with the same type.
     */
    @Test
    public void testPlayableOnTopSameType() {
        SkipCard other = new SkipCard(Color.GREEN, Color.TEAL);
        assertTrue(skipCard.playableOnTop(other)); // same type SKIP
    }

    /**
     * Tests playableOnTop() returns false for cards with different color and type.
     */
    @Test
    public void testPlayableOnTopDifferentColorAndType() {
        SkipCard other = new SkipCard(Color.BLUE, Color.PINK) {
            @Override
            public CardType getType() { return CardType.DRAW_ONE; }
        };
        assertFalse(skipCard.playableOnTop(other));
    }

    /**
     * Tests manual toggling of the light/dark side without calling action().
     */
    @Test
    public void testManualLightDarkSideToggle() {
        assertTrue(skipCard.isLightSideActive);
        skipCard.isLightSideActive = false;
        assertFalse(skipCard.isLightSideActive);
        skipCard.isLightSideActive = true;
        assertTrue(skipCard.isLightSideActive);
    }
}
