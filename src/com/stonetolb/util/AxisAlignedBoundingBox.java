package com.stonetolb.util;

public class AxisAlignedBoundingBox {
	/* center coordinates */
	private int x;
	private int y;
	
	/* dimensions */
	private int width;
	private int height;
	
	public AxisAlignedBoundingBox(int pX, int pY, int pW, int pH) {
		x = pX;
		y = pY;
		width = pW;
		height = pH;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getHalfWidth() { return width/2; }
	public int getHalfHeight() { return height/2; }
	public Vector2f getPosition() { return Vector2f.from(x, y); }
	
	public AxisAlignedBoundingBox transform(int pX, int pY) {
		return new AxisAlignedBoundingBox(x + pX, y + pY, width, height);
	}
	
	public AxisAlignedBoundingBox resize(int pW, int pH) {
		return new AxisAlignedBoundingBox(x, y, pW, pH);
	}
	
	/**
	 * Returns the Vector2f needed to resolve a collision between this and the other AABB
	 * object. If there is no collision, then the return value is a null vector.
	 * 
	 * @param other
	 * @return
	 */
	public Vector2f getOverlapVector(AxisAlignedBoundingBox other) {
		// distance between centers
		float xAxis = Math.abs(x - other.x);
		float yAxis = Math.abs(y - other.y);
		
		// combined width and height
		int cw = getHalfWidth() + other.getHalfWidth();
		int ch = getHalfHeight() + other.getHalfHeight();
		
		//Early out for non overlaps
		if (xAxis > cw ) { return Vector2f.NULL_VECTOR; }
		if (yAxis > ch ) { return Vector2f.NULL_VECTOR; } 
		
		//overlap
		int ox = (int)Math.abs(xAxis - cw);
		int oy = (int)Math.abs(yAxis - ch);
		
		return Vector2f.from(ox, oy);
	}
	
	/**
	 * Returns an AABB in the resolved position according to the Single Axis Theorem
	 * collision resolution strategy.
	 * 
	 * @param pushing The AABB that is staying in it's location
	 * @param overlap The overlap vector
	 * @return resolution vector that must be applied to this box to resolve collision
	 */
	public Vector2f getCollisionResolutionVector(AxisAlignedBoundingBox pushing, Vector2f overlap) {
		Vector2f dir = Vector2f
				.from(x, y)
				.sub(Vector2f.from(pushing.x, pushing.y))
				.normalize();
		
		if (overlap.getX() > overlap.getY()) {
			return Vector2f.from(0, (int)(dir.getY() * (overlap.getY() + 1)));
		} else if (overlap.getY() > overlap.getX()) {
			return Vector2f.from((int)(dir.getX() * (overlap.getX() + 1)), 0);
		} else {
			return Vector2f.from((int)(dir.getX() * (overlap.getX() + 1)), (int)(dir.getY() * (overlap.getY() + 1)));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		result = prime * result + x;
		result = prime * result + y;
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
		AxisAlignedBoundingBox other = (AxisAlignedBoundingBox) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AxisAlignedBoundingBox [x=" + x + ", y=" + y + ", width="
				+ width + ", height=" + height + "]";
	}
}
