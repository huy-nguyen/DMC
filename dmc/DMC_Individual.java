package dmc;

import rvg.VariateGenerator;
import java.util.Vector;
import java.util.Iterator;
import java.lang.Math;
import java.lang.ArithmeticException;

import java.lang.System;

public class DMC_Individual extends DMC{
	
	
	
	public DMC_Individual(int numWalkers, double refEnergy, boolean refEnergyConstant,
			   double dTau, double alpha, long seed, 
			   int initMode, double param1, double param2)
	{
		super(numWalkers, refEnergy, refEnergyConstant,dTau,alpha,seed,
			      initMode,param1,param2,false);/*set to false for now*/		
	}
	
}
