import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class is the main class for the game of Battleship. It combines the classes Player, Grid,
 * and Randomizer in order construct the actual game
 *
 * It has two players: the user and a computer. The user may input their ships
 * however they please (as long as the placements are acceptable in terms of
 * being within the grid and not overlapping), and the computer will set their
 * ships randomly with the same rules applied.
 *
 * The players will then go back and forth making guesses as to where their
 * opponent's ships may be. The user inputs their own desired locations to
 * guess while the computer makes random guesses each time.
 *
 * The game ends when either player guesses all of the locations of their
 * opponent's ships ("HITS")
 *
 * @author Scott Merritt
 * @version updated January 26, 2017
 */
public class Battleship {
    private Scanner keyboard = new Scanner(System.in);
    /**
     * This is the run method. It is the game itself that will interact with the user.
     *
     */
    public void run() {
        //creates two players
        Player user = new Player();
        Player computer = new Player();
        Strategy compStrat = new Strategy();

        System.out.println("Welcome to Battleship! \n");
        pause(2);

        userSetShips(user);
        pause(2);

        System.out.println("It's now the computer's turn to set its ships, this will only take a second... \n");
        pause(2);

        computerSetShips(computer);
        //printCompShips(computer); //for testing purposes
        System.out.println("Alright, now the game begins \n");
        pause(1);


        //this is the game itself, it will continue until either Player
        //has gotten the maximum number of hits specified in the Player class
        while(checkWin(user, computer) == 0) {
            while(askForGuess(user, computer)){}
            if(checkWin(user, computer) == 1)
                break;
            keyboard.nextLine(); //swallows last \n
            while(compStrat.computerGuess(computer, user)){}
            if(checkWin(user, computer) == -1)
                break;
        }
        if(checkWin(user, computer) == 1) {
            System.out.println("USER has sunken all of his opponent's ships!");
            pause(2);
        }
        else if(checkWin(user, computer) == -1) {
            System.out.println("COMPUTER has sunken all of his opponent's ships!");
            pause(2);
        }
        System.out.println("GAME OVER \n");
        pause(2);
        System.out.println("Thanks for playing!");
    }

    /**
     * This method prints the Ships on the player's board
     *
     * @param p the user
     */
    public void printUserBoard(Player p) {
        System.out.println("      YOUR SHIPS");
        System.out.println("   ================");
        p.getPlayerGrid().printShips();
        pause(1);

    }

    /**
     * This method will print the status of the opponent's board, essentially
     * where the user has made all their guesses
     *
     * @param p the computer
     */
    public void printGuessBoard(Player p) {
        System.out.println("     YOUR GUESSES");
        System.out.println("   ===============");
        p.getOpponentGrid().printStatus();
    }

    /**
     * This method is purely meant for testing to ensure that the computer has properly
     * placed all of its ships
     *
     * @param p the user
     */
    private void printCompShips(Player p){
        System.out.println("    COMPUTER SHIPS");
        System.out.println("   ===============");
        p.getPlayerGrid().printShips();
    }

    /**
     * This method will ask the user for a row and a column as a guess for
     * where the opponent's ship may be. It will notify the player if the
     * guess is a hit or miss or has already been guessed
     *
     * @param p the user
     * @param q the computer
     */
    private boolean askForGuess(Player p, Player q) {
        //variables
        String rowLetter = "";
        int row = 0, col = 0;
        Grid guessGrid = p.getOpponentGrid(), shipGrid = q.getPlayerGrid();

        System.out.println("Your turn to guess\n");
        pause(1);

        boolean first = true;
        while(true){
            if(first)
                first = false;
            else
                keyboard.nextLine();
            System.out.println("What row?");
            rowLetter = keyboard.nextLine().toUpperCase();
            if(rowLetter.length() != 0) {
                if(rowLetter.equals("QUIT"))
                    quit(); //exits game
                else {
                    row = convert(rowLetter.charAt(0)); //converts row letter to integer for Grid
                    if (row >= 1 && row <= 10)
                        break;
                    else {
                        System.out.println("Row letter out of bounds, try again [PRESS ENTER]");
                    }
                }
            }
        }
        while(true) {
            System.out.println("What column?");
            try {
                col = keyboard.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("INVALID INPUT PLEASE ENTER AN INTEGER");
                keyboard.next();
                pause(1);
                continue;
            }
            if (col >= 1 && col <= 10)
                break;
            else
                System.out.println("Column number out of bounds, try again");
        }
        pause(1);
        //check opponent's grid for ship at that location
        if(!(guessGrid.alreadyGuessed(row, col))) {
            if(shipGrid.hasShip(row, col)) {
                System.out.println("HIT!");
                pause(1);
                guessGrid.markHit(row, col); //marks a hit on user's grid for opponent
                p.hitCounter();
                printGuessBoard(p); //prints grid of guesses for user
                keyboard.nextLine(); //swallows last \n
                return checkWin(p, q) != 1; //if player wins then it stops asking for guesses
            }
            else {
                System.out.println("Miss...");
                pause(1);
                guessGrid.markMiss(row, col); //marks a miss on user's grid for opponent
                printGuessBoard(p); //prints grid of guesses for user
                return false;
            }
        }
        else {
            System.out.println("That location has already been guessed, please try again.");
            keyboard.nextLine(); //swallows last \n
            return true;
        }
    }

