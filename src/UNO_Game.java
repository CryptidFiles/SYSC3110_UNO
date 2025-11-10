import java.util.*;

/**
 * Manages the overall UNO Flip gameplay loop and core mechanics.
 * This class handles player setup, deck management, card distribution,
 * turn progression, score tallying, and victory conditions.
 *
 * The UNO_Game class serves as the main controller for the game,
 * coordinating interactions between {@link Player}, {@link Card}, and {@link Deck}.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 1.0
 */
public class UNO_Game {
    private ArrayList<Player> players;
    private int numPlayers;

    private Deck playDeck;
    private Stack<Card> playPile;
    private Direction direction;

    private boolean gameOver;
    private Player gameWinningPlayer;
    private boolean roundOver;
    private Player roundWinningPlayer;
    final int WINNING_SCORE = 500;

    private int currentPlayerIndex;
    private int skipCount;

    // Removed scanner as we want to only use GUI


    /**
     * Creates a new UNO game instance, gathers player info, and starts the main loop.
     */
    public UNO_Game(int numPlayers, ArrayList<String> playerNames) {
        this.players = new ArrayList<>();
        this.numPlayers = numPlayers;
        this.playDeck = new Deck();
        this.playPile = new Stack<>();
        this.direction = Direction.CLOCKWISE;
        this.gameOver = false;
        this.roundOver = false;
        this.skipCount = 0;
        this.currentPlayerIndex = 0;

        // Initialize players from provided data
        for (String name : playerNames) {
            players.add(new Player(name));
        }

        players = new ArrayList<>();
        playDeck = new Deck();
        playPile = new Stack<>();
        direction = Direction.CLOCKWISE;
        gameOver = false;
        skipCount = 0;

        // Initialize players from provided data
        this.numPlayers = numPlayers;
        for (String name : playerNames) {
            players.add(new Player(name));
        }
    }


    /**
     * Starts a new round - called by controller
     */
    public void startNewRound() {
        resetDecks();
        distributeCards();
        currentPlayerIndex = 0;
        gameOver = false;
        roundOver = false;
        roundWinningPlayer = null;
        skipCount = 0;
    }

    /**
     * Reinitializes the draw deck and clears the play pile for a new round.
     */
    private void resetDecks() {
        playDeck = new Deck();  // fresh shuffled decks
        playPile = new Stack<>();
    }

    /**
     * Deals seven cards to each player and places the first non-action card on the play pile.
     */
    private void distributeCards() {
        for (Player player : players) {
            player.clearHand();
            for (int i = 0; i < 7; i++) {
                Card c = playDeck.drawCard();
                player.drawCard(c);
            }
        }

        // Set up first card. Keep drawing cards until we get a non-action card
        Card firstCard;
        do {
            firstCard = playDeck.drawCard();

            // If it's an action card, put it back and shuffle
            if (firstCard != null && firstCard.getType().isActionCard()) {
                playDeck.addCard(firstCard);  // Put it back in the deck
                playDeck.shuffle();           // Shuffle it back in
                firstCard = null;             // Reset so we continue looping
            }
        } while (firstCard == null);

        // We found a non-action card
        playPile.push(firstCard);
    }

    /**
     * Attempts to play a card for the current player.
     *
     * @param cardIndex The index of the card in the player's hand (1-based)
     * @return boolean true if the card was successfully played, false otherwise
     */
    public boolean playCard(int cardIndex) {
        Player currentPlayer = getCurrentPlayer();

        if (!validMove(currentPlayer, cardIndex)) {
            return false;
        }

        Card chosenCard = currentPlayer.playCard(cardIndex);
        playPile.push(chosenCard);
        currentPlayer.removeCard(cardIndex);

        // Execute card action
            chosenCard.action(this, currentPlayer);

        // Check for round win
        if (currentPlayer.handSize() == 0) {
            roundWinningPlayer = currentPlayer;
            roundOver = true;
            tallyScores(roundWinningPlayer);

            // Check for game win
            if (roundWinningPlayer.getScore() >= WINNING_SCORE) {
                gameWinningPlayer = roundWinningPlayer;
                gameOver = true;
            }
        } else {
            moveToNextPlayer();
        }

        return true;
    }

