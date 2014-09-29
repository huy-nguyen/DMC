package graphs;

/**
 * This class implements a simple two dimensional coordinate, with an
 * x position and a y position.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class Coordinate
{
    /**
     * The x position.
     */ 
    public double x;

    /**
     * The y position.
     */
    public double y;
    
    /**
     * Creates a coordinate with the given positions.
     *
     * @param x The x position.
     * @param y The y position.
     */
    public Coordinate(double x, double y)
    {
	this.x = x;
	this.y = y;
    }
}

