package com.stonetolb.render;

import org.lwjgl.opengl.GL11;

import com.stonetolb.game.Game;
import com.stonetolb.util.Vector2f;

/**
 * FixedVantage object used to represent an orthogonal camera 
 * that is always positioned at the given target location.
 * 
 * @author james.baiera
 *
 */
public final class FixedVantage implements Vantage{
	
	private static volatile FixedVantage INSTANCE = null;
	private static Vector2f ORIGIN = Vector2f.NULL_VECTOR;
	
	private Vector2f position;
	private int screenWidth;
	private int screenHeight;
	
	/**
	 * Creates and returns the singleton instance of the FixedVantage object.
	 * @return FixedVantage instance.
	 */
	public static FixedVantage create() {
		if (INSTANCE == null) {
			synchronized(FixedVantage.class) {
				if (INSTANCE == null && Game.getGame().isPresent()) {
					INSTANCE = new FixedVantage(
							  Game.getGame().get().getWindowWidth()
							, Game.getGame().get().getWindowHeight()
							);
				}
			}
		}
		return INSTANCE;
	}
	
	/**
	 * Default Constructor.
	 * @param pWidth - Screen Width.
	 * @param pHeight - Screen Height.
	 */
	private FixedVantage(int pWidth, int pHeight) {
		screenWidth = pWidth;
		screenHeight = pHeight;
		position = ORIGIN;
	}
	
	/**
	 * {@inheritDoc Vantage}
	 */
	@Override
	public void updatePosition(Vector2f target) {
		float newX = target.getX() - ((float) screenWidth / 2F);
		float newY = target.getY() - ((float) screenHeight / 2F);
		position = Vector2f.from(newX, newY);
	}
	
	/**
	 * {@inheritDoc Vantage}
	 */
	@Override
	public void setPosition(Vector2f target) {
		updatePosition(target);
	}
	
	/**
	 * {@inheritDoc Vantage}
	 */
	@Override
	public void update(long delta) {
		//TODO : Switch to Vector2f positioning and use Camera move command.
		moveCamera();
	}
	
	/**
	 * Private helper method to move the camera.
	 */
	private void moveCamera() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(
				  (double)position.getX()
				, (double)position.getX() + (double)screenWidth
				, (double)position.getY() + (double)screenHeight
				, (double)position.getY()
				, 2000
				, 2000 * -1
			);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}
