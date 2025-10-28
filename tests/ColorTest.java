import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for the Color enum.
 * This class tests helper methods like isLight(), isDark(), getDarkCounterpart(), and getLightCounterpart().
 */
public class ColorTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting Color tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished Color tests.");
    }

    @Before
    public void setUp() {
        System.out.println("Starting a test...");
    }

    @After
    public void tearDown() {
        System.out.println("Test finished.");
    }

    @Test
    public void testIsLight() {
        assertTrue(Color.RED.isLight());
        assertTrue(Color.BLUE.isLight());
        assertTrue(Color.YELLOW.isLight());
        assertTrue(Color.GREEN.isLight());

        assertFalse(Color.PINK.isLight());
        assertFalse(Color.TEAL.isLight());
        assertFalse(Color.ORANGE.isLight());
        assertFalse(Color.PURPLE.isLight());
        assertFalse(Color.WILD.isLight());
    }

    @Test
    public void testIsDark() {
        assertTrue(Color.PINK.isDark());
        assertTrue(Color.TEAL.isDark());
        assertTrue(Color.ORANGE.isDark());
        assertTrue(Color.PURPLE.isDark());

        assertFalse(Color.RED.isDark());
        assertFalse(Color.BLUE.isDark());
        assertFalse(Color.YELLOW.isDark());
        assertFalse(Color.GREEN.isDark());
        assertFalse(Color.WILD.isDark());
    }

    @Test
    public void testGetDarkCounterpart() {
        assertEquals(Color.PINK, Color.RED.getDarkCounterpart());
        assertEquals(Color.TEAL, Color.BLUE.getDarkCounterpart());
        assertEquals(Color.ORANGE, Color.YELLOW.getDarkCounterpart());
        assertEquals(Color.PURPLE, Color.GREEN.getDarkCounterpart());

        // Dark or wild colors return themselves
        assertEquals(Color.PINK, Color.PINK.getDarkCounterpart());
        assertEquals(Color.WILD, Color.WILD.getDarkCounterpart());
    }

    @Test
    public void testGetLightCounterpart() {
        assertEquals(Color.RED, Color.PINK.getLightCounterpart());
        assertEquals(Color.BLUE, Color.TEAL.getLightCounterpart());
        assertEquals(Color.YELLOW, Color.ORANGE.getLightCounterpart());
        assertEquals(Color.GREEN, Color.PURPLE.getLightCounterpart());

        // Light or wild colors return themselves
        assertEquals(Color.RED, Color.RED.getLightCounterpart());
        assertEquals(Color.WILD, Color.WILD.getLightCounterpart());
    }
}