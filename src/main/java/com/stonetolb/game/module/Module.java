package com.stonetolb.game.module;

/**
 * A Module is a primary game state object that contains specific
 * logic on what to accomplish during the game loop.
 * 
 * @author james.baiera
 *
 */
public interface Module {
	
	/**
	 * Method call used to initialize a {@link Module} object.
	 */
	public void init();
	
	/**
	 * Method invoked to process a single 'tick' of game-time.
	 * 
	 * @param delta - Amount of time passed since last step
	 */
	public void step(long delta);
	
	/**
	 * Method invoked to render all objects onto the screen.
	 * 
	 * @param delta - Amount of time passed since last render
	 */
	public void render(long delta);
}
