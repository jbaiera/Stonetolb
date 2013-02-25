package com.stonetolb.util;

/**
 * A simple Pair utility object.
 * 
 * @author james.baiera
 *
 * @param <T1> First in pair
 * @param <T2> Second in pair
 * @see Vector2f
 */
public class Pair<T1, T2> {
	/**
	 * X component.
	 */
	public T1 x;
	
	/**
	 * Y component.
	 */
	public T2 y;

	/**
	 * Constructs a new Pair.
	 * 
	 * @param pXValue - X value.
	 * @param pYValue - Y value.
	 */
	public Pair(T1 pXValue, T2 pYValue) {
		x = pXValue;
		y = pYValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair<?,?> other = (Pair<?,?>) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pair [x=" + x + ", y=" + y + "]";
	}
	
}
