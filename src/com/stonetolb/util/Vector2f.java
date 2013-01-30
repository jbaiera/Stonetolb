package com.stonetolb.util;

/**
 * Immutable Vector object that represents 2 floats. 
 * <br><br>
 * Implementation is pretty light right now. Will expand as needed.
 * 
 * @author james.baiera
 *
 */
public class Vector2f {
	private float x;
	private float y;

	public static Vector2f NULL_VECTOR = new Vector2f(0f, 0f);
	
	public static Vector2f from(float x, float y) {
		if (x == 0f && y == 0f) {
			return NULL_VECTOR;
		} else {
			return new Vector2f(x,y);
		}
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f add(Vector2f other) {
		return new Vector2f(x + other.x, y + other.y);
	}
	
	public Vector2f sub(Vector2f other) {
		return new Vector2f(x - other.x, y - other.y);
	}
	
	public Vector2f normalize() {
		float len = (float)Math.sqrt((x*x) + (y*y));
		return new Vector2f(x/len, y/len);
	}
	
	public Vector2f getXFactor() {
		return new Vector2f(x, 0f);
	}
	
	public Vector2f getYFactor() {
		return new Vector2f(0f, y);
	}
	
	public float getX() {
		return x;
	}
	
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
