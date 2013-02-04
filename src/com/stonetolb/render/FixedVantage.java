package com.stonetolb.render;

import org.lwjgl.opengl.GL11;

import com.stonetolb.game.Game;
import com.stonetolb.util.Pair;
import com.stonetolb.util.Vector2f;

/**
 * FixedVantage object used to represent an orthogonal camera always at the given target location.
 * 
 * @author james.baiera
 *
 */
public final class FixedVantage implements Vantage{
	
	private static volatile FixedVantage INSTANCE = null;
	private static Pair<Float, Float> ORIGIN = new Pair<Float, Float>(0F, 0F);
	
	private Pair<Float, Float> position;
	private int screenWidth;
	private int screenHeight;
	
	/**
	 * Creates and returns the singleton instance of the FixedVantage object.
	 * @return FixedVantage instance.
	 */
	public static FixedVantage create() {
		if (INSTANCE == null) {
			synchronized(FixedVantage.class) {
				if (INSTANCE == null) {
					INSTANCE = new FixedVantage(
							  Game.getGame().getWindowWidth()
							, Game.getGame().getWindowHeight()
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
	
	@Override
	public void updatePosition(Vector2f target) {
		position.x = target.getX() - ((float) screenWidth / 2F);
		position.y = target.getY() - ((float) screenHeight / 2F);
	}
	
	@Override
	public void setPosition(Vector2f target) {
		updatePosition(target);
	}
	
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
				  position.x.doubleValue()
				, position.x.doubleValue() + (double)screenWidth
				, position.y.doubleValue() + (double)screenHeight
				, position.y.doubleValue()
				, 2000
				, 2000 * -1
			);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}
