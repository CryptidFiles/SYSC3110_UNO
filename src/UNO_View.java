public interface UNO_View {
    void updateGameState();
    void displayPlayerHand(Player player);
    void highlightCurrentPlayer();
    void showCardPlayed(Card card);
    void showWildColorSelection();
    void displayMessage(String message);
    void updateScores();
    void showWinner(Player player);


}
