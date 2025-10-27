import java.util.*;

public class UNO_Game {
    private ArrayList<Player> players;



    private Deck playDeck;
    private Stack<Card> playPile;
    private Direction direction;
    private boolean gameOver;
    private Player winningPlayer;
    private int numPlayers;
    private Scanner input = new Scanner(System.in);
    private int currentPlayerIndex;
    private int skipCount;

    public UNO_Game() {
        players = new ArrayList<>();
        playDeck = new Deck();
        //drawPile = new ArrayList<>();
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
            tallyScores(winningPlayer);

            System.out.print("Do you want to play again? (y/n): ");
            String choice = input.nextLine().trim().toLowerCase(); //Defensive programming right here
            playAgain = choice.equals("y");
        }

        System.out.println("\nThanks for playing UNO Flip!");
    }

    private void resetDecks() {
        playDeck = new Deck();  // fresh shuffled decks
        //drawPile = new ArrayList<>();
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

        // Set up first card
        Card firstCard = playDeck.drawCard();
        if (firstCard != null) {
            playPile.push(firstCard);
        }

        /**while (true) {
            Card card = playDeck.drawCard();
            if (card == null) break;
            drawPile.add(card);
        }*/
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
            // Check if current player(s) should be skipped by applying the number of skips to player index
            processSkip();

            //displays whose turn it is and the current card on top of play Pile so player knows what color
            Player player = players.get(currentPlayerIndex);

            System.out.println("\n" + player.getName() + "'s turn");
            System.out.print("Top Card:\t");
            topCard().printCard();
            System.out.println();

            int chosenIndex;
            while (true) {
                // If the player cannot play with their hand, draw a card and end turn
                if(!playableHand(player)){
                    System.out.println("You do not have a playable hand! " + player.getName() + " draws a card.");

                    Card card = playDeck.drawCard();

                    if (card == null) break;

                    card.printCard();
                    player.drawCard(card);

                    // End the turn of the player
                    break;
                }


                chosenIndex = player.takeTurn(input); //asks current player which card to play acc to index
                Card chosenCard = player.playCard(chosenIndex); //gets the card from hand but not remove it yet

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
                        winningPlayer = player;
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
                }
                winner.addScore(handPoints);
            }
            System.out.println(p.getName() + " total score: " + p.getScore()); //prints the updated scores
        }

        // Display the current scores of all players, isn't that repetitive code? this loop is unnecessary
        for (Player p : players) {
            System.out.println(p.getName() + " total score: " + p.getScore());
        }
        System.out.println("-------------------\n");
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
        // Skip all other players (players.size() - 1 skips)
        this.skipCount = players.size() - 1;
    }

    public void processSkip() {
        if (skipCount > 0) {
            skipCount--;
            // Skip the current player by advancing the index
            if (direction == Direction.CLOCKWISE) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            } else {
                currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
            }
        }
    }


    public Deck getPlayDeck() {
        return playDeck;
    }

    public static void main(String[] args) {
        UNO_Game game = new UNO_Game();

    }
}
