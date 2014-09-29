package dmc;

/**
 * This class extends the DMC class to provide a potential
 * for the simple harmonic oscillator.
 *
 * @author Huy Nguyen and Ian Terrell
 */
public class DMC_SHO extends DMC
{    
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
    public DMC_SHO(int numWalkers, double refEnergy, boolean refEnergyConstant,
		   double dTau, double alpha, long seed, 
		   int initMode, double param1, double param2, boolean individual)
    {
	super(numWalkers, refEnergy, refEnergyConstant,dTau,alpha,seed,
	      initMode,param1,param2,individual);
	
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
    public DMC_SHO(int numWalkers, double initialPosition, long seed,boolean individual)
    {
	super(numWalkers,initialPosition,seed,individual);
    }
    
    
    /**
     * This is the potential energy function of the simple
     * harmonic oscillator, V = 1/2 x^2 in dimensionless units.
     *
     * @param x The point at which to get the potential
     * @return Returns the potential energy at point x
     */
    public double V(double x) 
    {
	return 0.5 * x * x;
    }    
}
