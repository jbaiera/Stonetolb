package com.stonetolb.engine.component.physics;

import java.awt.Rectangle;

import com.stonetolb.engine.Entity;
import com.stonetolb.engine.component.EntityComponent;
import com.stonetolb.engine.physics.CollisionEvent;
import com.stonetolb.engine.physics.PhysicsManager;
import com.stonetolb.util.Pair;

/**
 * This Component is used to handle collision events that may occur
 * in the physics engine. The component stores previous and current
 * state information about the Entity. This component is also used
 * as the {@link CollisionEvent} callback object, and is used to 
 * resolve the collision event.
 * 
 * @author james.baiera
 *
 */
public class CollisionComponent extends EntityComponent implements
		CollisionEvent {

	/** Location of the Entity last update */
	private float lastXPosition;
	private float lastYPosition;
	
	/** Location of the Entity this update */
	private float currentXPosition;
	private float currentYPosition;
	
	/** The offset from the Entity Origin that the bounding box sits*/
	private Pair<Integer, Integer> offset;
	
	/** Binding to the rigid body object in the physics manager */
	private Integer rigidBodyKey;
	
	/** The Physics Manager */
	private PhysicsManager world;
	
	public CollisionComponent(String pId, Rectangle pBounds, PhysicsManager pWorld) {
		id = pId;
		world = pWorld;
		offset = new Pair<Integer, Integer>(pBounds.x, pBounds.y);
		
		// We register this object as the handler for collisions
		rigidBodyKey = pWorld.requestRigidBody(pBounds, this);
		
		// These floats are not pairs because this is faster than making and throwing out
		// Pair objects (one hundred * entities in world) times per second
		lastXPosition = 0F;
		lastYPosition = 0F;
		currentXPosition = 0F;
		currentYPosition = 0F;
	}
	
	@Override
	public void setOwner(Entity pParent) {
		parent = pParent;
		
		//Update the bounding box position and our logs of our last position.
		world.updateObjectPosition(rigidBodyKey
				, offset.x + parent.getAbsolute().x.intValue()
				, offset.y + parent.getAbsolute().y.intValue()
				);
		
		updateLastPosition(parent.getPosition());
		updateCurrentPosition(parent.getPosition());
	}
	
	/**
	 * Called in the event that a collision occurs.
	 */
	@Override
	public void onCollision() {
		//Set the position to be at the last spot that was good
		parent.setPosition(new Pair<Float, Float>(lastXPosition, lastYPosition));
		currentXPosition = lastXPosition;
		currentYPosition = lastYPosition;
	}

	@Override
	public void update(long delta) {
		//Update last position to be the current before
		lastXPosition = currentXPosition;
		lastYPosition = currentYPosition;
		
		//update current to be the new current
		updateCurrentPosition(parent.getPosition());
		
		//if we moved update the bounding box
		if(currentXPosition != lastXPosition || currentYPosition != lastYPosition) 
		{
			//Location of the box is the offset plus absolute
			world.updateObjectPosition(rigidBodyKey
					, offset.x + parent.getAbsolute().x.intValue()
					, offset.y + parent.getAbsolute().y.intValue()
					);
		}
	}

	private void updateLastPosition(Pair<Float,Float> pos)
	{
		lastXPosition = pos.x.floatValue();
		lastYPosition = pos.y.floatValue();
	}
	
	private void updateCurrentPosition(Pair<Float,Float> pos) {
		currentXPosition = pos.x.floatValue();
		currentYPosition = pos.y.floatValue();
	}
}
