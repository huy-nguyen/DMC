package graphs;

import java.lang.System;

import java.awt.*;
import javax.swing.*;

import java.util.Vector;
import java.util.Iterator;
import edu.uah.math.devices.Graph;
import edu.uah.math.distributions.Domain;
import java.text.DecimalFormat;

/**
 * This class extends the Graph class published by the University
 * of Alabama in Huntsville to graph scatterplot data.  All of the members
 * are public, so be careful how you manipulate them.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class DataGraph extends Graph 
{ 
    /**
     * Constant to say graph the data as lines.
     */
    public static final int LINES = 0;

    /**
     * Constant to say graph the data as points.
     */
    public static final int POINTS = 1;

    /**
     * The width in pixels.
     */
    public int width;

    /**
     * The height in pixels.
     */
    public int height;

    /**
     * The max and min values of the x and y axes.
     */
    public double xMin, xMax, yMin, yMax;
    
    /**
     * The list of functions to graph.
     */
    public Vector functionList;
    
    /**
     * The data to graph.
     */
    public GraphData theData;

    /**
     * The color of the graphed data.
     */
    public Color color;
    
    public int mode = LINES;

    /**
     * Creates a new graph.
     *
     * @param width the width of the graph
     * @param height the height of the graph
     */
    public DataGraph(int width, int height)
    {
	super();
	this.width = width;
	this.height = height;
	setPreferredSize(new Dimension(width,height));
	setToolTipText("Data Graph");
	theData = new GraphData();
	color = Color.red;
	functionList = new Vector();
    }
     
    /**
     * This function draws the graph with the given graphics element.
     *
     * @param g the Graphics element with which to draw the histogram.
     */ 
    public void paintComponent(Graphics g)
    {
	xMin = theData.xMin;
	xMax = theData.xMax;
	yMin = theData.yMin;
	yMax = theData.yMax;
	
	// Clear drawing area:
	g.clearRect(0,0,width,height);
	setScale(xMin,xMax,yMin,yMax);

		
	// Draw functions:
	Iterator i = functionList.iterator();
	while (i.hasNext()) {
	    Function F = (Function) i.next();
	    F.draw(g,this);
	}

	// Draw data:
	g.setColor(color);
	i = theData.points.iterator();
	double pX = 0.0;
	double pY = 0.0;
	boolean firstPoint = true;
	while (i.hasNext()) {
	    Coordinate c = (Coordinate) i.next();
	    
	    if (mode == LINES) {
		if (!firstPoint) 
		    drawLine(g,pX,pY,c.x,c.y);
		else
		    firstPoint = false;
		pX = c.x;
		pY = c.y;
	    }
	    else if (mode == POINTS) {
		drawPoint(g,c.x,c.y);
	    }
	}

	g.setColor(Color.black);
	double xAxisLoc;
	if (yMin <= 0 && yMax > 0) {
	    setMargins(20,20,20,20);
	    xAxisLoc = 0;
	}
	else {
	    setMargins(20,20,30,10);
	    double yPerPixel = (yMax-yMin)/ ((double) (getYGraph(yMin) - getYGraph(yMax)));
	    xAxisLoc = yMin-yPerPixel*11;
	}
	drawAxis(g,xMin,xMax,(xMax-xMin)/theData.numXTics,
		 Domain.CONTINUOUS,xAxisLoc,Graph.HORIZONTAL);
	drawAxis(g,yMin,yMax,(yMax-yMin)/theData.numYTics,
		 Domain.CONTINUOUS,xMin,Graph.VERTICAL);
    }
}

