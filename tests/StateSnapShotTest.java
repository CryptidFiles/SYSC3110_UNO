import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.*;

public class StateSnapShotTest {

    private UNO_Model game;

    @Before
    public void setUp() {
        ArrayList<String> names = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        ArrayList<Boolean> playerIsAI = new ArrayList<>(Arrays.asList(false, false));
        game = new UNO_Model(2, names, playerIsAI);
        game.startNewRound();
    }

    /**
     * Tests that a snapshot captures important parts of the current state.
     */
    @Test
    public void testSnapshotCapturesCoreState() {
        game.setDirection(Direction.COUNTERCLOCKWISE);
        game.setSkipCount(2);
        game.setHasActedThisTurn(true);
        game.setWildColorChoice(CardColor.BLUE);

        GameEvent event = game.createNewGameEvent();

        StateSnapShot snap = new StateSnapShot(
                event,
                game.getCurrentPlayerIndex(),
                game.getPlayers(),
                game.getPlayDeck(),
                game.getPlayPile(),
                game.getDirection(),
                game.getSkipCount(),
                game.hasActedThisTurn(),
                game.getWildColorChoice(),
                game.isWaitingForColorSelection()
        );

        assertEquals(game.getCurrentPlayerIndex(), snap.getCurrentPlayerIndex());
        assertEquals(game.getDirection(), snap.getDirection());
        assertEquals(game.getSkipCount(), snap.getSkipCount());
        assertEquals(game.hasActedThisTurn(), snap.getHasActedThisTurn());
        assertEquals(game.getWildColorChoice(), snap.getWildColorChoice());

        assertEquals(game.topCard().getActiveSide(), snap.getIsLightSide());

        assertEquals(game.getPlayers().size(), snap.getPlayersState().size());
        assertEquals(game.getPlayDeck().getDeck().size(), snap.getPlayDeckState().getDeck().size());
        assertEquals(game.getPlayPileSize(), snap.getPlayPileState().size());
    }

    /**
     * Tests that a snapshot is independent from later mutations on the model.
     */
    @Test
    public void testSnapshotIndependentFromModelMutations() {
        GameEvent event = game.createNewGameEvent();

        StateSnapShot snap = new StateSnapShot(
                event,
                game.getCurrentPlayerIndex(),
                game.getPlayers(),
                game.getPlayDeck(),
                game.getPlayPile(),
                game.getDirection(),
                game.getSkipCount(),
                game.hasActedThisTurn(),
                game.getWildColorChoice(),
                game.isWaitingForColorSelection()
        );

        int originalModelHand = game.getPlayers().get(0).handSize();
        int originalSnapHand = snap.getPlayersState().get(0).handSize();

        int originalModelDeckSize = game.getPlayDeck().getDeck().size();
        int originalSnapDeckSize = snap.getPlayDeckState().getDeck().size();

        int originalModelPileSize = game.getPlayPileSize();
        int originalSnapPileSize = snap.getPlayPileState().size();

        Player p0 = game.getPlayers().get(0);
        p0.drawCardToHand(new TestCard(CardColor.RED, CardType.FIVE));
        game.getPlayDeck().drawCardFromDeck();
        game.getPlayPile().push(new TestCard(CardColor.BLUE, CardType.SEVEN));

        assertEquals(originalSnapHand, snap.getPlayersState().get(0).handSize());
        assertEquals(originalSnapDeckSize, snap.getPlayDeckState().getDeck().size());
        assertEquals(originalSnapPileSize, snap.getPlayPileState().size());

        assertEquals(originalModelHand + 1, p0.handSize());
        assertEquals(originalModelDeckSize - 1, game.getPlayDeck().getDeck().size());
        assertEquals(originalModelPileSize + 1, game.getPlayPileSize());
    }

    /**
     * Simple test card used only in snapshot tests.
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
        public boolean playableOnTop(Card otherCard) {
            return this.getColor() == otherCard.getColor() || this.getType() == otherCard.getType();
        }
    }
}