import java.util.*;

public class UNO_Game {
    private ArrayList<Player> players;
    private Deck playDeck;
    private ArrayList<Card> drawPile;
    private Stack<Card> playPile;
    private Direction direction;
    private boolean gameOver;
    private Player winningPlayer;
    private int numPlayers;
    private Scanner input = new Scanner(System.in);

    public UNO_Game() {
        players = new ArrayList<>();
        playDeck = new Deck();
        drawPile = new ArrayList<>();
        playPile = new Stack<>();
        direction = Direction.CLOCKWISE;
        gameOver = false;

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
        drawPile = new ArrayList<>();
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

        while (true) {
            Card card = playDeck.drawCard();
            if (card == null) break;
            drawPile.add(card);
        }
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

    private void playGame() {
        gameOver = false;

        while (!gameOver) {
            for (Player player : players) {
                System.out.println("\n" + player.getName() + "'s turn");
                System.out.print("Top Card:\t");
                topCard().printCard();
                System.out.println();

                int chosenIndex;
                while (true) {
                    chosenIndex = player.takeTurn(input);
                    Card chosenCard = player.playCard(chosenIndex);

                    if (validMove(player, chosenIndex)) {
                        playPile.push(chosenCard);
                        System.out.print(player.getName() + " played:  ");
                        chosenCard.printCard();
                        chosenCard.action(this, player);

                        // simulate removing that card from hand
                        player.removeCard(chosenIndex);

                        // if player ran out of cards — they win the round
                        if (player.handSize() == 0) {
                            System.out.println("\n" + player.getName() + " wins ");
                            winningPlayer = player;
                            gameOver = true;
                            break;
                        }
                        break;
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                }

                if (gameOver) break;
            }
        }
    }

    private boolean validMove(Player p, int i) {
        return p.playCard(i).playableOnTop(topCard());
    }



    private void tallyScores(Player winner) {
        System.out.println("\n--- Scoreboard ---");

        // Tally winner's points from opponent's remaining cards
        for (Player p : players) {
            int handPoints = 0;

            if(p != winner) {
                for (Card c : p.getHand()) {
                    handPoints += c.getType().getPointValue();
                }
                winner.addScore(handPoints);
            }
            System.out.println(p.getName() + " total score: " + p.getScore());
        }

        // Display the current scores of all players
        for (Player p : players) {
            System.out.println(p.getName() + " total score: " + p.getScore());
        }
        System.out.println("-------------------\n");
    }

    public static void main(String[] args) {
        System.out.println("j");
        UNO_Game game = new UNO_Game();
    }
}
