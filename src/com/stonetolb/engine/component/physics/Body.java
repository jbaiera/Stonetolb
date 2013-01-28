package com.stonetolb.engine.component.physics;

import com.artemis.Component;
import com.stonetolb.util.AxisAlignedBoundingBox;
import com.stonetolb.util.Vector2f;

public abstract class Body extends Component {
	protected AxisAlignedBoundingBox aabb;
	protected Vector2f offset;
	
	public AxisAlignedBoundingBox getAABB() {
		return aabb;
	}
	
	public Body(AxisAlignedBoundingBox pAABB, int pXOffset, int pYOffset) {
		aabb = pAABB.transform(pXOffset, pYOffset); //Offset the bounding box.
		offset = Vector2f.from(pXOffset, pYOffset);
	}
	
	public Vector2f getOffset() {
		return offset;
	}
	
	public abstract int getMass();
	
	public abstract Vector2f transform(Vector2f change);
}
