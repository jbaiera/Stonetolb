package com.stonetolb.render;

import java.io.IOException;

/**
 * Null object that implements the {@link Drawable} interface.
 * Used as a fall back in the event that an art asset does not exist.
 * 
 * @author james.baiera
 *
 */
public class NullDrawable implements Drawable {
	
	private static final NullDrawable INSTANCE = new NullDrawable();
	
	/**
	 * Gets the instance of the NullDrawable.
	 * @return NullDrawable sprite
	 */
	public static NullDrawable getInstance() {
		return INSTANCE;
	}
	
	private Drawable img;
	
	/**
	 * Default Constructor.
	 */
	private NullDrawable() {
		try
		{
			img = new Sprite("sprites/null.gif");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * {@inheritDoc Drawable}
	 */
	@Override
	public void draw(int x, int y, int z, long delta) {
		img.draw(x, y, z, delta);
	}
	
	/**
	 * {@inheritDoc Drawable}
	 */
	@Override
	public void accept(Critic critic) {
		critic.analyze(this);
	}
}