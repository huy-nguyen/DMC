package graphs;

import dmc.Walker;
import java.util.Vector;
import java.util.Iterator;

/**
 * This class keeps track of the data necessary to graph an estimate
 * of Phi_0 in the Phi0Histogram class.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class Phi0EstimateData
{
    /**
     * An array containing a count of how many walkers are in each bin.
     */
    public int[] bins;

    /**
     * The number of bins to separate the data into.
     */
    public int numBins;
    
    /**
     * The max and min values of the x axis.
     */
    public double xMin, xMax;

    /**
     * The width of each bin.
     */
    public double binWidth;

    /**
     * Creates a new dataset with the values given.
     *
     * @param xMin the minimum x value
     * @param xMax the maximum x value
     * @param numBins the number of bins to separate the data into
     */
    public Phi0EstimateData(double xMin, double xMax, int numBins)
    {
	this.xMin = xMin;
	this.xMax = xMax;
	this.numBins = numBins;
	bins = new int[numBins];
	binWidth = (xMax - xMin) / numBins;
	for (int i = 0; i < numBins; i++)
	    bins[i] = 0;
    }
    
    /**
     * Adds a vector of data to the current dataset.  If it's not in
     * the bounds, which is possible, do nothing.
     *
     * @param v The vector of data to add.
     */
    public void addData(Vector v)
    {
	Iterator i = v.iterator();
	while (i.hasNext()) {
	    double x = ((Walker) i.next()).x;
	    int index = (int) ((x-xMin)/binWidth);
	    try {
		bins[index]++;
	    }
	    catch (ArrayIndexOutOfBoundsException aioobe) { }
	}
    }
}

