import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for ReverseCard in isolation, without other classes.
 */
public class ReverseCardTest {

    private ReverseCard reverseCard;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting ReverseCard tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished ReverseCard tests.");
    }

    @Before
    public void setUp() {
        reverseCard = new ReverseCard(CardColor.RED, CardColor.PINK);
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
        assertEquals(CardColor.RED, reverseCard.lightColor);
        assertEquals(CardColor.PINK, reverseCard.darkColor);
        assertEquals(CardType.LIGHT_REVERSE, reverseCard.lightType);
        assertEquals(CardType.DARK_REVERSE, reverseCard.darkType);
        assertTrue(reverseCard.isLightSideActive);
    }

    /**
     * Tests playableOnTop() returns true for cards with the same color.
     */
    @Test
    public void testPlayableOnTopSameColor() {
        ReverseCard other = new ReverseCard(CardColor.RED, CardColor.TEAL);
        assertTrue(reverseCard.playableOnTop(other));
    }

    /**
     * Tests playableOnTop() returns true for cards with the same type.
     */
    @Test
    public void testPlayableOnTopSameType() {
        ReverseCard other = new ReverseCard(CardColor.BLUE, CardColor.PURPLE);
        assertTrue(reverseCard.playableOnTop(other)); // same type LIGHT_REVERSE
    }

    /**
     * Tests playableOnTop() returns false for cards with different color and type.
     */
    @Test
    public void testPlayableOnTopDifferentColorAndType() {
        ReverseCard other = new ReverseCard(CardColor.GREEN, CardColor.ORANGE) {
            @Override
            public CardType getType() { return CardType.SKIP; }
        };
        assertFalse(reverseCard.playableOnTop(other));
    }

    /**
     * Tests manual toggling of the light/dark side without calling action().
     */
    @Test
    public void testManualLightDarkSideToggle() {
        assertTrue(reverseCard.isLightSideActive);
        reverseCard.isLightSideActive = false;
        assertFalse(reverseCard.isLightSideActive);
        reverseCard.isLightSideActive = true;
        assertTrue(reverseCard.isLightSideActive);
    }
}
