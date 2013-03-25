package com.stonetolb.render.util;

import com.stonetolb.util.Vector2f;

/**
 * Interface that is used by the {@link Camera} to control
 * perspective, view port size, and movement of the camera's
 * location.
 *  
 * @author james.baiera
 *
 */
public interface Vantage {
	
	/**
	 * Called to change the Vantage's expected location.
	 * @param targetPosition - Destination to move to.
	 */
	public void updatePosition(Vector2f targetPosition);
	
	/**
	 * Called to set the Vantage's current location.
	 * @param newCurrentPosition - New vantage position.
	 */
	public void setPosition(Vector2f newCurrentPosition);
	
	/**
	 * Called to actually update the Vantage's position every frame. 
	 * @param delta - Time since last update.
	 */
	public void update(long delta);
}
