package graphs;

import edu.uah.math.devices.Graph;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class implements the ability to graph a function on an existing
 * graph.  It is meant to be used as a superclass, with the f(x) function
 * overwritten.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class Function
{
    /**
     * The color to graph the function.
     */
    public Color color;
    
    /**
     * Creates a function to be graphed in blue.
     */
    public Function() 
    { 
	color = Color.blue;
    }
    
    /**
     * Creates a function to be graphed in the given color.
     *
     * @param c The color to graph the function in.
     */
    public Function(Color c) 
    { 
	color = c;
    }
    
    /**
     * The function to graph.
     *
     * @param x The independent variable of the function.
     * @returns the value of the function at the position x
     */
    public double f(double x)
    {
	return x;
    }

    /**
     * This function draws the function on the given graph.
     *
     * @param g The graphics element being graphed.
     * @graph The Graph on which to draw the function.
     */
    public void draw(Graphics g, Graph graph)
    {
	double xMax = graph.getXMax();
	double xMin = graph.getXMin();
	g.setColor(color);
	double dx = (xMax - xMin) / 
	    (graph.getXGraph(xMax) - graph.getXGraph(xMin));
	double pX = xMin;
	double pY = f(pX);
	double x, y;
	x = pX + dx; y = f(x);
	while (x <= xMax) {
	    graph.drawLine(g,pX,pY,x,y);
	    pX = x; pY = y;
	    x = pX + dx; y = f(x);
	}
    }
}
