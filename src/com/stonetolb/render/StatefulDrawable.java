package com.stonetolb.render;

/**
 * This interface is for items that are Drawable but require either
 * preparation before being drawn, cleanup after the fact, or both.
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