    /**
     * This method asks the user to set all of their ships onto their board.
     * It will start in order of ship length based on the array in the Player class.
     * It will ask for a valid row letter (if one is not given, it will repeat
     * the question). It will then ask for a valid column number (and ask again
     * if a invalid one is given). It will finally ask for the direction of the
     * ship, repeating the question if words other than 'vertical' or 'horizontal'
     * (ignoring case) are given. It will check if the ship could be set, it will
     * tell the user, otherwise it will tell the user that the ship could not be
     * set and reset the process for setting that ship (not all ships)
     *
     * @param p the user
     */
    private void userSetShips(Player p) {
        //variables
        String rowString = "", direction = "";
        int rowInt = 0, col = 0, dir = 0;

        System.out.println("Set up your ships to fit appropriately on a 10 x 10 grid.");
        System.out.println("You have 5 ships of length 2, 3, 3, 4, and 5. \n");

        pause(1);

        System.out.println("NOTE: the way the games sets the ships is as follows:");
        pause(1);
        System.out.println("-Vertical ships will start from the coordinate given and go DOWN from there to the specific length of the ship");
        pause(1);
        System.out.println("-Horizontal ships will start from the coordinate given and go RIGHT from there to the specific length of the ship \n");
        pause(2);

        for(int i = 0; i < Player.SHIP_LENGTHS.length; i++) {
            while(true) {
                Ship s = new Ship(Player.SHIP_LENGTHS[i]);
                System.out.println("Setting ship of length " + s.getLength());
                while(true) {
                    System.out.println("Enter row letter (A-J): ");
                    rowString = keyboard.nextLine().toUpperCase();
                    if(rowString.length() != 0) {
                        if(rowString.equals("QUIT"))
                            quit(); //exits game
                        else {
                            rowInt = (int) rowString.charAt(0) - 64;
                            if (rowInt >= 0 && rowInt <= 10)
                                break;
                            else
                                System.out.println("Row letter invalid, please try again");
                        }
                    }
                }
                while(true) {
                    System.out.println("Enter the column number (1- 10): ");
                    try{
                        col = keyboard.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("INVALID INPUT PLEASE ENTER AN INTEGER");
                        keyboard.next();
                        pause(1);
                        continue;
                    }
                    if(col >= 1 && col <= 10)
                        break;
                    else
                        System.out.println("Column number invalid, please try again");
                }
                boolean first = true; //this variable is meant to mark the first iteration of the following loop
                while(true) {
                    System.out.println("What direction (enter \"V\" for vertical or \"H\" for horizontal)");
                    if(first){
                        keyboard.nextLine(); //eats up \n when user hits enter after previous query
                        first = false;
                    }
                    direction = keyboard.nextLine();
                    if(direction.equalsIgnoreCase("v")) {
                        dir = Ship.VERTICAL;
                        break;
                    }
                    else if(direction.equalsIgnoreCase("h")) {
                        dir = Ship.HORIZONTAL;
                        break;
                    }
                    else {
                        System.out.println("Direction invalid, please try again");
                        pause(1);
                    }
                }
                if(p.chooseShipLocation(s, rowInt, col, dir)){
                    if(p.getPlayerGrid().hasShip(rowInt, col)) {
                        System.out.println("Ship set!");
                        pause(1);
                        printUserBoard(p);
                        System.out.println();
                        break;
                    }
                }
                else {
                    System.out.println("Ship location invalid, try again.");
                    pause(1);
                }
            }
        }
        System.out.println("All ships set!\n");
    }
    /**
     * This method will set the Ships of the computer player, going in ascending
     * order of the SHIP_LENGTHS array in the Player class. It will produce the
     * Location of each Ship randomly using the Randomizer class, checking to make
     * sure that each ship is set in a valid Location and is fully placed
     *
     * @param p the computer player
     */
    private void computerSetShips(Player p) {
        for(int i = 0; i < Player.SHIP_LENGTHS.length; i++) {
            while(true) {
                //Ship created from SHIP_LENGTHS array
                Ship s = new Ship(Player.SHIP_LENGTHS[i]);
                //variables for random ship placement
                int row = Randomizer.nextInt(1, 9), col = Randomizer.nextInt(1, 9), direction = Randomizer.nextInt(0, 1);

                if(p.chooseShipLocation(s, row, col, direction)){ //chooses location based on random row and column
                    if(p.getPlayerGrid().hasShip(row, col)) //checks to ensure that the ship was placed, if true it moves on to the next Ship length
                        break;
                }
            }
        }
        System.out.println("Computer's ships have been set! \n");
        pause(2);
    }

    /**
     * This method is unnecessary but takes care of a lot of repetition in the code.
     *
     * It pauses the main Thread for a given amount of seconds.
     *
     * @param seconds the amount of seconds it is paused
     */
    public static void pause(int seconds){
        try {
            Thread.sleep((long)1000 * seconds);
        }catch(InterruptedException e){System.exit(0);}
    }


    /**
     * This method will check to see if either player has won by checking each of
     * their number of hits
     *
     * @param p  the user
     * @param q  the computer
     * @return 1 if p has reached the max number of hits, -1 if q has reached the max number of hits, 0 otherwise
     */
    public static int checkWin(Player p, Player q){
        if(p.getNumberOfHits() == Player.MAX_HITS)
            return 1;
        else if(q.getNumberOfHits() == Player.MAX_HITS)
            return -1;
        else
            return 0;
    }

    /**
     * This method is called when the user answers a prompt with "quit". It exits
     * the game and stops running the program
     */
    public void quit(){
        System.exit(0);
    }

    /**
     *  Converts a char into an int
     * @param c is the character being converted
     * @return ASCII equivalent int
     */
    private int convert(char c){
        return ((int)(c)) - 64;
    }

}
