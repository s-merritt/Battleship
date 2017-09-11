import java.util.LinkedList;

/**
 * This class will serve as the strategy for the computer to use against the player. It will randomly
 * guess until it HITS. It will save where the hit was made and base it's next guess around the last
 * hit until it has destroyed a ship. Once a ship has been destroyed it will resume randomly guessing.
 *
 * @author Scott Merritt
 * @version created January 26, 2017
 */
public class Strategy {
    //List for holding Locations to make guesses on
    private LinkedList<int[]> spots2Check;
    //variables for determining direction
    private int[] pivot;
    private int direction;
    private boolean firstHit;

    /**
     * Constructor for objects of class Strategy
     */
    public Strategy(){
        spots2Check = new LinkedList<int[]>();
        pivot = new int[2];
        direction = Ship.UNSET; //remains unset until first hit
        firstHit = true;
    }

    /**
     * This method lets the computer guess where the ships on the Player's board
     * are. It is completely random so it will not logically make attacks on
     * adjacent hit markers
     *
     * @param q the computer
     * @param p the user
     */
    public boolean computerGuess(Player q, Player p) {
        //variables
        int row, col;
        int[] spot;
        Grid guessGrid, shipGrid;

        System.out.println("Now the computer's turn to guess...");
        Battleship.pause(2);
        while (true) {
            row = Randomizer.nextInt(1, 10);
            col = Randomizer.nextInt(1, 10);
            guessGrid = q.getOpponentGrid();
            shipGrid = p.getPlayerGrid();
            spot = getSpot();

            //check list first before randomly guessing
            if (spot[0] != -1 || spot[1] != -1){
                row = spot[0];
                col = spot[1];
            }else
                firstHit = true;
            //if false then continues with a random guess that would be firstHit on a different ship

            if (!(guessGrid.alreadyGuessed(row, col))) { //works to ensure random guess or spot from list isn't guessed again
                System.out.println("Computer's guess: " + convert(row) + "" + col);
                Battleship.pause(1);
                if (shipGrid.hasShip(row, col)) {
                    System.out.println("HIT!");
                    lookAround(row, col); //get surrounding spots to check
                    guessGrid.markHit(row, col); //marks a hit on user's grid for opponent
                    //NEW STUFF
                    if(firstHit) {
                        setPivot(new int[]{row, col}); //add hit as an array to store it
                        firstHit = false; //this will remain false until spots2Check List is empty
                    }
                    else {
                        int pivotRow = pivot[0], pivotCol = pivot[1];
                        if(col == pivotCol)
                            direction = Ship.VERTICAL;
                        else if(row == pivotRow)
                            direction = Ship.HORIZONTAL;
                        else
                            direction = Ship.UNSET;
                        weedOut(direction);
                    }

                    //END NEW STUFF
                    q.hitCounter();
                    printGuessBoard(q);
                    if (Battleship.checkWin(p, q) == -1) //check win to ensure method stops if computer has already won
                        return false;
                    //continue loop so computer guesses again
                } else {
                    System.out.println("Miss...");
                    guessGrid.markMiss(row, col); //marks a miss on user's grid for opponent
                    printGuessBoard(q);
                    return false; //return false to end loop
                }
            }
        }
    }

    /**
     *  This method returns a spot from the list as an array [row, col]
     *
     *  @return spot int[] if head is not null, [-1, -1] otherwise
     *
     */
    private int[] getSpot() {
        int[] spot;
        if (spots2Check.peek() != null)
            spot = spots2Check.pop();
        else
            return new int[]{-1, -1};
        return spot;

    }

    /**
     * This method will get spots from the Grid around the initial hit
     * in order to add them to the List of spots to check. The only spots
     * needed are above, below, left, and right since ships cannot be diagonal
     *
     */
    private void lookAround(int row, int col){
        if(row + 1 <= 10)
            spots2Check.add(new int[]{row+1, col}); //spot below
        if(row - 1 >= 1)
            spots2Check.add(new int[]{row-1, col}); //spot above
        if(col + 1 <= 10)
            spots2Check.add(new int[]{row, col+1}); //spot right
        if(col - 1 >= 1)
            spots2Check.add(new int[]{row, col-1}); //spot left
    }

    /**
     *  This method is used to convert the number from randomizer into a character so it can be printed for the user
     * @param n is the int being converted to char
     * @return ASCII equivalent char of the inputted int
     */
    private char convert(int n){
        return ((char)(n + 64));
    }

    /**
     * This method will print the status of the opponent's board, essentially
     * where the computer has made all its guesses
     *
     * @param q the computer
     */
    private void printGuessBoard(Player q) {
        Battleship.pause(1);
        System.out.println("    COMPUTER'S GUESSES");
        System.out.println("   ====================");
        q.getOpponentGrid().printStatus();
        Battleship.pause(1);
    }

    /**
     * Setter method for pivot
     * @param spot new pivot
     */
    private void setPivot(int[] spot){this.pivot = spot;}

    /**
     * This method is used to throw out spots from the List depending on the direction given
     *
     * @param direction is the direction "learned" from the computerGuess method
     */
    private void weedOut(int direction){
        int size = spots2Check.size();
        for(int i = 0; i < size; i++) { //loop goes through every spot in the list so far
            int[] spot = spots2Check.pop();
            int row = spot[0], col = spot[1], pivotRow = pivot[0], pivotCol = pivot[1];
            if(direction == Ship.VERTICAL){
                if(col == pivotCol)
                    spots2Check.add(spot); //if spot is in the same column as pivot then re-add to the list, dump all else
            }
            else if(direction == Ship.HORIZONTAL){
                if(row == pivotRow)
                    spots2Check.add(spot); //if spot is in the same row as pivot then re-add to the list, dump all else
            }
            else
                return;
        }
    }
}
