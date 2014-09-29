/**
 * This Java package is a port of the RVGS library written in C .
 * This ported class is derived from the java.util.Random class, and uses
 * its pseudo-random number generator.
 *
 */
/* -------------------------------------------------------------------------- 
 * This is an ANSI C library for generating random variates from 
 *      Uniform(a, b)     a < x < b     (a + b)/2    (b - a)*(b - a)/12 
 *      Normal(m, s)      all x         m            s*s
 *
 * Name              : rvgs.c  (Random Variate GeneratorS)
 * Author            : Steve Park & Dave Geyer
 * Language          : ANSI C
 * Latest Revision   : 10-28-98
 * --------------------------------------------------------------------------
 */

package rvg;

import java.lang.Math;
import java.util.Random;

/**
 * This Java package is a port of the RVGS library written in C 
 * This ported class is derived from the java.util.Random class, and uses
 * its pseudo-random number generator.
 *
 * @author Steve Park and Dave Geyer
 */
public class VariateGenerator extends Random 
{
    /**
     * Public constructor.
     *
     * @param seed The long seed with which to seed the parent Random class.
     */
    public VariateGenerator(long seed)
    {
	super(seed);
    }
    
    /**
     * Generates a Uniformly distributed random variate.
     *
     * @param a The lower bound.
     * @param b The upper bound
     * @return Returns a uniformly distributed real number between a and b.
     */
    public double Uniform(double a, double b)
	/* =========================================================== 
	 * Returns a uniformly distributed real number between a and b. 
	 * NOTE: use a < b
	 * ===========================================================
	 */
    { 
	return (a + (b - a) * nextDouble());
    }

    /**
     * Returns a normal (Gaussian) distributed real number.
     *
     * @param m The mean.
     * @param s The standard deviation.
     * @return Returns a normal (Gaussian) distributed real number.
     */
    public double Normal(double m, double s)
	/* ========================================================================
	 * Returns a normal (Gaussian) distributed real number.
	 * NOTE: use s > 0.0
	 *
	 * Uses a very accurate approximation of the normal idf due to Odeh & Evans, 
	 * J. Applied Statistics, 1974, vol 23, pp 96-97.
	 * ========================================================================
	 */
    { 
	final double p0 = 0.322232431088;     final double q0 = 0.099348462606;
	final double p1 = 1.0;                final double q1 = 0.588581570495;
	final double p2 = 0.342242088547;     final double q2 = 0.531103462366;
	final double p3 = 0.204231210245e-1;  final double q3 = 0.103537752850;
	final double p4 = 0.453642210148e-4;  final double q4 = 0.385607006340e-2;
	double u, t, p, q, z;

	u   = nextDouble();
	if (u < 0.5)
	    t = Math.sqrt(-2.0 * Math.log(u));
	else
	    t = Math.sqrt(-2.0 * Math.log(1.0 - u));
	p   = p0 + t * (p1 + t * (p2 + t * (p3 + t * p4)));
	q   = q0 + t * (q1 + t * (q2 + t * (q3 + t * q4)));
	if (u < 0.5)
	    z = (p / q) - t;
	else
	    z = t - (p / q);
	return (m + s * z);
    }
}