    /**
     * Current player draws a card from the deck.
     *
     * @return Card The drawn card, or null if deck is empty
     */
    public Card drawCard() {
        Player currentPlayer = getCurrentPlayer();

        // Automatic reshuffling if the drawn card leaves the deck empty
        if (playDeck.getDeck().isEmpty()) {
            reshuffleDrawingDeck();
            if (playDeck.getDeck().isEmpty()) {
                return null; // No cards available even after reshuffle
            }
        }

        Card drawnCard = playDeck.drawCard();
        if (drawnCard != null) {
            currentPlayer.drawCard(drawnCard);
        }

        moveToNextPlayer();
        return drawnCard;
    }


    private void moveToNextPlayer() {
        if (skipCount > 0) {
            processSkip();  // Handle skips automatically
        } else {
            // Normal turn progression
            if (direction == Direction.CLOCKWISE) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            } else {
                currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
            }
        }
    }

    /**   NUMBER OF PLAYERS AND NAME, MUST MAKE VIEW DO THIS
     * Prompts the user until a valid player count (2–4) is entered and stores it.

    public void numPlayers() {
        numPlayers = 0;
        while (true) {
            System.out.print("Enter the number of players (2-4): ");
            if (input.hasNextInt()) {
                numPlayers = input.nextInt();
                input.nextLine();
                if (numPlayers >= 2 && numPlayers <= 4) {
                    System.out.println("You entered: " + numPlayers);
                    break;
                } else {
                    System.out.println("Number of players must be between 2 and 4.");
                }
            } else {
                System.out.println("Please enter an integer");
                input.next();
            }

        }
    }

     * Collects and records each player's name based on the chosen player count.

    public void playerNames() {
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter a name for player " + (i + 1) + ": ");
            players.add(new Player(input.nextLine()));
        }
    }*/

    /**
     * @return Card, which is the card on top of the play pile
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

        // Save the top card of the play pile
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

        //System.out.println("Reshuffled the draw deck from play pile!");
    }


    /**
     * Determines whether the chosen card can be legally played on the current top card of the play Pile.
     *
     * @param p The player attempting the move.
     * @param  i The index of the chosen card in the player's hand.
     * @return boolean. true if the card is playable; false otherwise.
     */
    private boolean validMove(Player p, int i) { //returns true if players card matches type or color of the top card in play Pile
        return p.playCard(i).playableOnTop(topCard());
    }

    /**
     * Checks whether the player has at least one card that can be played on the top card.
     *
     * @param p The current player whose turn it is
     * @return boolean, true if he has any card in his hand that is playable, false other wise
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
    private void tallyScores(Player winner) {
        System.out.println("\n--- Scoreboard ---");

        // Tally winner's points from opponent's remaining cards
        for (Player p : players) {
            int handPoints = 0;

            if(p != winner) {
                for (Card c : p.getHand()) {
                    handPoints += c.getType().getPointValue(); //adds all players remaining card points to the winning player's score
                    System.out.println(handPoints + "from " + p.getName() + "'s hand");
                }
                winner.addScore(handPoints);
            }
        }

        /** MAKE THIS PART OF THE VIEW
         * Display the current scores of all players
        for (Player p : players) {
            System.out.println(p.getName() + " total score: " + p.getScore());

            // Assign the winner to anyone who has a total score of 500 or more points
            if(p.getScore() >= WINNING_SCORE){
                gameWinningPlayer = p;
            }
        }
        System.out.println("-------------------\n");


        if(gameWinningPlayer != null){
            System.out.println(gameWinningPlayer.getName() + " won!");
        }*/
    }

    /**
     * Returns the next player in turn order based on the current direction.
     *
     * @param currentPlayer The player whose successor is requested.
     * @return {@link Player}, the next Player in order.
     */
    public Player getNextPlayer(Player currentPlayer) {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int nextIndex;

        if (getDirection() == Direction.CLOCKWISE) {
            nextIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            nextIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }

        return players.get(nextIndex);
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
            System.out.println("Skipping " + skipCount + " player(s)!");

            for (int i = 0; i < skipCount; i++) {
                // Skip the current player by advancing the index
                if (direction == Direction.CLOCKWISE) {
                    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                } else {
                    currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
                }
            }

            skipCount = 0;
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
    }

    /**
     * Flips all cards (play pile, draw deck, and hands) to the opposite light/dark side.
     */
    public void flipGameSide() {

        boolean newSideState = !topCard().isLightSideActive;
        System.out.println("\n=== GAME FLIPPED TO " + (newSideState ? "DARK" : "LIGHT") + " SIDE ===");

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
     * @return {@link Deck} The active deck
     */
    public Deck getPlayDeck() {
        return playDeck;
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
     * @return true if playable based on UNO rules, false otherwise
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
