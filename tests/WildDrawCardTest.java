import org.junit.*;
import static org.junit.Assert.*;

public class WildDrawCardTest {

    private WildDrawCard wildDrawCard;
    private Card dummyCard;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting WildDrawCard tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished WildDrawCard tests.");
    }

    @Before
    public void setUp() {
        wildDrawCard = new WildDrawCard();
        dummyCard = new NumberCard(Color.RED, Color.BLUE, CardType.ONE);
        System.out.println("Test setup completed.");
    }

    @After
    public void tearDown() {
        wildDrawCard = null;
        dummyCard = null;
        System.out.println("Test teardown completed.");
    }

    /**
     * Unit test for playableOnTop method.
     * WildDrawCard can always be played on top of any card.
     */
    @Test
    public void testPlayableOnTop() {
        assertTrue(wildDrawCard.playableOnTop(dummyCard));
        System.out.println("testPlayableOnTop passed.");
    }
}
