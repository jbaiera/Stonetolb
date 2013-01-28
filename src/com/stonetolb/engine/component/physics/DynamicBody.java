package com.stonetolb.engine.component.physics;

import com.stonetolb.util.AxisAlignedBoundingBox;
import com.stonetolb.util.Vector2f;

public class DynamicBody extends Body {

	public DynamicBody(int pX, int pY, int pW, int pH, int pXOff, int pYOff) {
		super(new AxisAlignedBoundingBox(pX, pY, pW, pH), pXOff, pYOff);
	}
	
	public DynamicBody(AxisAlignedBoundingBox pAABB, int pXOffset, int pYOffset) {
		super(pAABB, pXOffset, pYOffset);
	}

	@Override
	public int getMass() {
		return 1;
	}

	@Override
	public Vector2f transform(Vector2f change) {
		aabb = aabb.transform((int)change.getX(), (int)change.getY());
		return aabb.getPosition();
	}
	
	
}
