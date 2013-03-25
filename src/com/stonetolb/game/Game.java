package com.stonetolb.game;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.google.common.base.Optional;
import com.stonetolb.game.module.Module;
import com.stonetolb.render.util.Camera;
import com.stonetolb.render.util.FixedVantage;

/**
 * This is our hook for launching the game, and also the home
 * of the main game loop. From here, we'll be running the frame
 * render operation, which will draw each sprite contained in the 
 * list of things to draw.
 * 
 * @author james.baiera
 * 
 */
public class Game {
	private static long	TIMER_TICKS_PER_SECOND = Sys.getTimerResolution();
	private static Game INSTANCE;
	
	private String windowTitle;
	private int	windowWidth;
	private int	windowHeight;

	private long lastLoopTime;
	private long lastFpsTime;
	private int	fps;
	
	private boolean gameRunning;
	private boolean	fullscreen;

	private Module module;
	
	/**
	 * Creates the Game object and returns it.
	 * 
	 * @param pWindowTitle - Title to display on the window header.
	 * @param pWindowWidth - Width of the game window.
	 * @param pWindowHeight - Height of the game window.
	 * @param pModuleToRun - Module to load and run at start up.
	 * @param fullscreen - Option to set fullscreen on or off.
	 * @return Game instance
	 * @throws GeneralGameException On failure to load Module for any reason.
	 */
	public static Game createGame(String pWindowTitle, int pWindowWidth, int pWindowHeight, String pModuleToRun, boolean fullscreen)
	throws GeneralGameException
	{
		if (INSTANCE == null) {
			INSTANCE = new Game(pWindowTitle, pWindowWidth, pWindowHeight, pModuleToRun, fullscreen);
		}
		return INSTANCE;
	}
	
	/**
	 * Returns the singleton game object if it has been created. Null if not yet created.
	 * @return Optional of type Game. 
	 */
	public static Optional<Game> getGame() {
		return Optional.fromNullable(INSTANCE);
	}
	
	/**
	 * Construct our game and set it running.
	 * @param pWindowTitle - Title of the Game Window
	 * @param pWindowWidth - Game window width
	 * @param pWindowHeight - Game window height
	 * @param pModuleToRun - Game Module to run at start up
	 * @param pFullscreen - Option to set fullscreen on or off
	 * @throws GeneralGameException On failure to load given module
	 */
	private Game(
			  String pWindowTitle
			, int pWindowWidth
			, int pWindowHeight
			, String pModuleToRun
			, boolean pFullscreen)
	throws GeneralGameException
	{
		windowTitle = pWindowTitle;
		windowWidth = pWindowWidth;
		windowHeight = pWindowHeight;
		fullscreen = pFullscreen;
		gameRunning = true;
		
		// Dynamically load a module by name
		System.out.println("Loading Game Module...");
		try {
			Class<?> clazz = Class.forName(pModuleToRun);
			module = (Module) clazz.newInstance();
			
			System.out.println("Module Loaded : " + module.toString());
		} catch(ClassNotFoundException cnfe) {
			throw new GeneralGameException("Could not find module : " + pModuleToRun, cnfe);
		} catch (InstantiationException ie) {
			throw new GeneralGameException("Could not instantiate module : " + pModuleToRun, ie);
		} catch (IllegalAccessException iae) {
			throw new GeneralGameException("Could not access module : " + pModuleToRun, iae);
		}
	}

	/**
	 * Initializes game, runs game, then cleans up screen on game completion
	 * @throws GeneralGameException On irrecoverable game failure.
	 */
	public void execute() 
	throws GeneralGameException
	{
		try {
			initialize();
			gameLoop();
			Display.destroy();
		} catch (Exception e) {
			throw new GeneralGameException("Error occurred in game", e);
		}
	}
	
