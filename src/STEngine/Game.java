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

package STEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

/**
 * This is our hook for launching the game, and also the home
 * of the main game loop. From here, we'll be running the frame
 * render operation, which will draw each sprite contained in the 
 * list of things to draw.
 * 
 * @author comet
 */
public class Game {
	public static boolean	gameRunning		= true;
	private static boolean	isApplication;
	private boolean 		fullscreen;
	private int 			width;
	private int 			height;
	private String			WINDOW_TITLE 	= "Stonetolb 0.1.0";
	Sprite 					test;
	
	/**
	 * Create the Game Object and initialize it
	 * 
	 * @param fullscreen
	 */
	public Game(boolean fullscreen) {
		this.fullscreen = fullscreen;
		initialize();
	}
	
	/**
	 * Begin Game main loop
	 */
	public void execute() {
		gameLoop();
	}
	
	/**
	 * Called at object creation to configure the screen, openGL,
	 * and create all environment items.
	 */
	private void initialize(){
		try {
			setDisplayMode();
			Display.setTitle(WINDOW_TITLE);
			Display.setFullscreen(fullscreen);
			Display.create();
			
			if(isApplication) {
				Mouse.setGrabbed(true);
			}
			
			setGLOptions();
		} catch (LWJGLException e) {
			System.out.println("Game exiting - exception in initialization:");
			e.printStackTrace();
			Game.gameRunning = false;
			return;
		}
		
		initEnviron();
	}
	
	private void gameLoop() {
		while (gameRunning) {
			Display.sync(60);
			
			//draw everything
			test.draw(20, 40);
			//do some logic
			
			//update the window
			Display.update();
		}
		
		//clean uparg0
		Display.destroy();
	}
	
	private boolean setDisplayMode(){
		try {
			// Get the display modes
			DisplayMode [] dm = org.lwjgl.util.Display.getAvailableDisplayModes(width, height, -1, -1, -1, -1, 60, 60);
			
			org.lwjgl.util.Display.setDisplayMode(dm
				, new String[] {"width=" + width, "height=" + height, "freq=" + 60, "bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()}
			);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to enter fullscreen, continuing in windowed mode");
		}
		return false;
	}
	
	private void setGLOptions() {
		//enable textures
		glEnable(GL_TEXTURE_2D);
		
		//disable depth test
		glDisable(GL_DEPTH_TEST);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(0,width,height,0,-1,1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glViewport(0,0,width,height);
	}
	
	private void initEnviron() {
		test = new Sprite("test.gif");
	}
	
	public static void main(String argv[]) {
		isApplication = true;
		System.out.println("Use -fullscreen for fullscreen mode");
		new Game((argv.length > 0 && "-fullscreen".equalsIgnoreCase(argv[0]))).execute();
		System.exit(0);
	}
}




























