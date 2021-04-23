package com.util.library.common.apachemath.fitting.funtion;


import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.util.FastMath;

public class Sine implements ParametricUnivariateFunction
{

	/**
	 * Parametric Sine function like y=asin(bx+c)+d
	 *  
	 */
	public double value(double x, double... parameters)
			throws IllegalArgumentException
	{
		validateParameters(parameters);
		double a = parameters[0];
		double b = parameters[1];
		double c = parameters[2];
		double d = parameters[3];
		return (a * FastMath.sin(b * x + c) + d);
	}

	public double[] gradient(double x, double... parameters)
			throws IllegalArgumentException
	{
		validateParameters(parameters);
		double a = parameters[0];
		double b = parameters[1];
		double c = parameters[2];
		double d = parameters[3];
		
		return new double[] 
				{
					FastMath.sin(b * x + c) + d,
					a *  FastMath.cos(b * x + c) * x,
					a *  FastMath.cos(b * x + c),
					1.0
				};
	}
	
	private void validateParameters(double[] param) 
			throws IllegalArgumentException
	{
		if (param == null)
			throw new IllegalArgumentException();
		if (param.length != 4)
			throw new IllegalArgumentException();
		if (param[1] == 0)
			throw new IllegalArgumentException();
	}

}
