package com.stonetolb.engine.component.physics;

import com.stonetolb.util.AxisAlignedBoundingBox;
import com.stonetolb.util.Vector2f;

public class StaticBody extends Body {
	
	private Vector2f position;
	
	public StaticBody(int pX, int pY, int pW, int pH, int pXOff, int pYOff) {
		super(new AxisAlignedBoundingBox(pX, pY, pW, pH), pXOff, pYOff);
		position = aabb.getPosition();
	}
	
	public StaticBody(AxisAlignedBoundingBox pAABB, int pXOff, int pYOff) {
		super(pAABB, pXOff, pYOff);
		position = aabb.getPosition();
	}
	
	@Override
	public int getMass() {
		return 1000;
	}
	
	@Override
	public Vector2f transform(Vector2f change) {
		return position;
	}
}
