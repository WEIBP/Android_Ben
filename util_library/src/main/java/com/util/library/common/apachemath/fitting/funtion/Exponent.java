package com.util.library.common.apachemath.fitting.funtion;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.util.FastMath;

public class Exponent implements ParametricUnivariateFunction
{
	/**
	 * Parametric Exponent Function class like y=a*(e^x)+b
	 * 
	 */
	@Override
	public double value(double x, double... parameters) throws
            IllegalArgumentException
	{
		if (validateParameters(parameters)) {
			double a = parameters[0];
			double b = parameters[1];

			return (a * (FastMath.exp(x))+b);
		} else {
			return Double.NaN;
		}
	}
	

	@Override
	public double[] gradient(double x, double... parameters)
	{
		validateParameters(parameters);
		return new double[]{
				Math.exp(x),
				1
				};
	}
	
	private boolean validateParameters(double[] param)
	{
		if (param == null)
			return false;
		if (param.length != 2)
			return false;
		if (param[0] == 0)
			return false;
		return true;
	}

}
