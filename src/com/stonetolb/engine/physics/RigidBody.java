package com.stonetolb.engine.physics;

import java.awt.Rectangle;

/**
 * RigidBody is an object that contains a location and a bounding box.
 * @author james.baiera
 *
 */
public class RigidBody {
	protected CollisionEvent event;
	protected Rectangle bounds;
	protected boolean inUse;
	
	public RigidBody(Rectangle pBounds, CollisionEvent pEvent) {
		event = pEvent;
		bounds = pBounds;
		inUse = true;
	}
	
	public RigidBody() {
		event = null;
		bounds = null;
		inUse = false;
	}
	
	public void init(Rectangle pBounds, CollisionEvent pEvent) {
		if(!inUse && pBounds != null && pEvent != null) {
			event = pEvent;
			bounds = pBounds;
			inUse = true;
		}
	}
	
	public void clear() {
		inUse = false;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void updatePosition(int x, int y) {
		if (inUse) {
			bounds.x = x;
			bounds.y = y;
		}
	}
	
	public CollisionEvent collidesWith(RigidBody pOther) {
		if(inUse && pOther.inUse) {
			if (bounds.intersects(pOther.bounds)) {
				return event;
			}
		}
		
		return null;
	}
}