	/**
	 * Initializes and runs the main game loop. Continues to run logic each 
	 * frame until game ends.
	 */
	private void gameLoop() {
		while (gameRunning) {
			// run game logic for this frame
			gameLogic();

			// update window contents
			Display.update();
		}
	}

	/**
	 * Initialize the common elements for the game
	 * @throws GeneralGameException On failure to set display mode or create display.
	 */
	private void initialize()
	throws GeneralGameException
	{
		try {
			setDisplayMode();
			Display.setTitle(windowTitle);
			Display.create();
			
			// Create the Camera
			Camera.setVantage(FixedVantage.create());
			
			// Set starting time for game loop
			lastLoopTime = getTime();
			
		} catch (LWJGLException le) {
			gameRunning = false;
			throw new GeneralGameException("Could not create display object", le);
		}

		// setup the initial game state
		module.init();
	}

	/**
	 * Notification that a frame is being rendered. Responsible for
	 * running game logic and rendering the scene.
	 */
	private void gameLogic() {
		// Sync Display to 60 fps
		Display.sync(60);

		// Calculate time since last loop
		long delta = getTime() - lastLoopTime;
		lastLoopTime = getTime();
		lastFpsTime += delta;
		fps++;

		// update our FPS counter if one second has passed
		if (lastFpsTime >= 1000) {
			Display.setTitle(windowTitle + " (FPS: " + fps + ")");
			lastFpsTime = 0;
			fps = 0;
		}

		// Allow Game Logic
		module.step(delta);
		
		// render the game frame
		module.render(delta);
		
		// Update Camera's position
		Camera.getInstance().update(delta);
		
		// if escape has been pressed, stop the game
		if ((Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))) {
			gameRunning = false;
		}
	}

	/**
	* Sets the display mode
	* 
	* @throws GeneralGameException On failure to set window display mode.
	*/
	private boolean setDisplayMode()
	throws GeneralGameException
	{	
		DisplayMode dm;
		
		try {
			//----------------------------------------------------------------------
			// TEST : Getting display modes
			// TODO : REVISIT LATER
//			DisplayMode[] displayModes = org.lwjgl.opengl.Display.getAvailableDisplayModes();
//			
//			int i = 0;
//			for(DisplayMode displayMode : displayModes) {
//				System.out.println(i++ + ": " + displayMode.toString() + " F:" + displayMode.isFullscreenCapable());
//			}
			// END TEST
			//----------------------------------------------------------------------
			
			// Set display mode for the window
			dm = new DisplayMode(windowWidth,windowHeight);
			System.out.println("Using Display Mode  : " + dm.toString());
			System.out.println("Full Screen Capable : " + dm.isFullscreenCapable());
	    	Display.setDisplayMode(dm);
		} 
		catch (LWJGLException lwjgle) {
			throw new GeneralGameException("Could not set display mode on LWJGL Display.", lwjgle);
		}
		
		try {
			//If full screen, set's full screen
			if (fullscreen && dm.isFullscreenCapable())
			{
				Display.setFullscreen(fullscreen);
			}
				
			return true;
		} 
		catch (LWJGLException lwjgle) {
			System.out.println("Unable to enter fullscreen : " + lwjgle.getMessage());
			System.out.println("Continuing in windowed mode");
		}
		
		return false;
	}
	
	/**
	 * Get the high resolution time in milliseconds
	 *
	 * @return The high resolution time in milliseconds
	 */
	public static long getTime() {
		// we get the "timer ticks" from the high resolution timer
		// multiply by 1000 so our end result is in milliseconds
		// then divide by the number of ticks in a second giving
		// us a nice clear time in milliseconds
		return (Sys.getTime() * 1000) / TIMER_TICKS_PER_SECOND;
	}
	
	/**
	 * Returns the set window width
	 * @return Game display width.
	 */
	public int getWindowWidth() {
		return windowWidth;
	}
	
	/**
	 * Returns the set window height
	 * @return Game display height.
	 */
	public int getWindowHeight() {
		return windowHeight;
	}
}