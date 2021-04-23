package com.util.library.common.apachemath.fitting.funtion;


import org.apache.commons.math3.analysis.ParametricUnivariateFunction;

/**
 * Parametric hyperbola function like y=a+b/x
 * @author Somnus.V
 */
public class Hyperbola implements ParametricUnivariateFunction
{


	@Override
	public double value(double x, double... parameters) 
			throws IllegalArgumentException
	{
		validatexDomain(x);
		validateParameters(parameters);
		double a=parameters[0];
		double b=parameters[1];
		return (a + b/x);
	}

	@Override
	public double[] gradient(double x, double... parameters) 
			throws IllegalArgumentException
	{
		validatexDomain(x);
		validateParameters(parameters);
		return new double[]{
				1.0,
				1/x};
	}
	
	private void validateParameters(double[] param) 
			throws IllegalArgumentException
	{
		if (param==null)
			throw new IllegalArgumentException();
		if (param.length!=2)
			throw new IllegalArgumentException();
		if (param[1]==0)
			throw new IllegalArgumentException();
	}
	
	private void validatexDomain(double x) 
			throws IllegalArgumentException
	{
		if (x==0)
			throw new IllegalArgumentException();
	}
}
