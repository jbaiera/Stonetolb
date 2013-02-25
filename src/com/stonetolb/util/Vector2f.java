package com.stonetolb.util;

/**
 * Vector data object that represents a two dimensional matrix of floats.
 * This object can be treated as a standard (x,y) pair of type float. It
 * should be noted that this object is immutable; All methods from this 
 * object return new objects as a result.
 * 
 * @author james.baiera
 *
 */
public class Vector2f {
	private float x;
	private float y;

	/**
	 * The standard null vector (0f,0f).
	 */
	public static Vector2f NULL_VECTOR = new Vector2f(0f, 0f);
	
	/**
	 * Creates a new {@link Vector2f} based on the given values.
	 * @param x - X value.
	 * @param y - Y value.
	 * @return A {@link Vector2f} object based on the given values.
	 */
	public static Vector2f from(float x, float y) {
		if (x == 0f && y == 0f) {
			return NULL_VECTOR;
		} else {
			return new Vector2f(x,y);
		}
	}
	
	/**
	 * Default constructor. It is advised to use {@link Vector2f#from(float, float)}
	 * instead of this due to caching logic.
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the sum of the two Vector2f objects.
	 * @param other - The second Vector2f.
	 * @return The sum as a new Vector2f.
	 */
	public Vector2f add(Vector2f other) {
		return new Vector2f(x + other.x, y + other.y);
	}
	
	/**
	 * Returns the difference of the two Vector2f objects.
	 * @param other - The second Vector2f.
	 * @return The difference as a new Vector2f.
	 */
	public Vector2f sub(Vector2f other) {
		return new Vector2f(x - other.x, y - other.y);
	}
	
	/**
	 * Normalizes the Vector2f to a vector of length 1f pointing
	 * in the original Vector2f's direction.  
	 * @return - The normalized Vector2f as a new object;
	 */
	public Vector2f normalize() {
		float len = (float)Math.sqrt((x*x) + (y*y));
		return new Vector2f(x/len, y/len);
	}
	
	/**
	 * Returns a new Vector2f with the Y value set to 0f and the
	 * original X value.
	 * @return This X component of this Vector2f.
	 */
	public Vector2f getXFactor() {
		return new Vector2f(x, 0f);
	}
	
	/**
	 * Returns a new Vector2f with the X value set to 0f and the
	 * original Y value.
	 * @return This Y component of this Vector2f.
	 */
	public Vector2f getYFactor() {
		return new Vector2f(0f, y);
	}
	
	/**
	 * Returns the X value.
	 * @return the value of X.
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Returns the Y value.
	 * @return the value of Y.
	 */
	public float getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
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
		Vector2f other = (Vector2f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vector2f [x=" + x + ", y=" + y + "]";
	}
	
}
