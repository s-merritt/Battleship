
/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player {
    //Lengths of all ships
    public static final int[] SHIP_LENGTHS = {2, 3, 3, 4, 5};

    //this value is the total number of hits a player can have
    //if this many are on the opponent's board, the game is over
    public static final int MAX_HITS = 17;

    //instance variables
    private Grid playerGrid;
    private Grid opponentGrid;
    private int numberOfHits;

    /**
     * This is the Player constructor. It will serve as the base for
     * the user and the computer players. Each player has 2 Grids, one
     * that shows all of their own ships, and one that shows their
     * guesses on their opponent's board. It will keep track of the
     * number of ships they have unplaced and placed
     */
    public Player() {
        this.playerGrid = new Grid();
        this.opponentGrid = new Grid();
        this.numberOfHits = 0;
    }

    public Grid getPlayerGrid()
    {
        return playerGrid;
    }

    public Grid getOpponentGrid()
    {
        return opponentGrid;
    }

    public void hitCounter()
    {
        numberOfHits++;
    }

    public int getNumberOfHits()
    {
        return numberOfHits;
    }

    /**
     * This method is called to allow the player to choose what Location
     * on their Grid to set one of their ships
     *
     * @param s is selected Ship to place
     * @param row is the row of the chosen Location
     * @param col is the column of the chosen Location
     * @param direction sets the direction, horizontal or vertical, of the Ship s
     *
     * @return true if ship was added, false otherwise
     */
    public boolean chooseShipLocation(Ship s, int row, int col, int direction) {
        if(playerGrid.hasShip(row, col))
            return false;
        s.setLocation(row, col);
        s.setDirection(direction);
        playerGrid.setShip(row, col, true);

        return playerGrid.addShip(s); //if true ship was added
    }
}
