import java.util.*;

public class UNO_Game {
    private ArrayList<Player> players;
    private Player currentPlayer;
    private Deck deck;
    private ArrayList<Card> drawPile;
    private Stack<Card> playPile;
    private Direction direction;
    private boolean gameOver;
    private Card topCard;
    private int numPlayers;
    private Scanner input = new Scanner(System.in);



    public UNO_Game(){
        deck = new Deck();
        drawPile = new ArrayList<>();
        playPile = new Stack<>();
        players = new ArrayList<>();
        gameOver = false;
        numPlayers();
        playerNames();
        distributeCards();
        playGame();
    }

    public void numPlayers(){
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

    public void playerNames(){
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter a name for player " + (i + 1) + ": ");
            players.add(new Player(input.nextLine()));
        }
    }

    public Card topCard(){
        return playPile.peek();
    }

    //Did something but needs work from here

    private void playGame() {
        while (!gameOver) {
            for (Player player : players) {
                System.out.println(player.getName() + "'s turn");

                System.out.println();

                System.out.println("Top Card: ");

                topCard().printCard();

                System.out.println();

                while (!validMove(player, player.takeTurn())) {
                    player.takeTurn();
                }
            }
        }
    }

    private boolean validMove(Player p, int i) {
        return p.playCard(i).playableOnTop(topCard);
    }

    private void distributeCards(){
        for (Player player : players){
            for (int i=0; i < 7; i++){
                Card c = deck.getDeck().removeLast();
                player.drawCard(c);
            }
        }
        Card c = deck.getDeck().removeLast();
        playPile.add(c);
        drawPile = deck.getDeck(); //The remaining cards are the draw pile now
    }

    public static void main(String[] args) {
        UNO_Game game = new UNO_Game();
    }
//    public boolean isValidPlay(Card card) {
//        return card.playableOnTop(topCard);
//    }
//
//    public void nextTurn() {
//    }
//
//    public void handleSpecialCard(Card card) {
//        switch(card.getValue()) {
//            case SKIP -> nextTurn(); // Skip next player
//
//            case REVERSE ->
//                    this.direction = (this.direction == Direction.CLOCKWISE)
//                    ? Direction.COUNTERCLOCKWISE
//                    : Direction.CLOCKWISE;
//
//            case DRAW_TWO -> {
//                // Next player draws 2 cards
//                //Player nextPlayer;
//                //nextPlayer.drawCardToHand(deck.drawCard());
//                //nextPlayer.drawCardToHand(deck.drawCard());
//                //nextTurn(); // Skip their turn
//            }
//            case WILD_DRAW_FOUR -> {
//                // Repeat Draw Two code but also change top card's color
//                // Ensure the color can be chosen by player
//            }
//        }
//    }
}
