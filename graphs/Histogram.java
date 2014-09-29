package graphs;

import dmc.Walker;

import java.lang.System;

import java.awt.*;
import javax.swing.*;

import java.util.Vector;
import java.util.Iterator;
import edu.uah.math.devices.Graph;
import edu.uah.math.distributions.Domain;

/**
 * This class extends the Graph class published by the University
 * of Alabama in Huntsville to graph histogram data.  All of the members
 * are public, so be careful how you manipulate them.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class Histogram extends Graph 
{ 
    /**
     * Constant to say graph the histogram as boxes.
     */
    public static final int BOXES = 0;

    /**
     * Constant to say graph the histogram as centered points.
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
     * The vector of walkers to act as data.
     */
    public Vector theData;

    /**
     * The number of bins of the histogram.
     */
    public int numBins;
    
    /**
     * The max and min values of the x axis.
     */
    public double xMin, xMax;
    
    /**
     * The list of functions to graph.
     */
    public Vector functionList;
    
    /**
     * Whether or not to normalize the histogram.
     */
    public boolean normalize;
    
    /**
     * The color of the graphed data.
     */
    public Color color = Color.red;

    /**
     * The mode to graph the histogram in, boxes or points.
     */
    public int mode = BOXES;

    /**
     * Constructs a histogram with the given parameters.
     *
     * @param width the width of the histogram
     * @param height the height of the histogram
     * @param xMin the min x value to graph
     * @param xMax The max x value to graph
     * @param numBins The number of bins of the histogram
     */
    public Histogram(int width, int height, double xMin, 
		     double xMax, int numBins)
    {
	super();
	this.width = width;
	this.height = height;
	this.xMin = xMin;
	this.xMax = xMax;
	this.numBins = numBins;
	setPreferredSize(new Dimension(width,height));
	setToolTipText("Histogram");
	theData = new Vector();
	functionList = new Vector();
    }
    
    /**
     * This function draws the histogram with the given graphics element.
     *
     * @param g the Graphics element with which to draw the histogram.
     */ 
    public void paintComponent(Graphics g)
    {
	// Fill bin data:
	double totalArea = 0.0;
	double binWidth = (xMax - xMin) / numBins;
	int bins[] = new int[numBins];
	for (int j = 0; j < numBins; j++) 
	    bins[j] = 0;
	int binMax = 0;
	Iterator i = theData.iterator();
	while (i.hasNext()) {
	    totalArea += binWidth;
	    int c = 0;
	    double val = ((Walker) i.next()).x;
	    while (val >= xMin) {
		c++;
		val -= binWidth;
	    }
	    try {
		bins[c-1]++;
		if (bins[c-1] > binMax)
		    binMax = bins[c-1];
	    } catch (ArrayIndexOutOfBoundsException aioobe) { }
	}
	
	double normalizationFactor = 1.0;
	if (normalize)
	    normalizationFactor = 1.0 / totalArea;
	
	// Clear drawing area:
	g.clearRect(0,0,width,height);
	double yScaleMax = binMax*normalizationFactor;
	if (yScaleMax < 1.0)
	    yScaleMax = 1.0;
	setScale(xMin,xMax,0,yScaleMax);

	/*
	 * If we're drawing boxes, draw them before the functions.
	 * If we're drawing points, draw them after the functions but 
	 * before the axes.
	 */

	// Draw Histogram (BOXES):
	g.setColor(color);
	if (mode == BOXES)
	    for (int j = 0; j < numBins; j++)
		fillBox(g, xMin+j*binWidth, 0.0, 
			xMin+(j+1)*binWidth, normalizationFactor*bins[j]);
		
	// Draw functions:
	i = functionList.iterator();
	while (i.hasNext()) {
	    Function F = (Function) i.next();
	    F.draw(g,this);
	}
	
	// Draw Histogram (POINTS):
	g.setColor(color);
	if (mode == POINTS)
	    for (int j = 0; j < numBins; j++)
		drawPoint(g,xMin+(j+0.5)*binWidth, normalizationFactor*bins[j]);

	g.setColor(Color.black);
	drawAxis(g,xMin,xMax,(xMax-xMin)/20,Domain.CONTINUOUS,0,Graph.HORIZONTAL);
	drawAxis(g,0,yScaleMax,yScaleMax/20,
		 Domain.DISCRETE,0,Graph.VERTICAL);
    }
}

