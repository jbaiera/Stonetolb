package com.stonetolb.render;

import com.stonetolb.Game;
import com.stonetolb.util.Vector2f;

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
				if(INSTANCE == null) {
					INSTANCE = new FluidVantage(
							  Game.getGame().getWindowWidth()
							, Game.getGame().getWindowHeight()
							, speed
							);
				}
			}
		}
		return INSTANCE;
	}
	
	
	private FluidVantage(int pScreenW, int pScreenH, float pSpeed) {
		screenWidth = pScreenW;
		screenHeight = pScreenH;
		normalSpeed = pSpeed;
		
		currentPosition = Vector2f.NULL_VECTOR;
		targetPosition = Vector2f.NULL_VECTOR;
	}
	
	@Override
	public void updatePosition(Vector2f target) {
		if(target != null) {
			targetPosition = Vector2f.from(
					  target.getX() - ((float) screenWidth / 2F)
					, target.getY() - ((float) screenHeight / 2F)
					);
		}
	}
	
	@Override
	public void setPosition(Vector2f newPosition) {
		currentPosition = Vector2f.from(
				  newPosition.getX() - ((float) screenWidth / 2F)
				, newPosition.getY() - ((float) screenHeight / 2F)
				);
		targetPosition = currentPosition;
	}

	@Override
	public void update(long delta) {
		currentPosition = Vector2f.from(
				  fadeOut(currentPosition.getX(), targetPosition.getX(), normalSpeed)
				, fadeOut(currentPosition.getY(), targetPosition.getY(), normalSpeed)
				);
		
//		System.out.println(currentPosition);
		Camera.moveCamera(currentPosition, screenWidth, screenHeight);
	}

	private float fadeOut(float current, float target, float ratio) {
		return (target * (ratio)) + ((1 - ratio) * current);
	}
}
