import java.util.*;

public class UNO_Game {
    private ArrayList<Player> players;
    private int numPlayers;

    private Deck playDeck;
    private Stack<Card> playPile;
    private Direction direction;

    private boolean gameOver;
    private Player roundWinningPlayer;
    private Player gameWinningPlayer;
    final int WINNING_SCORE = 500;

    private int currentPlayerIndex;
    private int skipCount;

    private Scanner input = new Scanner(System.in);


    public UNO_Game() {
        players = new ArrayList<>();
        playDeck = new Deck();
        playPile = new Stack<>();
        direction = Direction.CLOCKWISE;
        gameOver = false;
        skipCount = 0;

        numPlayers();
        playerNames();
        gameLoop(); // main game loop
    }

    private void gameLoop() {
        boolean playAgain = true;

        while (playAgain) {
            resetDecks();
            distributeCards();
            playGame();

            System.out.println("\n--- Round Over ---");
            tallyScores(roundWinningPlayer);

            System.out.print("Do you want to play again? (y/n): ");
            String choice = input.nextLine().trim().toLowerCase(); //Defensive programming right here
            playAgain = choice.equals("y");
        }

        System.out.println("\nThanks for playing UNO Flip!");
    }

    private void resetDecks() {
        playDeck = new Deck();  // fresh shuffled decks
        playPile = new Stack<>();
    }

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

    public void playerNames() {
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter a name for player " + (i + 1) + ": ");
            players.add(new Player(input.nextLine()));
        }
    }

    public Card topCard() {
        return playPile.peek();
    }

    //where all actions happen, player turns, playing cards and detecting if someone won
    private void playGame() {
        currentPlayerIndex = 0; //start from player 0
        gameOver = false;

        //keeps looping over all players until one runs out of cards and win
        while (!gameOver) {
            // Reshuffle the play pile into the deck if the deck is empty
            if(playDeck.getDeck().isEmpty()) {
                reshuffleDrawingDeck();
            }

            // If any pending skips from the previous player's action occurred
            // then apply the number of skips to player index
            if(skipCount > 0){
                processSkip();
            }

            // displays whose turn it is
            Player player = players.get(currentPlayerIndex);
            System.out.println("\n" + player.getName() + "'s turn");

            int chosenIndex;
            while (true) {
                // Print out the top card of the play pile so player knows what color
                System.out.print("Top Card:\t");
                topCard().printCard();
                System.out.println();

                // If the player cannot play with their hand, draw a card and end turn
                if(!playableHand(player)){
                    System.out.println("You do not have a playable hand! " + player.getName() + " draws a card.");

                    Card card = playDeck.drawCard();
                    if (card == null) break;

                    card.printCard();
                    player.drawCard(card);
                    break; // End the turn of the player
                }


                chosenIndex = player.takeTurn(input); //asks current player which card to play acc to index

                // If the user decides to draw a card this turn (enter 0), draw from the deck and end turn
                if (chosenIndex == 0) {
                    System.out.println(player.getName() + " decided to draw a ");

                    Card card = playDeck.drawCard();
                    if (card == null) break;

                    card.printCard();
                    player.drawCard(card);
                    break; // End the turn of the player
                }

                // If a valid card input, get the card from the hand but not remove it yet
                Card chosenCard = player.playCard(chosenIndex);

                if (validMove(player, chosenIndex)) { //checks if card valid (matches color type on top of play pile
                    playPile.push(chosenCard); //pushes that card to the play pile
                    System.out.print(player.getName() + " played:  ");//displays that the player played and finished his turn
                    chosenCard.printCard(); //prints the card played
                    System.out.println();

                    chosenCard.action(this, player); //executes the effect of the card

                    // simulate removing that card from hand
                    player.removeCard(chosenIndex);

                    // if player ran out of cards — they win the round
                    if (player.handSize() == 0) {
                        System.out.println("\n" + player.getName() + " wins ");
                        roundWinningPlayer = player;
                        gameOver = true;
                        break; //someone won
                    }
                    break; // no one won but player's turn finished
                    //if neither of above breaks happen, move to next player
                } else {
                    System.out.println("Invalid move. Try again.\n"); //if chose invalid card, prompts user to try again
                }
            }

            if (gameOver) break; //if game over do not have to go to next player, just breaks

            if (direction == Direction.CLOCKWISE) {
                currentPlayerIndex = (currentPlayerIndex+ 1) % players.size();
            } else {
                currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
            }
        }
    }


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

        System.out.println("Reshuffled the draw deck from play pile!");
    }



    private boolean validMove(Player p, int i) { //returns true if players card matches type or color of the top card in play Pile
        return p.playCard(i).playableOnTop(topCard());
    }

    private boolean playableHand(Player p) {
        for (Card c : p.getHand()) {
            if(c.playableOnTop(topCard())) {
                return true;
            }
        }
        return false;
    }

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

        // Display the current scores of all players
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
        }
    }

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

    public Direction getDirection() {
        return direction;
    }

    public void flipDirection() {
        if (direction == Direction.CLOCKWISE){
            direction = Direction.COUNTERCLOCKWISE;
        }else{
            direction = Direction.CLOCKWISE;
        }
    }

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


    // Skip methods that manipulate currentPlayerIndex
    public void addSkip(int count) {
        this.skipCount += count;
    }


    public void skipAllPlayers() {
        // Skip all other players
        this.skipCount = players.size() - 1;
    }

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


    public Deck getPlayDeck() {
        return playDeck;
    }

    public static void main(String[] args) {
        UNO_Game game = new UNO_Game();

    }
}
