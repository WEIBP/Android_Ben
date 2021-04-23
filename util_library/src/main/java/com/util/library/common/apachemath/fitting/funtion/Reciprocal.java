package com.util.library.common.apachemath.fitting.funtion;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.util.FastMath;

/***
* Parametric Reciprocal function like y=1/(a+b*x)
* @author Somnus.V
*/
public class Reciprocal implements ParametricUnivariateFunction
{
	@Override
	public double value(double x, double... parameters) 
			throws IllegalArgumentException
	{
		if (validatexDomainAndParameters(x,parameters) ) return Double.NaN;
		double a=parameters[0];
		double b=parameters[1];
		return 1/(a + b*x);
	}
	/**
	 * 
	 */
	@Override
	public double[] gradient(double x, double... parameters)
			throws IllegalArgumentException
	{
		if (validatexDomainAndParameters(x,parameters) ) return new double[]{};
		double a=parameters[0];
		double b=parameters[1];
		return new double[]{
				-FastMath.pow(a+b*x, -2),
				-x*FastMath.pow(a+b*x, -2)
				};
	}
	
	private boolean validatexDomainAndParameters(Double x, double[] param)
			throws IllegalArgumentException
	{
		return param==null || param.length!=2 ||param[1]==0 || param[0]+param[1]*x == 0;
//		if (param==null)
//			throw new IllegalArgumentException();
//		if (param.length!=2)
//			throw new IllegalArgumentException();
//		if (param[1]==0 || param[0]+param[1]*x == 0)
//			throw new IllegalArgumentException();
	}
}
