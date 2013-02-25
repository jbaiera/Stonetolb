package com.stonetolb.graphics;

/**
 * All objects that are capable of being displayed to the screen
 * must implement this interface.
 * 
 * @author james.baiera
 */
public interface Drawable {
	
	/**
	 * Normal call that draws item in a frame.
	 * 
	 * @param x - X coordinate to draw at
	 * @param y - Y coordinate to draw at
	 * @param z - Z coordinate to draw at
	 * @param delta - amount of time that has past since last drawing
	 */
	public void draw(int x, int y, int z, long delta);
	
	/**
	 * Method used for double dispatching on any {@link Critic} object.
	 * @param critic
	 */
	public void accept(Critic critic);
}
