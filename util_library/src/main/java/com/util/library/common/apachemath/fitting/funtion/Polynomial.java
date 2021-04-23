package com.util.library.common.apachemath.fitting.funtion;



import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.util.MathUtils;

public class Polynomial implements ParametricUnivariateFunction
{
	@Override
	public double[] gradient(double x, double... parameters)
	{
		final double[] gradient = new double[parameters.length];
		double xn = 1.0;
		for (int i = 0; i < parameters.length; ++i)
		{
			gradient[i] = xn;
			xn *= x;
		}
		return gradient;
	}

	@Override
	public double value(final double x, final double... coefficients)
			throws IllegalArgumentException
	{
		MathUtils.checkNotNull(coefficients);
		int n = coefficients.length;
		if (n == 0) {
			throw new IllegalArgumentException();
		}
		double result = coefficients[n - 1];
		for (int j = n - 2; j >= 0; j--) {
			result = x * result + coefficients[j];
		}
		return result;
	}
}
