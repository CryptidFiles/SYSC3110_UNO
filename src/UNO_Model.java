import java.util.*;
import javax.swing.Timer;

/**
 * The UNO_Game class manages the core logic, rules, and state transitions
 * of the UNO Flip! game.
 * Acting as the "Model" in the MVC architecture, this class coordinates interactions
 * between {@link Player}, {@link Card}, and {@link Deck} objects, while notifying
 * attached {@link UNO_View} observers of state changes. It tracks player turns,
 * manages decks, enforces gameplay rules, calculates scores, and determines round
 * and game winners.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
public class UNO_Model {
    private ArrayList<Player> players; //a list of all players
    private int numPlayers; //how many players there are

    private Deck playDeck; //where we draw cards from
    private Stack<Card> playPile; //where we put played cards
    private Direction direction; //CLOCKWISE OR COUNTERCLOCKWISE

    private boolean gameOver; //is the whole game done? (a player reached 500 points?)
    private Player gameWinningPlayer; //who won the whole game

    private boolean roundOver; //is this round finished
    private Player roundWinningPlayer; //who won this round

    final int WINNING_SCORE = 200; //first to 500 wins the whole game

    private int currentPlayerIndex; //whose turn it is
    private int skipCount; //how many players to skip next time we move turns

    private boolean waitingForColorSelection; // For wild card implementations

    // Game state attributes
    private GameEvent.EventType lastEventType;
    private String statusMessage;
    private boolean shouldEnableNextPlayer;
    private boolean shouldEnableDrawButton;
    private Card lastPlayedCard;

    // GUI views that will display changes in the model
    private List<UNO_View> views;


    /**
     * Creates a new UNO Flip! game instance with the specified number of players and names.
     * Initializes all core data structures including players, decks, direction,
     * and game state variables.
     *
     * @param numPlayers the number of players (2 – 4)
     * @param playerNames a list of player names in turn order
     */
    public UNO_Model(int numPlayers, ArrayList<String> playerNames, ArrayList<Boolean> playerIsAI) {
        this.players = new ArrayList<>();
        this.numPlayers = numPlayers;
        this.playDeck = new Deck(); //a new shuffled deck
        this.playPile = new Stack<>(); //empty pile
        this.direction = Direction.CLOCKWISE;
        this.gameOver = false;
        this.roundOver = false;
        this.skipCount = 0;
        this.currentPlayerIndex = 0;
        views = new ArrayList<>();

        // Initialize players from provided data
        this.numPlayers = numPlayers;
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(playerNames.get(i), playerIsAI.get(i), new BasicAIStrategy()));
        }

        // Initialize new event tracking fields
        this.lastEventType = GameEvent.EventType.GAME_STATE_CHANGED;
        this.statusMessage = "Game started!";
        this.shouldEnableNextPlayer = false;
        this.shouldEnableDrawButton = false;
        this.lastPlayedCard = null;

        // Initiate the game
        startNewRound();
    }

    /**
     * Registers a {@link UNO_View} observer to receive updates when game state changes.
     *
     * @param view the view instance to add
     */
    public void addUnoView(UNO_View view){
        views.add(view);
    }

    /**
     * Removes a previously registered {@link UNO_View} observer.
     *
     * @param view the view instance to remove
     */
    public void removeUnoView(UNO_View view){
        views.remove(view);
    }

    // Replace all notify methods with this single method
    protected void notifyViews() {
        GameEvent event = new GameEvent(
                lastEventType,
                getCurrentPlayer(),        // currentPlayer
                roundWinningPlayer,        // winningPlayer (could be null)
                lastPlayedCard != null ? lastPlayedCard : topCard(), // card
                statusMessage,             // message
                direction,                 // direction
                shouldEnableNextPlayer,    // enableNextPlayer
                shouldEnableDrawButton     // enableDrawButton
        );

        for (UNO_View view : views) {
            view.handleGameEvent(event);
        }

        if(lastEventType != GameEvent.EventType.MESSAGE){
            // Reset temporary state if not a message notification
            resetEventState();
        }
    }

    /**
     * Resets the temporary event state after notification
     */
    private void resetEventState() {
        lastEventType = GameEvent.EventType.GAME_STATE_CHANGED;
        statusMessage = null;
        shouldEnableNextPlayer = false;
        shouldEnableDrawButton = false;
        lastPlayedCard = null;
    }


    /**
     * Sets up event for a specific type and message
     */
    protected void prepareEvent(GameEvent.EventType type, String message) {
        this.lastEventType = type;
        this.statusMessage = message;
    }

    /**
     * Only updates the game's message box for communication
     */
    protected void notifyMessage(String message) {
        this.statusMessage = message;
        notifyViews();
    }


    /**
     * Returns whether the game is waiting for color selection
     */
    public boolean isWaitingForColorSelection() {
        return waitingForColorSelection;
    }

    /**
     * Activates the wild color selection phase
     */
    public void triggerColorSelection() {
        this.waitingForColorSelection = true;
        prepareEvent(GameEvent.EventType.COLOR_SELECTION_NEEDED, null);
        notifyViews();
    }

    /**
     * Completes the color selection phase
     */
    public void completeColorSelection() {
        this.waitingForColorSelection = false;
        prepareEvent(GameEvent.EventType.GAME_STATE_CHANGED, null);
        notifyViews();
    }


    /**
     * Starts a new round of gameplay by resetting decks, distributing cards,
     * and resetting round status variables.
     * Called by the controller at the start of each round.
     */
    public void startNewRound() {
        resetDecks();
        distributeCards();
        currentPlayerIndex = 0;
        gameOver = false; //since we are doing a new round, whole game not done yet
        roundOver = false; //round just got activated
        roundWinningPlayer = null; //no winners yet
        skipCount = 0;

        // Update the game state
        prepareEvent(GameEvent.EventType.GAME_STATE_CHANGED, "New round started!");
        shouldEnableDrawButton = !hasPlayableHand(getCurrentPlayer());
        notifyViews();
    }

    /**
     * Reinitialize the draw deck and clears the play pile for a new round.
     */
    private void resetDecks() {
        playDeck = new Deck();  // fresh shuffled decks
        playPile = new Stack<>(); //empty discard pile
    }

    /**
     * Deals seven cards to each player and places the first non-action card on the play pile.
     */
    private void distributeCards() {
        for (Player player : players) {
            player.clearHand();
            for (int i = 0; i < 7; i++) { //deals 7 cards to each player
                try {
                    Card c = playDeck.drawCardFromDeck();
                    if (c == null) throw new NoSuchElementException("Deck is empty while distributing cards");
                    player.drawCardToHand(c);
                } catch (Exception e) {
                    prepareEvent(GameEvent.EventType.MESSAGE, "Error dealing cards: " + e.getMessage());
                    notifyViews();
                }
            }
        }

        // Set up first card. Keep drawing cards until we get a non-action card
        try {
            Card firstCard;
            do {
                firstCard = playDeck.drawCardFromDeck();

                // If it's an action card, put it back and shuffle
                if (firstCard != null && firstCard.getType().isActionCard()) {
                    playDeck.addCard(firstCard);  // Put it back in the deck
                    playDeck.shuffle();           // Shuffle it back in
                    firstCard = null;             // Reset so we continue looping
                }
            } while (firstCard == null);

            // We found a non-action card
            playPile.push(firstCard);
        } catch (Exception e) {
            prepareEvent(GameEvent.EventType.MESSAGE, "Error initializing first card: " + e.getMessage());
            notifyViews();
        }
    }


    /**
     * Attempts to play a card for the current player.
     *
     * @param cardIndex The index of the card in the player's hand (1-based)
     * @return boolean true if the card was successfully played, false otherwise
     */
    public boolean playCard(int cardIndex) {
        try {
            Player currentPlayer = getCurrentPlayer();

            if (!validMove(currentPlayer, cardIndex)) {
                return false;
            }

            Card chosenCard = currentPlayer.playCard(cardIndex);
            playPile.push(chosenCard);
            currentPlayer.removeCard(cardIndex);

            // Execute card action
            chosenCard.action(this, currentPlayer);

            // Set up card played event
            lastEventType = GameEvent.EventType.CARD_PLAYED;
            lastPlayedCard = chosenCard;
            shouldEnableNextPlayer = !waitingForColorSelection;
            shouldEnableDrawButton = false;

            // Check for round win
            if (currentPlayer.handSize() == 0) {
                roundWinningPlayer = currentPlayer;
                roundOver = true;
                tallyScores(roundWinningPlayer);

                prepareEvent(GameEvent.EventType.ROUND_WON,
                        roundWinningPlayer.getName() + " wins this round!");
                lastPlayedCard = null; // Use top card for display

                if (roundWinningPlayer.getScore() >= WINNING_SCORE) {
                    gameWinningPlayer = roundWinningPlayer;
                    gameOver = true;
                    prepareEvent(GameEvent.EventType.GAME_WON, null);
                } else {
                    prepareEvent(GameEvent.EventType.MESSAGE, "Starting new round!");
                }
            }

            notifyViews();
            return true;

        } catch (Exception e) {
            prepareEvent(GameEvent.EventType.MESSAGE, "Error playing card: " + e.getMessage());
            notifyViews();
            return false;
        }
    }


    /**
     * Current player draws a card from the deck. Either voluntary or because they do not have a playable hand
     *
     * @return {@link Card} The drawn card, or null if deck is empty
     */
    public Card drawCard() {
        try {
            Player currentPlayer = getCurrentPlayer();

            // If draw deck is empty, try to rebuild and reshuffle it from the play pile
            if (playDeck.getDeck().isEmpty()) {
                reshuffleDrawingDeck();
                if (playDeck.getDeck().isEmpty()) {
                    return null; // No cards available even after reshuffle
                }
            }


            //draw the card and give it to the current player
            Card drawnCard = playDeck.drawCardFromDeck();
            if (drawnCard != null) {
                currentPlayer.drawCardToHand(drawnCard);
            }

            // NOTIFY views as player has drawn a card and skipping current player's turn
            prepareEvent(GameEvent.EventType.GAME_STATE_CHANGED,
                    "Card drawn! Press 'Next Player' to continue.");
            shouldEnableNextPlayer = true;
            shouldEnableDrawButton = false;

            notifyViews();
            return drawnCard;
        } catch (Exception e) {
            prepareEvent(GameEvent.EventType.MESSAGE, "Error drawing card: " + e.getMessage());
            notifyViews();
            return null;
        }
    }


    /**
     * Advances the turn to the next player, taking into account skips and direction.
     * Notifies views after updating the current player index.
     */
    public void moveToNextPlayer() {
        // Reset next player button state BEFORE moving
        shouldEnableNextPlayer = false;

        if (skipCount > 0) {
            processSkip();  // jumps past players
        } else {
            // Normal turn progression
            if (direction == Direction.CLOCKWISE) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            } else {
                currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
            }
            notifyViews();  // Only notify once after the move
        }

        // After we have moved to next player
        Player newPlayer = getCurrentPlayer();
        boolean hasPlayableHand = hasPlayableHand(newPlayer);

        prepareEvent(GameEvent.EventType.PLAYER_CHANGED, null);
        shouldEnableNextPlayer = false; // Always false when switching players
        shouldEnableDrawButton = !hasPlayableHand && !newPlayer.isPlayerAI();

        notifyViews();

        if (newPlayer.isPlayerAI() && !isRoundOver() && !isGameOver()) {
            executeAITurn();
        }
    }


    public void executeAITurn() {
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer.isPlayerAI() && currentPlayer.getAIStrategy() != null) {
            AIStrategy strategy = currentPlayer.getAIStrategy();

            // Notify view to show AI "thinking"
            for (UNO_View view : views) {
                if (view instanceof UNO_Frame) {
                    ((UNO_Frame) view).showAITurnIndicator(currentPlayer);
                }
            }

            // Execute AI decision with delay of a second
            Timer timer = new Timer(strategy.getDelayMilliseconds(), e -> {
                Card topCard = topCard();
                int cardChoice = strategy.chooseCard(currentPlayer, topCard, this);

                // Show AI's selection
                for (UNO_View view : views) {
                    if (view instanceof UNO_Frame) {
                        Card selectedCard = cardChoice > 0 ? currentPlayer.getHand().get(cardChoice - 1) : null;
                        ((UNO_Frame) view).showAICardSelection(currentPlayer, cardChoice, selectedCard);
                    }
                }

                // Execute the chosen action
                if (cardChoice == 0) {
                    drawCard();
                    moveToNextPlayer();
                } else {
                    playCard(cardChoice);
                    // moveToNextPlayer will be called automatically if no color selection needed
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }




    /**
     * @return {@link Card}, which is the card on top of the play pile
     */
    public Card topCard() {
        return playPile.peek();
    }

    /**
     * Rebuilds the draw deck from the play pile while preserving the current top discard.
     */
    private void reshuffleDrawingDeck(){
        if (playPile.size() <= 1) {
            return; // Not enough cards to reshuffle (need at least 2: one to keep, one to reshuffle)
        }

        // removes and saves the top card of the play pile
        Card topCard = playPile.pop();

        // Add all remaining play pile cards back to the deck
        while (!playPile.isEmpty()) {
            Card card = playPile.pop();
            playDeck.addCard(card);
        }

        // Shuffle the deck
        playDeck.shuffle();

        // Put the saved top card back on the play pile
        playPile.push(topCard);

    }


    /**
     * Determines whether the chosen card can be legally played on the current top card of the play Pile.
     *
     * @param p The player attempting the move.
     * @param  i The index of the chosen card in the player's hand.
     * @return boolean. true if the card is playable; false otherwise.
     */
    public boolean validMove(Player p, int i) { //returns true if players card matches type or color of the top card in play Pile
        try {
            return p.playCard(i).playableOnTop(topCard());
        } catch (Exception e) {
            prepareEvent(GameEvent.EventType.MESSAGE, "Error validating top card: " + e.getMessage());
            notifyViews();
            return false;
        }
    }

    /**
     * Checks whether the player has at least one card that can be played on the top card.
     *
     * @param p The current player whose turn it is
     * @return boolean, true if he has any card in his hand that is playable, false otherwise
     */
    public boolean hasPlayableHand(Player p) {
        for (Card c : p.getHand()) {
            if(c.playableOnTop(topCard())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Totals opponents' remaining card values to the round winner and updates overall game status.
     *
     * @param winner The player who won the round.
     */
    public void tallyScores(Player winner) {
        // Tally winner's points from opponent's remaining cards
        for (Player p : players) {
            int handPoints = 0;

            if(p != winner) {
                for (Card c : p.getHand()) {
                    handPoints += c.getType().getPointValue(); //adds all players remaining card points to the winning player's score
                }
                winner.addScore(handPoints);
            }
        }
        prepareEvent(GameEvent.EventType.SCORES_UPDATED, null);
        notifyViews();
    }

    /**
     * Returns the next player in turn order based on the current direction.
     *
     * @param currentPlayer The player whose successor is requested.
     * @return {@link Player}, the next Player in order.
     */
    public Player getNextPlayer(Player currentPlayer) {
        int currentPlayerIndex = players.indexOf(currentPlayer); //gets the index of the current player
        int nextIndex;

        if (getDirection() == Direction.CLOCKWISE) {
            nextIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            nextIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }

        return players.get(nextIndex); //returns the player based on their index that was set by (nextindex)
    }


    /**
     * Increases the number of players to skip on the next skip processing.
     *
     * @param count The number of players to skip
     */
    public void addSkip(int count) {
        this.skipCount += count;
    }

    /**
     * Schedules a skip for all other players on the next skip processing.
     */
    public void skipAllPlayers() {
        // Skip all other players
        this.skipCount = players.size() - 1;
    }

    /**
     * Applies pending skips by advancing the current turn index, then clears the skip count.
     */
    public void processSkip() {
        if (skipCount > 0) {
            // Calculate the final position directly based on skip count and direction
            if (direction == Direction.CLOCKWISE) {
                // Skip N players means advance by (N + 1) positions
                // +1 to get to the first player to skip, +N to skip all of them
                currentPlayerIndex = (currentPlayerIndex + skipCount + 1) % players.size();
            } else {
                // Counter-clockwise: go backwards by (N + 1) positions
                currentPlayerIndex = (currentPlayerIndex - skipCount - 1 + players.size()) % players.size();
            }

            skipCount = 0;  // Reset skip count after processing
            notifyViews();   // Notify views once after all skips are processed
        }
    }




    /**
     * Returns the current direction of play.
     *
     * @return {@link Direction}. The current direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Toggles the direction of play between clockwise and counterclockwise.
     */
    public void flipDirection() {
        if (direction == Direction.CLOCKWISE){
            direction = Direction.COUNTERCLOCKWISE;
        }else{
            direction = Direction.CLOCKWISE;
        }
        prepareEvent(GameEvent.EventType.DIRECTION_FLIPPED,
                "Direction flipped to " + direction + "!");
        notifyViews();
    }

    /**
     * Flips all cards (play pile, draw deck, and hands) to the opposite light/dark side.
     */
    public void flipGameSide() {
        // Flip all cards in play pile (discard pile)
        for (Card card : playPile) {
            card.flip();
        }

        // Flip all cards in draw pile
        for (Card card : playDeck.getDeck()) {
            card.flip();
        }

        // Flip all cards in players' hands
        for (Player player : players) {
            for (Card card : player.getHand()) {
                card.flip();
            }
        }
        prepareEvent(GameEvent.EventType.GAME_STATE_CHANGED, "Game side flipped!");
        notifyViews();
    }





    // Getters for view and controller access

    /**
     * Returns the current player.
     *
     * @return {@link Player} The current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Returns all players in the game.
     *
     * @return ArrayList<Player> List of all players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns whether the game is over.
     *
     * @return boolean true if game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Returns whether the current round is over.
     *
     * @return boolean true if round is over
     */
    public boolean isRoundOver() {
        return roundOver;
    }

    /**
     * Returns the round winning player.
     *
     * @return Player The round winner, or null if round not over
     */
    public Player getRoundWinningPlayer() {
        return roundWinningPlayer;
    }

    /**
     * Returns the game winning player.
     *
     * @return Player The game winner, or null if game not over
     */
    public Player getGameWinningPlayer() {
        return gameWinningPlayer;
    }

    /**
     * Returns the draw deck used for gameplay.
     *
     * @return Deck The active deck
     */
    public Deck getPlayDeck() {
        return playDeck;
    }

    /**
     * Returns the play pile used for gameplay.
     *
     * @return Deck The active deck
     */
    public Stack<Card> getPlayPile() {
        return playPile;
    }

    /**
     * Returns the number of cards in the play pile.
     *
     * @return int Number of cards in play pile
     */
    public int getPlayPileSize() {
        return playPile.size();
    }

    /**
     * Checks whether a card can be legally played on the top card of the play pile.
     *
     * @param card The card the player wants to play
     * @return boolean. true if playable based on UNO rules, false otherwise
     */
    public boolean isPlayable(Card card) {
        if (card == null || playPile.isEmpty()) return false;

        Card top = topCard();

        // Wilds are always playable
        if (card.getColor() == CardColor.WILD) return true;

        // Match by color or type/value
        return card.getColor() == top.getColor() || card.getType() == top.getType();
    }
}
