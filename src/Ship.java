
/**
 * Write a description of class Ship here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Ship
{
    private int row;
    private int col;
    private int length;
    private int direction;

    //Direction constants
    public static final int UNSET = -1;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    /**
     * This is the Ship constructor, it will create a ship of a
     * given length with a default location of (-1, -1) --which
     * will not be on the grid-- and an unset
     * direction
     *
     * @param length = length of ship
     */
    public Ship(int length)
    {
        this.length = length;
        col = -1;
        row = -1;
        direction = -1;
    }

    public boolean isLocationSet()
    {
        return col != -1 && row != -1;
    }

    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setDirection(int direction)
    {
        this.direction = direction;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public int getLength()
    {
        return length;
    }

    public int getDirection()
    {
        return direction;
    }

    private String directionToString() {
        if(direction == 0)
            return "Horizontal";
        else if(direction == 1)
            return "Vertical";
        else
            return "Direction invalid";
    }

    public String toString()
    {
        return "Ship with length " + length;
    }
}
