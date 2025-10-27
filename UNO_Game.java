import java.util.*;

public class UNO_Game {
    private ArrayList<Player> players; //array of all players in the game
    private Deck playDeck; //full deck of cards
    private ArrayList<Card> drawPile; //cards that can still be drawn
    private Stack<Card> playPile; //pile of cards that have been played
    private Direction direction; //current play dirction
    private boolean gameOver; //if game is over or still going
    private Player winningPlayer; //who won
    private int numPlayers; //how many players in the round
    private Scanner input = new Scanner(System.in); //for user input

    public UNO_Game() {
        players = new ArrayList<>();
        playDeck = new Deck();
        drawPile = new ArrayList<>();
        playPile = new Stack<>();
        direction = Direction.CLOCKWISE;
        gameOver = false;

        numPlayers(); //ask user how many players
        playerNames(); //set player names
        gameLoop(); // to start the game
    }

    private void gameLoop() {
        boolean playAgain = true;

        while (playAgain) {
            resetDecks(); //fresh shuffled deck and piles
            distributeCards(); //give 7 cards to each player
            playGame(); //one round of game

            System.out.println("\n--- Round Over ---");
            tallyScores(winningPlayer); //update scores

            System.out.print("Do you want to play again? (y/n): ");
            String choice = input.nextLine().trim().toLowerCase(); //Defensive programming right here
            playAgain = choice.equals("y"); //if not equal, playAgain is false and game ends
        }

        System.out.println("\nThanks for playing UNO Flip!");
    }

    private void resetDecks() {
        playDeck = new Deck();  // fresh shuffled decks
        drawPile = new ArrayList<>();
        playPile = new Stack<>();
    }

    private void distributeCards() {
        //to give each player 7 cards from the deck
        for (Player player : players) {
            player.clearHand();
            for (int i = 0; i < 7; i++) {
                Card c = playDeck.drawCard(); //new card drawn from deck
                player.drawCard(c); //giving current player that card
            }
        }

        // Set up first card on play pile
        Card firstCard = playDeck.drawCard();
        if (firstCard != null) {
            playPile.push(firstCard);
        }
        //move remaining cards in the deck to the draw pile
        while (true) {
            Card card = playDeck.drawCard();
            if (card == null) break; //no more cards in the deck
            drawPile.add(card);
        }
    }

    public void numPlayers() {
        numPlayers = 0;
        while (true) {
            System.out.print("Enter the number of players (2-4): ");
            if (input.hasNextInt()) { //input is an integer notr a string
                numPlayers = input.nextInt();
                input.nextLine(); //so future scanner read work correctly
                if (numPlayers >= 2 && numPlayers <= 4) {
                    System.out.println("You entered: " + numPlayers);
                    break; //the only way to stop the infinite loop; when input is valid
                } else {
                    System.out.println("Number of players must be between 2 and 4.");
                }
            } else {
                System.out.println("Please enter an integer");
                input.next(); //discard the baf input so loop can start fresh again
            }

        }
    }

    public void playerNames() {
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter a name for player " + (i + 1) + ": ");
            players.add(new Player(input.nextLine())); //the .nextline() wait for the user to type something and enter
        }
    }

    public Card topCard() {
        return playPile.peek();
    }

    //where all actions happen, player turns, playing cards and detecting if someone won
    private void playGame() {
        int currentIndex = 0; //start from player 0
        gameOver = false;

        //keeps looping over all players until one runs out of cards and win
        while (!gameOver) {
            //displays whose turn it is and the current card on top of play Pile so player knows what color
            Player player = players.get(currentIndex);
            System.out.println("\n" + player.getName() + "'s turn");
            System.out.print("Top Card:\t");
            topCard().printCard();
            System.out.println();

//            for (Player player : players) {
//                System.out.println("\n" + player.getName() + "'s turn");
//                System.out.print("Top Card:\t");
//                topCard().printCard();
//                System.out.println();

                int chosenIndex;
                while (true) {
                    chosenIndex = player.takeTurn(input); //asks current player which card to play acc to index
                    Card chosenCard = player.playCard(chosenIndex); //gets the card from hand but not remove it yet

                    if (validMove(player, chosenIndex)) { //checks if card valid (matches color type on top of play pile
                        playPile.push(chosenCard); //pushes that card to the play pile
                        System.out.print(player.getName() + " played:  "); //displays that the player played and finished his turn
                        chosenCard.printCard(); //prints the card played
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
                        System.out.println("Invalid move. Try again."); //if chose invalid card, prompts user to try again
                    }
                }

                if (gameOver) break; //if game over dont have to go to next player, just breaks

                if (direction == Direction.CLOCKWISE) {
                    currentIndex = (currentIndex + 1) % players.size();
                } else {
                    currentIndex = (currentIndex - 1 + players.size()) % players.size();
                }
            //}
        }
    }

    private boolean validMove(Player p, int i) { //returns true if players card matches type or color of the top card in play Pile
        return p.playCard(i).playableOnTop(topCard());
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


    public static void main(String[] args) {
        System.out.println("j");
        UNO_Game game = new UNO_Game();
    }
}
