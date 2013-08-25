package com.stonetolb.engine.component.physics;

import com.stonetolb.util.AxisAlignedBoundingBox;
import com.stonetolb.util.Vector2f;

public class KinematicBody extends Body {

	public KinematicBody(int pX, int pY, int pW, int pH, int pXOff, int pYOff) {
		super(new AxisAlignedBoundingBox(pX, pY, pW, pH), pXOff, pYOff);
	}
	
	public KinematicBody(AxisAlignedBoundingBox pAABB, int pXOff, int pYOff) {
		super(pAABB, pXOff, pYOff);
	}
	
	@Override
	public int getMass() {
		return 50;
	}

	@Override
	public Vector2f transform(Vector2f change) {
		aabb = aabb.transform((int)change.getX(), (int)change.getY()); 
		return aabb.getPosition();
	}
}
