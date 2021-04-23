package com.util.library.common.stringparser;

/**
 * Exception thrown if expression cannot be parsed correctly.
 * 
 * @see com.math.stringparser.ExpressionTree
 */
public class ExpressionParseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String descrip = null;
	private int index = 0;

	public ExpressionParseException(String descrip, int index) {
		this.descrip = descrip;
		this.index = index;
	}

	public String getDescription() {
		return descrip;
	}

	public int getIndex() {
		return index;
	}

	public String toString() {
		return "(" + index + ") " + descrip;
	}
}