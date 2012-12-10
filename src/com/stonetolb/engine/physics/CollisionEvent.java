package com.stonetolb.engine.physics;

/**
 * CollisionEvent is an Interface that defines what should
 * be done in the event that a collision of a rigid body occurs.
 * 
 * @author james.baiera
 *
 */
public interface CollisionEvent {
	
	public void onCollision();
	
}
