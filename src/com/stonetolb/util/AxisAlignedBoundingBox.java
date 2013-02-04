package com.stonetolb.util;

/**
 * A standard implementation of the Axis Aligned Bounding Box.
 * Similar to the Rectangle, the AxisAlignedBoundingBox (AABB for short)
 * is a shape that is rectangular, but has a different point of origin.
 * <p>
 * A standard Rectangle is denoted as the point (x,y) of it's top left corner
 * and a vector (w,h) to denote it's dimensions. The AABB is denoted with the
 * same dimension vector (w,h), but it's origin point (x,y) is that of it's center.
 * <p>
 * This implementation is immutable, therefore all calls will return new AABB objects
 * instead of modifying internal state.
 * 
 * @author james.baiera
 *
 */
public class AxisAlignedBoundingBox {
	/* center coordinates */
	private final int x;
	private final int y;
	
	/* dimensions */
	private final int width;
	private final int height;
	
	/**
	 * Default constructor.
	 * @param pX - Horizontal location.
	 * @param pY - Vertical location.
	 * @param pW - Box width.
	 * @param pH - Box height.
	 */
	public AxisAlignedBoundingBox(int pX, int pY, int pW, int pH) {
		x = pX;
		y = pY;
		width = pW;
		height = pH;
	}
	
	/**
	 * @return Horizontal location.
	 */
	public int getX() { return x; }
	
	/**
	 * @return Vertical location.
	 */
	public int getY() { return y; }
	
	/**
	 * @return Distance from origin to the lateral sides.
	 */
	public int getHalfWidth() { return width/2; }
	
	/**
	 * @return Distance from origin to the dorsal sides.
	 */
	public int getHalfHeight() { return height/2; }
	
	/**
	 * @return {@link Vector2f} of the box's position.
	 */
	public Vector2f getPosition() { return Vector2f.from(x, y); }
	
	/**
	 * Translates the box the given distance.
	 * @param pX - Distance to move horizontally.
	 * @param pY - Distance to move vertically.
	 * @return new AABB object in new location.
	 */
	public AxisAlignedBoundingBox transform(int pX, int pY) {
		return new AxisAlignedBoundingBox(x + pX, y + pY, width, height);
	}
	
	/**
	 * Resizes the box to the given dimensions.
	 * @param pW - New width.
	 * @param pH - New height.
	 * @return new AABB object with new dimensions.
	 */
	public AxisAlignedBoundingBox resize(int pW, int pH) {
		return new AxisAlignedBoundingBox(x, y, pW, pH);
	}
	
	/**
	 * Returns the Vector2f needed to resolve a collision between another AABB
	 * object. If there is no collision, then the return value is {@link Vector2f#NULL_VECTOR}.
	 * 
	 * @param other
	 * @return {@link Vector2f} that resolves the collision if it exists. 
	 * <br> {@link Vector2f#NULL_VECTOR} if no collision exists.
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
	 * Returns position of this AABB in the resolved position according to the Single Axis Theorem
	 * collision resolution strategy.
	 * 
	 * @param pushing - The AABB acting upon this AABB.
	 * @param overlap - The overlap vector
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
