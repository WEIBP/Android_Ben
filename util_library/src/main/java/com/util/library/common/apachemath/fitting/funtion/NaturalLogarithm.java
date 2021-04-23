package com.util.library.common.apachemath.fitting.funtion;


import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.util.FastMath;

public class NaturalLogarithm implements ParametricUnivariateFunction
{
	/**
	 * Parametric natural Logarithm Function like y=a + blnx
	 * 
	 */
	@Override
	public double value(double x, double... parameters) throws IllegalArgumentException
	{
		vaidateParameters(parameters);
		double a = parameters[0];
		double b = parameters[1];
		
		return (a + b* FastMath.log(x));
	}

	@Override
	public double[] gradient(double x, double... parameters) throws IllegalArgumentException
	{
		validateDomain(x);
		vaidateParameters(parameters);
		return new double[]{
				1,
				FastMath.log(x)
			};
	}
	
	private void vaidateParameters(double[] param) throws IllegalArgumentException
	{
		if (param == null)
			throw new IllegalArgumentException();
		else if (param.length != 2)
			throw new IllegalArgumentException();
		else if (param[0]==0 || param[1]==0)
			throw new IllegalArgumentException();
	}
	
	private void validateDomain(double x) throws IllegalArgumentException
	{
		if (x<=0)
			throw new IllegalArgumentException();
	}
}
