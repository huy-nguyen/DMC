package dmc;

import java.util.Vector;
import java.util.Random;

public class DMC_SHO_Gaussian extends DMC{
	
	public double variationalParam;
	
	public DMC_SHO_Gaussian(int numWalkers, double refEnergy, boolean refEnergyConstant,
			   double dTau, double alpha, long seed, 
			   int initMode, double param1, double param2, double variation, boolean individual) 
	{
		super(numWalkers, refEnergy, refEnergyConstant,dTau,alpha,seed,
			      initMode,param1,param2, individual);
		this.variationalParam = variation;		
	}
	
	public DMC_SHO_Gaussian(int numWalkers, double initialPosition, long seed, double variation, boolean individual) {
		super(numWalkers,initialPosition,seed, individual);
		this.variationalParam = variation;		
	}
    
	
	public double V(double x) {
		return 0.5 * x * x;
	}
    
	
	public double drift(double x) {
		double result = (-4 * variationalParam * x);
    	return result;
	}
    
	public double localEnergy(double x) {
		double result =  variationalParam + x*x*( .5 - 2 * variationalParam * variationalParam);		
		return result;
	}
	
	public double Weight(double x) {		
		double result = Math.exp((refEnergy - localEnergy(x))*dTau);
		return result;
	}	
	
	public double calculateLocalEnergy() {
		double totalLocalEnergy = 0.0;
		for(Object j : walkers) {
    		totalLocalEnergy += localEnergy( ((Walker)j).x );
    		
    	}
		return (double ) totalLocalEnergy / walkers.size(); 
	}
	
	public boolean acceptOrReject(double oldPosition, double newPosition) {
    	double trialRatio = Math.exp(- 2 * variationalParam * (oldPosition * oldPosition - newPosition * newPosition ));
    	double numerExponent = newPosition - oldPosition - 0.5 * dTau * drift(oldPosition);
    	double denomExponent = oldPosition - newPosition - 0.5 * dTau * drift(newPosition);
    	double numerator = Math.exp(- numerExponent * numerExponent / (4 * 0.5 * dTau));
    	double denominator = Math.exp(- denomExponent * denomExponent / (4 * 0.5 * dTau));
    	double q = trialRatio * numerator / denominator;
    	
    	Random generator = new Random();
    	double r = generator.nextDouble();
    	if( r < q) {
    		return true;
    	}
    	else {
    		return false;
    	}
    	//return true;
    	
    } 
	
	public void walk() {
    	double totalEnergy = 0.0;
    	double totalLocalEnergy = 0.0;
    	for (Object i : walkers) {
    		totalNumOfIterations++;
			Walker w =(Walker)i;
			double oldPosition = w.x;
			
			double driftForce = .5* dTau * drift(w.x);
			w.drift = driftForce;
			double newPosition = oldPosition + driftForce +Math.sqrt(dTau) * rvg.Normal(0.0,1.0);
			if( acceptOrReject(oldPosition, newPosition)) {
				w.x = newPosition;
				numOfAcceptedSteps++;
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
    		refEnergy = avg - ((double)(n-numWalkers))/(((double)numWalkers)*dTau);
    	    else
    		refEnergy = avg - alpha*((double)n-numWalkers)/(((double)numWalkers));
    	} 
    }
}
