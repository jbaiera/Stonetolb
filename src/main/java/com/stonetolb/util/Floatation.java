package com.stonetolb.util;

/**
 * Float helper class. No shame.
 */
public class Floatation {
	private static final double EPSILON = 0.00001;
	private static final double ZERO = 0.0;


	private static boolean floatingPointEpsilonComparison(double valOne, double valTwo) {
		return (valTwo == ZERO) ? (valOne == valTwo) : (Math.abs((valOne / valTwo) - 1.0) < EPSILON);
	}

	public static boolean closeEnough(float arg1, float arg2) {
		return floatingPointEpsilonComparison(arg1, arg2);
	}
	
	public static boolean isZero(float arg1) {
		return Float.floatToIntBits(arg1) == Float.floatToIntBits(0F);
	}
	
	public static boolean isZero(float ... arg1) {
		for(float arg : arg1) {
			if (!isZero(arg)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean nonZero(float arg1) {
		return !isZero(arg1);
	}
}
