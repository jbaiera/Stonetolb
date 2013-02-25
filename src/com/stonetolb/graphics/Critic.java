package com.stonetolb.graphics;

/**
 * Interface that defines handler methods for the general Drawable
 * Interface Definition Tree. Should an object require to perform 
 * specific actions based on the type of Drawable that an object 
 * may be, it should implement this interface and override the 
 * corresponding handler methods.
 * 
 * @author james.baiera
 *
 */
public interface Critic {
	/**
	 * Called if the {@link Drawable} that accepts this Critic 
	 * implements the Drawable Interface only.
	 * @param image
	 */
	public void analyze(Drawable image);
	
	/**
	 * Called if the {@link Drawable} that accepts this Critic
	 * implements the {@link StatefulDrawable} Interface.
	 * @param image
	 */
	public void analyze(StatefulDrawable image);
}
