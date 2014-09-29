package dmc;

import java.util.HashSet;
import java.util.Set;
import java.awt.Color;

/**
 * A one dimensional walker.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class Walker implements java.io.Serializable
{
    /**
     * The position of the walker.
     */
    public double x;
    
    public double drift;
    
    public double weight = 1;
    
    public static int count = 0;
    
    static int colorCount = 0;
    
    public Color walkerColor;
    
    public static Set<Color> colors = new HashSet<Color>(); 
    
    public int id;
    
    Color assignColor() {
    	if(! colors.contains(Color.red)) {
    		colors.add(Color.red);
    		colorCount = 0;
    		return Color.red;
    	}
    	else if (! colors.contains(Color.blue)) {
    		colors.add(Color.blue);
    		colorCount = 0;
    		return Color.blue;
    	}
    	else if (! colors.contains(Color.green)) {
    		colors.add(Color.green);
    		colorCount = 0;
    		return Color.green;
    	}
    	else if (! colors.contains(Color.cyan)) {
    		colors.add(Color.cyan);
    		colorCount = 0;
    		return Color.cyan;
    	}
    	else if (! colors.contains(Color.magenta)) {
    		colors.add(Color.magenta);
    		colorCount = 0;
    		return Color.magenta;
    	}
    	else if (! colors.contains(Color.yellow)) {
    		colors.add(Color.yellow);
    		colorCount = 0;
    		return Color.yellow;
    	}
    	else if (! colors.contains(Color.pink)) {
    		colors.add(Color.pink);
    		colorCount = 0;
    		return Color.pink;
    	}
    	else if (! colors.contains(Color.orange)) {
    		colors.add(Color.orange);
    		colorCount = 0;
    		return Color.orange;
    	}
    	else if (! colors.contains(Color.lightGray)) {
    		colors.add(Color.lightGray);
    		colorCount = 0;
    		return Color.lightGray;
    	}
    	else if (! colors.contains(Color.darkGray)) {
    		colors.add(Color.darkGray);
    		colorCount = 0;
    		return Color.darkGray;
    	}
    	else if (! colors.contains(Color.black)) {
    		colors.add(Color.black);
    		colorCount = 0;
    		return Color.black;
    	}
    	else {
    		colorCount++;
    		switch( colorCount % 11) {
    		case 0: return Color.red;
    		case 1: return Color.blue;
    		case 2: return Color.green;
    		case 3: return Color.cyan;
    		case 4: return Color.magenta;
    		case 5: return Color.yellow;
    		case 6: return Color.pink;
    		case 7: return Color.orange;
    		case 8: return Color.lightGray;
    		case 9: return Color.darkGray;
    		case 10: return Color.black;
    		default: System.out.println("Error assigning color");
    		}
    	}
    	return Color.black;
    }

    /**
     * Constructor.  Sets x to the value given.
     *
     * @param x The value of x to initialize the Walker's position.
     */
    public Walker(double x)
    {
	this.x = x;
	id = ++count;
	this.walkerColor = assignColor();
    }
    
    public Walker(double x, double weight)
    {
	this.x = x;
	this.weight = weight;
	id = ++count;
	this.walkerColor = assignColor();
    }
}
