package com.util.library.common.stringparser.func;

import com.util.library.common.stringparser.PascalsTriangle;

/**
 * The combination function.
 * 
 * @see com.util.library.common.stringparser.PascalsTriangle
 */
public class CombinFunction implements Function {

	public CombinFunction() {
	}

	/**
	 * Returns the number of ways r items can be chosen from n items. The value
	 * of n is (int) d[0] and the value of r is (int) d[1].
	 */
	public double of(double[] d, int numParam) {
		int n = (int) d[0];
		int r = (int) d[1];
		return PascalsTriangle.nCr(n, r);
	}

	/**
	 * Returns true only for 2 parameters, false otherwise.
	 */
	public boolean acceptNumParam(int numParam) {
		return numParam == 2;
	}

	public String toString() {
		return "combin(n, r)";
	}
}