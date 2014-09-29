package dmc;

import rvg.VariateGenerator;

import java.util.Collections;
import java.util.Vector;
import java.util.Iterator;
import java.lang.Math;
import java.lang.ArithmeticException;

import java.lang.System;

/**
 * A Diffusion Monte Carlo Simulation.
 *
 * This class implements the basic DMC structure as outlined in
 * "Introduction to the Diffusion Monte Carlo Method" by Ioan Kosztin,
 * Byron Faber and Klaus Schulten in 1995.  
 *
 * It only provides the basic methods for the walkers to be moved, 
 * to be born and to die.  The actual simulation loop with start and 
 * end conditions must be implemented in another class.
 *
 * This class is meant to be subclassed; the child class will overwrite 
 * the V() function to give the simulation a valid potential.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class DMC
{
    /*************
     * CONSTANTS *
     *************/
    
    /**
     * The default number of walkers.
     */
    public final static int DEFAULT_NUM_WALKERS = 500;

    /**
     * The default timestep.
     */
    public final static double DEFAULT_DTAU = .1;

    /**
     * The default random variate generator seed.
     */
    public final static long DEFAULT_SEED = 123456789;

    /**
     * The default reference energy.
     */
    public final static double DEFAULT_REF_ENERGY = -1.0;
    
    public static boolean INDIVIDUAL_SIMULATION = true;

    /**
     * The default value to whether or not to hold the reference
     * energy constant.
     */
    public final static boolean DEFAULT_REF_ENERGY_CONSTANT = false;

    /**
     * The default feedback parameter.
     */
    public final static double DEFAULT_ALPHA = -1.0;

    /**
     * The default x_0 used for delta function walker initialization.
     */
    public final static double DEFAULT_DELTA_FNC_X0 = 0.0;

    /**
     * The default minimum x value.
     * (Unenforced, used mainly as a guide for histogram display.)
     */
    public final static double DEFAULT_X_MIN = -5.0;

    /**
     * The default maximum x value.
     * (Unenforced, used mainly as a guide for histogram display.)
     */
    public final static double DEFAULT_X_MAX = 5.0;

    /**
     * The default parameter a for a uniformly distributed
     * walker initialization.
     */
    public final static double DEFAULT_UNIFORM_A = -4.0;

    /**
     * The default parameter b for a uniformly distributed
     * walker initialization.
     */
    public final static double DEFAULT_UNIFORM_B = 4.0;

    /**
     * The default parameter mu for a gaussian distributed
     * walker initialization.
     */
    public final static double DEFAULT_GAUSSIAN_MU = 0.0;

    /**
     * The default parameter sigma for a gaussian distributed
     * walker initialization.
     */
    public final static double DEFAULT_GAUSSIAN_SIGMA = 1.0;
    
    /**
     * Initialization mode for walkers.
     */
    public final static int INIT_DELTA_FNC = 0;
    
    /**
     * Initialization mode for walkers.
     */
    public final static int INIT_UNIFORM = 1;
    
    /**
     * Initialization mode for walkers.
     */
    public final static int INIT_GAUSSIAN = 2;

    /*****************
     * DATA ELEMENTS *
     *****************/

    /**
     * The array of walkers.
     */
    public static Vector walkers;
    
    public static Vector oldWalkers1;
    
    public static Vector oldWalkers2;
    
    public static Vector oldWalkers3;
    
    public static Vector oldWalkers4;
    
    public static Vector oldWalkers5;

    /**
     * The desired number of walkers.
     */
    public int numWalkers;

    /**
     * The timestep.
     */
    public double dTau;

    /**
     * The current time.
     */
    public double tau;

    /**
     * The reference energy, E_r.
     */
    public double refEnergy;

    /**
     * The feedback parameter.
     */
    public double alpha;

    /**
     * The random variate generator.
     */
    public VariateGenerator rvg;

    /**
     * Whether or not to hold the reference energy constant.
     */ 
    public boolean refEnergyConstant;
    
    /**
     * Total number of proposed steps that are accepted
     */ 
    public int numOfAcceptedSteps = 0;
    
    /**
     * Total number of iterations
     */ 
    public int totalNumOfIterations = 0;
    
    public int stepCount = 0;
    
    /**
     * Acceptance ratio = accepted / total 
     */ 
    public double acceptanceRatio;
    
    public double averageLocalEnergy;
    
    /***********
     * METHODS *
     ***********/
        
    /**
     * Constructor.  Takes one of everything and initializes the simulation.
     *
     * @param numWalkers The number of walkers to start the simulation with.
     * @param refEnergy The reference energy to start the simulation with. If it is
     *                    negative, use the current average energy of all the walkers.
     * @param refEnergyConstant Whether or not to hold the reference energy constant.
     * @param dTau The timestep to use.
     * @param alpha The feedback parameter to use (use -1.0 to use the default value
     *              of 1/dTau).
     * @param seed The long int to seed the random variate generator.
     * @param initMode The mode in which to initialize the random walkers.
     * @param param1 First parameter for the initialization mode.
     *               X_0 for delta functions, a for Uniform, mu for Gaussian
     * @param param2 Second parameter for the initialization mode.
     *               unused for delta functions, b for Uniform, sigma for Gaussian
     */
    public DMC(int numWalkers, double refEnergy, boolean refEnergyConstant,
	       double dTau, double alpha, long seed, 
	       int initMode, double param1, double param2, boolean individual)
    {
    INDIVIDUAL_SIMULATION = individual;
	this.numWalkers = numWalkers;
	this.dTau = dTau;
	tau = 0.0;
	this.refEnergyConstant = refEnergyConstant;
	this.alpha = alpha;
	rvg = new VariateGenerator(seed);
	
	// Initialize the walkers:
	double totalEnergy = 0.0;
	walkers = new Vector(numWalkers);
	switch (initMode) {
	case INIT_DELTA_FNC:
	    for (int i = 0; i < numWalkers; i++) {
		Walker w = new Walker(param1);
		totalEnergy += V(w.x);
		walkers.add(w);
	    }
	    break;
	case INIT_UNIFORM:
	    for (int i = 0; i < numWalkers; i++) {
		Walker w = new Walker(rvg.Uniform(param1,param2));
		totalEnergy += V(w.x);
		walkers.add(w);
	    }		
	    break;
	case INIT_GAUSSIAN:
	    for (int i = 0; i < numWalkers; i++) {
		Walker w = new Walker(rvg.Normal(param1,param2));
		totalEnergy += V(w.x);
		walkers.add(w);
	    }
	    break;
	}
	
		oldWalkers1 = new Vector();
		oldWalkers2 = new Vector();
		oldWalkers3 = new Vector();
		oldWalkers4 = new Vector();
		oldWalkers5 = new Vector();
	
	
	if (refEnergy < 0) {
	    this.refEnergy = totalEnergy / numWalkers;
	}
	else {
	    this.refEnergy = refEnergy; 
	}
	
	
	
    }
    

    /**
     * This constructor constructs a simulation with a delta function walker
     * initialization about point initialPosition, the seed given, and the number
     * of walkers given, but everything else with default values.
     *
     * @param numWalkers The number of walkers to start the simulation with.
     * @param initialPosition The position at which to start the walkers.
     * @param seed The long int to seed the random variate generator.
     */
    public DMC(int numWalkers, double initialPosition, long seed, boolean individual)
    {
	this(numWalkers, DEFAULT_REF_ENERGY, DEFAULT_REF_ENERGY_CONSTANT,
	     DEFAULT_DTAU, DEFAULT_ALPHA, seed, INIT_UNIFORM, initialPosition, 0, individual);
    }
    
    /**
     * This is the potential energy function, and is meant
     * to be overwritten by a derived class.
     *
     * @param x The point at which to get the potential
     * @return Returns the potential energy at point x
     */
    public double V(double x)
    {
	return x;
    }

    
    /**
     * This function returns the weight of a walker.
     *
     * @param w The walker to return the weight of.
     */
    public double Weight(double x)
    {
	return Math.exp(-(V(x) - refEnergy)*dTau);
    }
    /**
     * copy the first argument into the second
     * @param walkers1
     * @param walkers2
     */
    Vector copy(Vector walkers1) {    	
    	
    	int n = walkers1.size();
    	Vector walkers2 = new Vector(n);
    	/*for(int i = 0; i < n; i++) {
    		walkers2.add(new Walker(-1));
    	}*/
    	try {
    		walkers2 = (Vector)(ObjectCloner.deepCopy(walkers1));
    	}
    	catch(Exception e) {
    		System.out.println("Exception in main = " +  e);
    	}
    	return walkers2;
    }
    
    void print(Vector walkersVector) {
    	System.out.println("Start of print");
    	for (Object i : walkersVector) {
			System.out.println("Walker " + ((Walker) i).id + " has current position "+ ((Walker) i).x + " and color " + ((Walker) i).walkerColor);
		}
    }
    /**
     * This function does one complete iteration of the simulation,
     * including moving the walkers, branching them, and updating the
     * reference energy.
     */
    public void Iterate()
    {
    if(INDIVIDUAL_SIMULATION) {
    	stepCount++;
        if(stepCount >= 5) {
        	oldWalkers5 = copy(oldWalkers4);
        	oldWalkers4 = copy(oldWalkers3);
        	oldWalkers3 = copy(oldWalkers2);
        	oldWalkers2 = copy(oldWalkers1);
        	oldWalkers2 = copy(oldWalkers1);
        	oldWalkers1 = copy(walkers);
        }
        else if (stepCount==4) {
        	oldWalkers4 = copy(oldWalkers3);
        	oldWalkers3 = copy(oldWalkers2);
        	oldWalkers2 = copy(oldWalkers1);
        	oldWalkers2 = copy(oldWalkers1);
        	oldWalkers1 = copy(walkers);
        }
        else if(stepCount == 3) {
        	oldWalkers3 = copy(oldWalkers2);
        	oldWalkers2 = copy(oldWalkers1);
        	oldWalkers2 = copy(oldWalkers1);
        	oldWalkers1 = copy(walkers);
        }
        if (stepCount == 2) {
        	oldWalkers2 = copy(oldWalkers1);
        	oldWalkers1 = copy(walkers);
        }
        else if(stepCount == 1) {
        	oldWalkers1 = copy(walkers);
        }
    }
    
    
	walk();
	branch();
	
    
	acceptanceRatio = (double ) numOfAcceptedSteps / totalNumOfIterations;
	
	dTau *= acceptanceRatio;
	tau += dTau;
    }

    /**
     * This function moves each of the walkers, and updates the reference
     * energy with respect to their new positions.
     *
     * @throws ArithmeticException Thrown if the number of walkers drops to 0.
     *                             A specialized exception should probably
     *                             be made eventually.
     */  
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
    /**
     * Branches the walkers.  (Birth/Death process)
     */
    public void branch()
    {
	Vector createdWalkers = new Vector();    // Temp holds new walkers
	Iterator i = walkers.iterator();
	while (i.hasNext()) {
	    Walker w = (Walker) i.next();
	    if (INDIVIDUAL_SIMULATION) {
	    	w.weight = Weight(w.x);
	    }
	    
	    int m = (int) (Weight(w.x) + rvg.Uniform(0.0,1.0));
	    //w.weight = m;
	    if (m > 3) {
	    	m = 3;
	    }
	    if (m == 0) {
	    	i.remove();
	    }
	    else {
	    	for (int j = 1; j < m; j++) { 
	    		createdWalkers.add(new Walker(w.x));
	    	}
	    }
	}
	walkers.addAll(createdWalkers);
    }
    
    public boolean acceptOrReject(double oldPosition, double newPosition) {
    	//numOfAcceptedSteps++;    	
    	//return newState;
    	
    	return true;
    }  
    
    public double drift(double x) {
    	return 0;
    }
    
    public double localEnergy(double x) {
		return 0.0;
	}
}
