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
 * of Alabama in Huntsville to graph an estimate of the ground
 * state wavefunction.  All of the members are public, so be careful 
 * how you manipulate them.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class Phi0Histogram extends Graph 
{ 
    /**
     * The width in pixels.
     */
    public int width;

    /**
     * The height in pixels.
     */
    public int height;
    
    
    /**
     * The data for the graph.
     */
    public Phi0EstimateData theData;
    
    /**
     * The list of functions to graph.
     */
    public Vector functionList;
       
    /**
     * The color of the graphed data.
     */
    public Color color = Color.red;

    /**
     * Creates an estimate histogram of phi_0 from the given parameters.
     *
     * @param width The width in pixels
     * @param height the height in pixels
     * @param data The data for the graph
     */
    public Phi0Histogram(int width, int height, Phi0EstimateData data)
    {
	super();
	this.width = width;
	this.height = height;
	theData = data;
	setPreferredSize(new Dimension(width,height));
	setToolTipText("Phi_0 Estimate Graph");
	functionList = new Vector();
    }
    
    /**
     * This function draws the histogram with the given graphics element.
     * The normalization algorithm is taken from the paper cited
     * in DMC.java.
     *
     * @param g the Graphics element with which to draw the histogram.
     */ 
    public void paintComponent(Graphics g)
    {
	double normalizationFactor = 1.0;
	double tmcount = 0.0;
	for (int j = 0; j < theData.numBins; j++) {
	    tmcount += theData.bins[j]*theData.bins[j];
		
	}
	
	
	normalizationFactor = 1.0 / Math.sqrt(tmcount*theData.binWidth);
	//System.out.println("tmcount = " + tmcount + " and nnormalizationFactor = " + normalizationFactor);

	// Clear drawing area:
	g.clearRect(0,0,width,height);
	double yScaleMax = 1.0;
	setScale(theData.xMin,theData.xMax,0,yScaleMax);
		
	// Draw functions:
	Iterator i = functionList.iterator();
	while (i.hasNext()) {
	    Function F = (Function) i.next();
	    F.draw(g,this);
	}
	
	// Draw Histogram:
	g.setColor(color);
	for (int j = 0; j < theData.numBins; j++)
	    drawPoint(g,theData.xMin+(j+0.5)*theData.binWidth, 
		      normalizationFactor*theData.bins[j]);

	g.setColor(Color.black);
	drawAxis(g,theData.xMin,theData.xMax,(theData.xMax-theData.xMin)/20,
		 Domain.CONTINUOUS,0,Graph.HORIZONTAL);
	drawAxis(g,0,yScaleMax,yScaleMax/20,
		 Domain.DISCRETE,0,Graph.VERTICAL);
    }
}

