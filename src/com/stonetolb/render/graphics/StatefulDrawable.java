package com.stonetolb.render.graphics;

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