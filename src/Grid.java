/**
 * This class uses the Location class to form a standard board in the game of battleship.
 * It also defines a few methods for placing ships for both the user and the computer
 *
 * @author Scott Merritt
 * @version updated January 26, 2017
 */
public class Grid {
    //instance variable
    private Location[][] grid;

    //rows and columns constants
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;

    /**
     * This is the Grid constructor. It will create
     * a 2D grid using a 2D array of Locations
     * from the Location class
     *
     * IMPORTANT NOTE: each method that affects a Location
     * based on a given row and column will be subtracted
     * by one when used in the grid in order to correlate
     * the given numbers with the proper rows and columns
     * on the game board
     */
    public Grid() {
        grid = new Location[NUM_ROWS][NUM_COLS];
        for(int i = 0; i< Grid.NUM_ROWS; i++)
            for(int j = 0; j< Grid.NUM_COLS; j++)
                grid[i][j] = new Location(); //initialize each Location in grid
    }

    public void markHit(int row, int col){
        (grid[row - 1][col - 1]).markHit();
    }

    public void markMiss(int row, int col){
        (grid[row - 1][col - 1]).markMiss();
    }

    public void setStatus(int row, int col, int status){
        (grid[row - 1][col - 1]).setStatus(status);
    }

    public int getStatus(int row, int col){
        return (grid[row - 1][col - 1]).getStatus();
    }

    public boolean alreadyGuessed(int row, int col){
        return !(grid[row - 1][col - 1]).isUnguessed();
    }

    public void setShip(int row, int col, boolean val){
        (grid[row - 1][col - 1]).setShip(val);
    }

    public boolean hasShip(int row, int col){
        return (grid[row - 1][col - 1]).hasShip();
    }

    public Location get(int row, int col){
        return grid[row - 1][col - 1];
    }

    public int numRows(){
        return NUM_ROWS;
    }
    public int numCols(){
        return NUM_COLS;
    }
    /**
     * This method will print the status of each spot on the grid
     * using dashes for unguessed spots, X's for hits, and O's for
     * misses
     */
    public void printStatus() {
        System.out.print(" ");
        for(int k = 0; k < NUM_COLS; k++) //Prints top row of numbers to label each column
            System.out.print(" " + (k+1)); //k + 1 so it starts at 1 not 0
        System.out.println();

        for(int i = 0; i < NUM_ROWS; i++) {
            //first it will convert i to ascii equivalent to letters A-G
            char letter = (char)(i+65);
            System.out.print(letter + " "); //prints each row letter
            for(int j = 0; j<NUM_COLS; j++) {
                int stat = (grid[i][j]).getStatus();
                if(stat == Location.UNGUESSED)
                    System.out.print("- ");
                else if (stat == Location.HIT)
                    System.out.print("X ");
                else if (stat == Location.MISSED)
                    System.out.print("O ");
            }
            System.out.println();
        }
    }
    /**
     * This method will print the location of the player's ships,
     * marking the occupied spots with an 'X'
     */
    public void printShips() {
        System.out.print(" ");
        for(int k = 0; k < NUM_COLS; k++) //Prints top row of numbers to label each column
            System.out.print(" " + (k+1)); //k + 1 so it starts at 1 not 0
        System.out.println();

        for(int i = 0; i < NUM_ROWS; i++) {
            //first it will convert i to ascii equivalent to letters A-G
            char letter = (char)(i+65);
            System.out.print(letter + " "); //prints each row letter
            for(int j = 0; j<NUM_COLS; j++) {
                boolean ship = (grid[i][j]).hasShip();
                if(ship)
                    System.out.print("X ");
                else
                    System.out.print("- ");

            }
            System.out.println();
        }
    }
    /**
     * This method can be called on the player's own grid. To
     * add a ship we will go to the ship's location and mark
     * a true value in every location that the ship takes up.
     *
     * It will check to see if the ship has a location and
     * direction and has enough space to be set
     *
     * @param s is the ship being added
     *
     * @return valid is true if ship was properly added, false otherwise
     */
    public boolean addShip(Ship s) {
        boolean valid = false;
        if(s.isLocationSet() && s.getDirection() != Ship.UNSET) {
            int i = s.getRow(), j = s.getCol();
            if(s.getDirection() == Ship.VERTICAL) {
                if (isValidLocationV(s, i, j)) {
                    for (i = s.getRow(); i < s.getRow() + s.getLength(); i++)
                        setShip(i, s.getCol(), true); //changes by row
                    valid = true;
                }
            }
            else if(s.getDirection() == Ship.HORIZONTAL) {
                if (isValidLocationH(s, i, j)) {
                    for (j = s.getCol(); j < s.getCol() + s.getLength(); j++)
                        setShip(s.getRow(), j, true); //changes by column
                    valid = true;
                }
            }
        }
        return valid;
    }
    /**
     * This method will check the validity of the selected Location for a Ship
     * if the Ship's direction is VERTICAL. It will check if the length is within
     * the borders of the Grid and if there is already a Ship occupying on of the
     * spaces
     *
     * @param s is the ship being set
     * @param row is the desired row
     * @param col is the desired column
     */
    public boolean isValidLocationV(Ship s, int row, int col) {
        if(row + s.getLength() <= 11)
            for(int k = row + 1; k < s.getRow() + s.getLength(); k++)
                if(hasShip(k, col)) {
                    setShip(row, col, false);
                    return false; //if there is already a ship, location is invalid
                }
                else
                    continue;
        else {
            setShip(row, col, false);
            return false;
        }
        return true;
    }
    /**
     * This method will check the validity of the selected Location for a Ship
     * if the Ship's direction is HORIZONTAL. It will check if the length is within
     * the borders of the Grid and if there is already a Ship occupying any of the
     * spaces
     *
     * @param s is the ship being set
     * @param row is the desired row
     * @param col is the desired column
     */
    public boolean isValidLocationH(Ship s, int row, int col) {
        if(col + s.getLength() <= 11) {
            for (int k = col + 1; k < s.getCol() + s.getLength(); k++) {
                if (hasShip(row, k)) {
                    setShip(row, col, false);
                    return false; //if there is already a ship, location is invalid
                }
            }
        }
        else {
            setShip(row, col, false);
            return false;
        }
        return true;
    }
}
