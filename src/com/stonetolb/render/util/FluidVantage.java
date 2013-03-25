package com.stonetolb.render.util;

import com.stonetolb.game.Game;
import com.stonetolb.util.Vector2f;

/**
 * FluidVantage object used to create a ease-in-ease-out orthogonal camera movement.
 * 
 * Causes objects on the screen to jitter during short distance updates due to float based 
 * math. When used in integer mode, this creates a small bounding space that the Camera will 
 * simply not move in. Further analysis is required to make this production worthy.
 * 
 * @author james.baiera
 *
 */
public final class FluidVantage implements Vantage {

	private static volatile FluidVantage INSTANCE = null;
	
	private int screenWidth;
	private int screenHeight;
	
	private Vector2f currentPosition;
	private Vector2f targetPosition;
	private float normalSpeed;

	/**
	 * Creates (or recreates) the single instance of FluidVantage.
	 * @param speed - range between 0 and 1 for fading factor.
	 * <br>0 = Fixed at "current" position
	 * <br>1 = Fixed at "target" position
	 * @return The FluidVantage instance object.
	 */
	public static FluidVantage create(float speed) {
		if(INSTANCE == null) {
			synchronized(FluidVantage.class) {
				if(INSTANCE == null && Game.getGame().isPresent()) {
					INSTANCE = new FluidVantage(
							  Game.getGame().get().getWindowWidth()
							, Game.getGame().get().getWindowHeight()
							, speed
							);
				}
			}
		}
		return INSTANCE;
	}
	
	/**
	 * Default Constructor.
	 * @param pScreenW - Screen Width.
	 * @param pScreenH - Screen Height.
	 * @param pSpeed - Camera speed.
	 */
	private FluidVantage(int pScreenW, int pScreenH, float pSpeed) {
		screenWidth = pScreenW;
		screenHeight = pScreenH;
		normalSpeed = pSpeed;
		
		currentPosition = Vector2f.NULL_VECTOR;
		targetPosition = Vector2f.NULL_VECTOR;
	}
	
	/**
	 * {@inheritDoc Vantage}
	 */
	@Override
	public void updatePosition(Vector2f target) {
		if(target != null) {
			targetPosition = Vector2f.from(
					  target.getX() - ((float) screenWidth / 2F)
					, target.getY() - ((float) screenHeight / 2F)
					);
		}
	}
	
	/**
	 * {@inheritDoc Vantage}
	 */
	@Override
	public void setPosition(Vector2f newPosition) {
		currentPosition = Vector2f.from(
				  newPosition.getX() - ((float) screenWidth / 2F)
				, newPosition.getY() - ((float) screenHeight / 2F)
				);
		targetPosition = currentPosition;
	}

	/**
	 *{@inheritDoc Vantage}
	 */
	@Override
	public void update(long delta) {
		currentPosition = Vector2f.from(
				  Math.round(fadeOut(currentPosition.getX(), targetPosition.getX(), normalSpeed))
				, Math.round(fadeOut(currentPosition.getY(), targetPosition.getY(), normalSpeed))
				);
		
//		System.out.println(currentPosition);
		Camera.moveCamera(currentPosition, screenWidth, screenHeight);
	}

	/**
	 * Movement function used in calculating the Camera's actual position.
	 * @param current - Current location.
	 * @param target - Target location.
	 * @param ratio - Speed to move.
	 * @return new current location.
	 */
	private float fadeOut(float current, float target, float ratio) {
		return (target * (ratio)) + ((1 - ratio) * current);
	}
}
