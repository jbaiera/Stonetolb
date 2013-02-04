package com.stonetolb.game.module;

/**
 * Interface that defines how the game state objects should act during the game loop
 * 
 * @author james.baiera
 *
 */
public interface Module {
	
	/**
	 * Runs this method at the start of execution
	 */
	public abstract void init();
	
	/**
	 * One step of game logic
	 * 
	 * @param delta amount of time passed since last step
	 */
	public abstract void step(long delta);
	
	/**
	 * Render all entities
	 * 
	 * @param delta amount of time passed since last render
	 */
	public abstract void render(long delta);
}
