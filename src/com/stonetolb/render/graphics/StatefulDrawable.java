package com.stonetolb.render.graphics;

/**
 * This interface is for items that are Drawable but require some 
 * preparation before and cleanup after being drawn
 * 
 * @author james.baiera
 *
 */
public interface StatefulDrawable extends Drawable{

	/**
	 * Hook that readies what ever is about to be drawn
	 */
	public abstract void ready();

	/**
	 * Hook that cleans up what ever was being drawn
	 */
	public abstract void dispose();

}