package graphs;

import java.util.Vector;

/**
 * A set of data for use with a DataGraph.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class GraphData
{
    /**
     * The max and min values of the x and y values of the points.
     */
    public double xMin, xMax, yMin, yMax;

    /**
     * The number of tics on the x and y axes.
     */
    public int numXTics, numYTics;

    /**
     * The maximum number of points to graph.
     */
    public int maxPoints;
        
    /**
     * The coordinates in the data set.
     */
    public Vector points;
    
    /**
     * Creates a new data set with the given parameters.
     *
     * @param x0 The minimum anticipated x value.
     * @param x1 The maximum anticipated x value.
     * @param y0 The minimum anticipated y value.
     * @param y1 The maximum anticipated y value.
     * @param xTics the number of tics on the x axis
     * @param yTics the number of tics on the y axis
     * @param mp The maximum number of points to graph
     */
    public GraphData(double x0, double x1, double y0, double y1,
		     int xTics, int yTics, int mp)
    {
	xMin = x0;
	xMax = x1;
	yMin = y0;
	yMax = y1;
	numXTics = xTics;
	numYTics = yTics;
	points = new Vector();
	maxPoints = mp;
    }
	    
    /**
     * Creates a new data set with the given parameters and 10 tics/axis.
     *
     * @param x0 The minimum anticipated x value.
     * @param x1 The maximum anticipated x value.
     * @param y0 The minimum anticipated y value.
     * @param y1 The maximum anticipated y value.
     * @param mp The maximum number of points to graph
     */
    public GraphData(double x0, double x1, double y0, double y1, int mp)
    {
	this(x0,x1,y0,y1,10,10,mp);
    }
    
    /**
     * Creates a new data set with default values.
     */
    public GraphData()
    {
	this(0.0,1.0,0.0,1.0,-1);
    }
	
    /**
     * Adds a coordinate to the data set.  It adjusts the min and max
     * values if the point added is lower or higher than the existing
     * minimums or maximums.
     * <p>
     * If using a maximum number of points, it assumes that the x's 
     * coming in are monotonically increasing.
     *
     * @param c The coordinate to add.
     */
    public void addCoordinate(Coordinate c)
    {
	points.add(c);
	if (c.x < xMin)
	    xMin = c.x;
	else if (c.x > xMax)
	    xMax = c.x;
	if (c.y < yMin)
	    yMin = c.y;
	else if (c.y > yMax)
	    yMax = c.y;
	
	if (maxPoints > 0)
	    while (points.size() > maxPoints) {
		points.remove(0);
		xMin = ((Coordinate) points.get(0)).x;
	    }
    }
}

