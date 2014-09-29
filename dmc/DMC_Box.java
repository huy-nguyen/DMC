package dmc;

import java.util.Vector;
import java.util.Iterator;
import java.lang.Math;
import java.lang.ArithmeticException;
import java.lang.System;

public class DMC_Box extends DMC{
	
	public static double  boundary = 3;
	public DMC_Box(int numWalkers, double refEnergy, boolean refEnergyConstant,
			   double dTau, double alpha, long seed, 
			   int initMode, double param1, double param2)
	    {
		super(numWalkers, refEnergy, refEnergyConstant,dTau,alpha,seed,
		      initMode,param1,param2,false);/*set to false for now*/
		
	    }
	public DMC_Box(int numWalkers, double initialPosition, long seed) {
		super(numWalkers,initialPosition,seed,false);
	}
	
	public void Iterate()
    {
    //totalNumOfIterations++;
	walk();
	branch();
	acceptanceRatio = (double ) numOfAcceptedSteps / totalNumOfIterations;
	if (acceptanceRatio > 0) {
		dTau *= acceptanceRatio;
	}
	tau += dTau;
    }
	
	public double V(double x) {
		return 0;
	}
    
	 public void walk() {
	    	double totalEnergy = 0.0;
	    	double totalLocalEnergy = 0.0;
	    	Iterator i = walkers.iterator();
	    	
	    	while (i.hasNext()) {
	    		Walker w = (Walker) i.next();
	    		totalNumOfIterations++;				
				double oldPosition = w.x;
				double newPosition = oldPosition + .5* dTau * drift(w.x) +Math.sqrt(dTau) * rvg.Normal(0.0,1.0);
				if( (newPosition < boundary) && (newPosition > -boundary) ) {
					w.x = newPosition;
				}
				else {
					i.remove();
				}
			}
	    	
	    	for (Object j : walkers) {
				Walker s = (Walker)j;
				totalEnergy += V(s.x);
				totalLocalEnergy += localEnergy(s.x);
			}
	    	int n = walkers.size();
	    	if (n == 0) {
	    	    throw new ArithmeticException();
	    	}
	    	double avg = totalEnergy / (double) n;
	    	averageLocalEnergy = totalLocalEnergy / (double) n;
	    	if (!refEnergyConstant) {
	    	    if (alpha < 0)
	    		refEnergy = avg - ((double)(n-numWalkers)) / (((double)numWalkers)*dTau);
	    	    else
	    		refEnergy = avg - alpha*((double)n-numWalkers)/(((double)numWalkers));
	    	} 
	    }
	 public boolean acceptOrReject(double oldPosition, double newPosition) {
	    	if ( ( newPosition < boundary ) && (newPosition > -boundary) ) {
	    		return true;
	    	}
	    	else {
	    		return false;
	    	}
	    	
	    }  
}
