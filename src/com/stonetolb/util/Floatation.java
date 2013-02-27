package com.stonetolb.util;

/**
 * Float helper class. No shame.
 */
public class Floatation {

	public static boolean equals(float arg1, float arg2) {
		return Float.floatToIntBits(arg1) == Float.floatToIntBits(arg2);
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
	
	public static boolean nonZero(float ... arg1) {
		for(float arg : arg1) {
			if (isZero(arg)) {
				return false;
			}
		}
		
		return true;
	}
	
}
