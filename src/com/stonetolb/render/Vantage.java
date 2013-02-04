package com.stonetolb.render;

import com.stonetolb.util.Vector2f;

/**
 * Interface that defines the contract between the {@link Camera} and
 * it's specific moving and perspective attributes.
 * @author james.baiera
 *
 */
public interface Vantage {
	
	/**
	 * Called to change the Vantage's expected location.
	 * @param target - Destination to move to.
	 */
	public void updatePosition(Vector2f target);
	
	/**
	 * Called to set the Vantage's current location.
	 * @param target - New vantage position.
	 */
	public void setPosition(Vector2f target);
	
	/**
	 * Called to actually update the Vantage's position every frame. 
	 * @param delta - Time since last update.
	 */
	public void update(long delta);
}
