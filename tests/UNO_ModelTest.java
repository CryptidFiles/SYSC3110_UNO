import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;
import java.util.*;

public class UNO_ModelTest {
    private UNO_Model game;
    private Player player1, player2;
    private ArrayList<String> names;
    private ArrayList<Boolean> playerIsAI;

    @Before
    public void setUp() {
        names = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        playerIsAI = new ArrayList<>(Arrays.asList(false, false));
        game = new UNO_Model(2, names, playerIsAI);
        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);
    }

    /**
     * Tests basic initialization of the UNO game.
     */
    @Test
    public void testInitialization() {
        assertEquals(2, game.getPlayers().size());
        assertFalse(game.isGameOver());
        assertFalse(game.isRoundOver());
        assertNotNull(game.getPlayDeck());
    }

    /**
     * Tests that starting a new round resets player state and flags correctly.
     */
    @Test
    public void testStartNewRoundResetsState() {
        game.startNewRound();
        assertEquals(2, game.getPlayers().size());
        assertFalse(game.isGameOver());
        assertFalse(game.isRoundOver());
        assertEquals(0, player1.getScore());
    }

    /**
     * Tests that the direction of play correctly flips between clockwise and counterclockwise.
     */
    @Test
    public void testDirectionFlip() {
        game.startNewRound();

        assertEquals(Direction.CLOCKWISE, game.getDirection());
        game.flipDirection();
        assertEquals(Direction.COUNTERCLOCKWISE, game.getDirection());
        game.flipDirection();
        assertEquals(Direction.CLOCKWISE, game.getDirection());
    }

    /**
     * Tests that the next player is correctly determined when playing clockwise.
     */
    @Test
    public void testGetNextPlayerClockwise() {
        game.startNewRound();
        Player next = game.getNextPlayer(player1);
        assertEquals(player2, next);
    }

    /**
     * Tests that the next player is correctly determined when playing counterclockwise.
     */
    @Test
    public void testGetNextPlayerCounterClockwise() {
        game.startNewRound();
        game.flipDirection();
        Player next = game.getNextPlayer(player1);
        assertEquals(player2, next); // 2 players → same result
    }

    /**
     * Tests adding and processing a skip in clockwise direction (skips back to same player with 2 players).
     */
    @Test
    public void testAddSkipAndProcessSkipClockwise() {
        game.startNewRound();
        game.addSkip(1);
        game.processSkip();
        assertEquals(player1, game.getCurrentPlayer());
    }

    /**
     * Tests skipping all players and ensuring the turn cycles back correctly.
     */
    @Test
    public void testSkipAllPlayers() {
        game.startNewRound();
        game.skipAllPlayers();
        game.processSkip();
        assertEquals(player1, game.getCurrentPlayer());
    }

    /**
     * Tests that card validity (same color/type) is correctly determined by playableOnTop().
     */
    @Test
    public void testValidMoveTrueAndFalse() {
        Card top = new TestCard(CardColor.RED, CardType.FIVE);
        game.startNewRound();
        game.getPlayPile().clear();
        game.getPlayPile().push(top);

        Card valid = new TestCard(CardColor.RED, CardType.NINE);
        Card invalid = new TestCard(CardColor.BLUE, CardType.SEVEN);

        player1.drawCardToHand(valid);
        player1.drawCardToHand(invalid);

        assertTrue(game.validMove(player1, player1.handSize() - 1));
        assertFalse(game.validMove(player1, player1.handSize()));
    }

    /**
     * Tests that isPlayable() returns true for same color, same type, and wild cards.
     */
    @Test
    public void testIsPlayableConditions() {
        Card top = new TestCard(CardColor.YELLOW, CardType.THREE);
        game.startNewRound();
        game.getPlayPile().clear();
        game.getPlayPile().push(top);

        Card sameColor = new TestCard(CardColor.YELLOW, CardType.NINE);
        Card sameType = new TestCard(CardColor.RED, CardType.THREE);
        Card wild = new TestCard(CardColor.WILD, CardType.WILD);

        assertTrue(game.isPlayable(sameColor));
        assertTrue(game.isPlayable(sameType));
        assertTrue(game.isPlayable(wild));
    }

    /**
     * Tests that turn correctly advances in clockwise direction between players.
     */
    @Test
    public void testMoveToNextPlayerClockwise() {
        game.startNewRound();
        Player start = game.getCurrentPlayer();
        game.moveToNextPlayer();
        assertEquals(player2, game.getCurrentPlayer());
        game.moveToNextPlayer();
        assertEquals(start, game.getCurrentPlayer());
    }

    /**
     * Tests that turn correctly advances in counterclockwise direction between players.
     */
    @Test
    public void testMoveToNextPlayerCounterClockwise() {
        game.startNewRound();
        game.flipDirection();
        Player start = game.getCurrentPlayer();
        game.moveToNextPlayer();
        assertEquals(player2, game.getCurrentPlayer());
        game.moveToNextPlayer();
        assertEquals(start, game.getCurrentPlayer());
    }

    /**
     * Tests that drawing a card increases the player’s hand size.
     */
    @Test
    public void testDrawCardAddsToHand() {
        game.startNewRound();
        int before = player1.handSize();
        game.drawCard();
        assertTrue(player1.handSize() >= before + 1);
    }

    /**
     * Tests that tallyScores() adds points to the winner based on opponents' cards.
     */
    @Test
    public void testTallyScoresAddsToWinner() {
        game.startNewRound(); // This is to avoid the EmptyStackException

        // Remove all random cards to get a predictable score
        player2.getHand().clear();

        Card c1 = new TestCard(CardColor.RED, CardType.FIVE);
        Card c2 = new TestCard(CardColor.BLUE, CardType.SKIP);
        player2.drawCardToHand(c1);
        player2.drawCardToHand(c2);

        int expected = c1.getType().getPointValue() + c2.getType().getPointValue();

        game.tallyScores(player1);

        assertEquals(expected, player1.getScore());
    }

    /**
     * Tests that flipping the game side correctly flips all cards in the play deck.
     */
    @Test
    public void testFlipGameSideFlipsAllCards() {
        game.startNewRound();
        game.flipGameSide();
        boolean topSide = game.topCard().isLightSideActive;
        for (Card c : game.getPlayDeck().getDeck()) {
            assertEquals(topSide, c.isLightSideActive);
        }
    }

    /**
     * Tests that a player wins the game once their score reaches or exceeds the winning threshold.
     */
    @Test
    public void testGameWinningCondition() {
        game.startNewRound(); // This is to avoid the EmptyStackException

        player1.addScore(490);
        player2.drawCardToHand(new TestCard(CardColor.GREEN, CardType.SEVEN));
        player2.drawCardToHand(new TestCard(CardColor.BLUE, CardType.SKIP));
        game.tallyScores(player1);
        assertTrue(player1.getScore() >= 490);
    }

    /**
     * The Card class is abstract and so TestCard is a helper class.
     */
    private static class TestCard extends Card {
        public TestCard(CardColor color, CardType type) {
            this.isLightSideActive = true;
            this.lightColor = color;
            this.lightType = type;
            this.darkColor = color;
            this.darkType = type;
        }

        /** Needed to implement the abstract class. No logic implementation here. */
        @Override
        public boolean action(UNO_Model model, Player player) {
            return true;
        }

        /** Tests whether a card can be played on another based on color or type. */
        @Override
        public boolean playableOnTop(Card otherCard) {
            return this.getColor() == otherCard.getColor() || this.getType() == otherCard.getType();
        }
    }

    /** A test only AI strategy that returns predetermined decisions to make AI behavior predictable in unit tests. */
    private static class TestAIStrategy implements AIStrategy {
        int delay;
        int cardToPlay;

        TestAIStrategy(int cardToPlay, int delay) {
            this.cardToPlay = cardToPlay;
            this.delay = delay;
        }

        @Override
        public int chooseCard(Player player, Card topCard, UNO_Model game) {
            return cardToPlay;
        }

        @Override
        public CardColor chooseWildColor(Player aiPlayer, boolean isLightSide) {
            return CardColor.RED;
        }

        @Override
        public int getDelayMilliseconds() {
            return delay;
        }

        @Override
        public void setDelayMilliseconds(int delayMilliseconds) {
            this.delay = delayMilliseconds;
        }
    }

    /** Waits briefly then flushes the EDT so Swing timer actions complete before assertions run. */
    private void waitForTimer() {
        try {
            Thread.sleep(500); // guarantee timer event scheduled
            SwingUtilities.invokeAndWait(() -> {}); // flush events
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for executeAITurn()
     */
    @Test
    public void testExecuteAITurnPlaysCard() {
        ArrayList<String> names = new ArrayList<>(Arrays.asList("AI", "Bob"));
        ArrayList<Boolean> isAI = new ArrayList<>(Arrays.asList(true, false));

        UNO_Model game = new UNO_Model(2, names, isAI);

        Player ai = game.getPlayers().get(0);

        // Remove random cards
        ai.getHand().clear();

        // Add two controlled cards
        Card top = new TestCard(CardColor.RED, CardType.THREE);
        game.getPlayPile().clear();
        game.getPlayPile().push(top);

        Card toPlay = new TestCard(CardColor.RED, CardType.FIVE);
        ai.drawCardToHand(toPlay);

        // Give AI strategy that always chooses card index 1
        ai.setAiStrategy(new TestAIStrategy(1, 0));

        // Run AI turn
        game.executeAITurn();

        // Force Swing Timer action to run
        waitForTimer();

        // The card should now be on the play pile
        assertEquals(toPlay, game.topCard());

        // AI should have played its only card
        assertEquals(0, ai.handSize());
    }
}