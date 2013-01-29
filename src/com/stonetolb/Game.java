/* 
 * Copyleft (o) 2012 James Baiera
 * All wrongs reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.stonetolb;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.stonetolb.game.module.Module;
import com.stonetolb.render.Camera;
import com.stonetolb.render.FixedVantage;

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
	 * @param pWindowTitle
	 * @param pWindowWidth
	 * @param pWindoHeight
	 * @param pModuleToRun
	 * @param fullscreen
	 * @return
	 */
	public static Game createGame(String pWindowTitle, int pWindowWidth, int pWindowHeight, String pModuleToRun, boolean fullscreen)
	{
		if (INSTANCE == null) {
			INSTANCE = new Game(pWindowTitle, pWindowWidth, pWindowHeight, pModuleToRun, fullscreen);
		}
		return INSTANCE;
	}
	
	/**
	 * Returns the singleton game object if it has been created. Null if not yet created.
	 * @return
	 */
	public static Game getGame() {
		return INSTANCE;
	}
	
	/**
	 * Construct our game and set it running.
	 * @param pWindowWidth
	 * @param pWindowHeight
	 * @param pModuleToRun
	 * @param fullscreen
	 */
	private Game(
			  String pWindowTitle
			, int pWindowWidth
			, int pWindowHeight
			, String pModuleToRun
			, boolean fullscreen) 
	{
		this.windowTitle = pWindowTitle;
		this.windowWidth = pWindowWidth;
		this.windowHeight = pWindowHeight;
		this.fullscreen = fullscreen;
		this.gameRunning = true;
		
		// Dynamically load a module by name
		System.out.println("Loading Game Module...");
		try {
			Class<?> clazz = Class.forName(pModuleToRun);
			module = (Module) clazz.newInstance();
			
			System.out.println("Module Loaded : " + module.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (module == null) {
				System.out.println("Failed to load module : " + pModuleToRun);
				System.exit(1);
			}
		}
	}

	/**
	 * Initializes game, runs game, then cleans up screen on game completion
	 */
	public void execute() {
		initialize();
		gameLoop();
		Display.destroy();
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
	 */
	private void initialize() {
		try {
			setDisplayMode();
			Display.setTitle(windowTitle);
			Display.create();
			
			// Create the Camera
			Camera.setVantage(FixedVantage.create());
			
			// Set starting time for game loop
			lastLoopTime = getTime();
			
		} catch (LWJGLException le) {
			System.out.println("Game exiting - exception in initialization:");
			le.printStackTrace();
			gameRunning = false;
			return;
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
	*/
	private boolean setDisplayMode() {
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
			DisplayMode dm = new DisplayMode(windowWidth,windowHeight);
			System.out.println("Using Display Mode  : " + dm.toString());
			System.out.println("Full Screen Capable : " + dm.isFullscreenCapable());
	    	Display.setDisplayMode(dm);
			
			//If full screen, set's full screen
			if (fullscreen && dm.isFullscreenCapable())
			{
				Display.setFullscreen(fullscreen);
			}
				
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to enter fullscreen, continuing in windowed mode");
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
	 * @return
	 */
	public int getWindowWidth() {
		return windowWidth;
	}
	
	/**
	 * Returns the set window height
	 * @return
	 */
	public int getWindowHeight() {
		return windowHeight;
	}
}