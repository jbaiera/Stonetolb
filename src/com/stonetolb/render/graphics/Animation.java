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

package com.stonetolb.render.graphics;


import java.util.ArrayList;
import java.util.List;


/**
 * Animation is a glorified list of Sprite objects, which,
 * based on a predetermined interval, changes which Sprite
 * is currently active. <br><br>The interval the user specifies is
 * more or less just a suggestion, and the actual interval 
 * is the closest multiple of the list size to the stated 
 * interval. <br><br>This object is handled just like a Sprite
 * in terms of drawing commands except you pass in the time since
 * the last frame.
 * 
 * @author comet
 */
public class Animation implements Drawable{
	
	/**
	 * Helper class used to construct animations from multiple procedure calls.
	 * Builder reset's state after each build, meaning that subsequent calls to building
	 * methods will not affect previously built objects.
	 * 
	 * @author james.baiera
	 *
	 */
	public class AnimationBuilder {
		private List<Sprite> sprites;
		private int interval;
		
		private AnimationBuilder() {
			initBuilder();
		}
		
		private void initBuilder() {
			sprites = new ArrayList<Sprite>();
			interval = Animation.BASE_MILLISECONDS_PER_FRAME;
		}
		
		/**
		 * Sets Animation play time in Milliseconds.
		 * 
		 * @param pInterval
		 * @return this AnimationBuilder
		 */
		public AnimationBuilder setInterval(int pInterval) {
			interval = pInterval;
			return this;
		}
		
		/**
		 * Adds a single frame to the animation. All frames are equally spaced.
		 * 
		 * @param pFrame
		 * @return this AnimationBuilder 
		 */
		public AnimationBuilder addFrame(Sprite pFrame) {
			sprites.add(pFrame);
			return this;
		}
		
		/**
		 * Returns a new Animation object based off of values previously entered into builder.
		 * 
		 * @return New Animation Object
		 */
		public Animation build() {
			//Build result
			Animation returnValue = new Animation(interval);
			returnValue.spriteList = sprites;
			returnValue.updateActual();
			
			//reset builder
			initBuilder();
			
			//return
			return returnValue;
		}
	}
	
	//approximate milliseconds until animation loops
	private int interval;
	//actual milliseconds until animation loops
	//based on number of sprites and interval amt
	private int actualInterval;
	//number of milliseconds since animation start
	private int stepCount;
	//keep track of whether or not the animation is running
	private boolean running;
	//list of sprites used in the animation, in order of playing
	protected List<Sprite> spriteList;
	
	private static int BASE_MILLISECONDS_PER_FRAME = 500; //Animation would last half a second
	
	
	public Animation(int interval) {
		this.interval = interval;
		this.actualInterval = interval;
		this.stepCount = 0;
		this.running = false;
		this.spriteList = new ArrayList<Sprite>();
	}
	
	/**
	 * Add a new Sprite to the end of the sprite list. This
	 * updates the actual interval.
	 * 
	 * @param newSprite A sprite to add to the frame set
	 */
	public void addFrame(Sprite newSprite) {
		spriteList.add(newSprite);
		updateActual();
	}
	
	/**
	 * Set's the Animation back to the starting frame.
	 * This keeps the Animation from starting in the middle
	 * of it's frames because of how long ago it might have 
	 * last been drawn.
	 */
	public void start() {
		if(!running) {
			stepCount = 0;
			running = true;
		}
	}
	
	/**
	 * Stops the Animation playback so that it can be restarted
	 * at a later time from the beginning
	 */
	public void stop() {
		running = false;
	}
	
	/**
	 * Draw the animation at the x and y coordinate, displaying
	 * the sprite from the frame in the animation based on the 
	 * given time elapsed since the start of the tween.
	 * 
	 * @param x X coordinate at which to draw the animation
	 * @param y Y coordinate at which to draw the animation
	 * @param delta number of milliseconds passed since last frame
	 * drawing
	 */
	@Override
	public void draw(int x, int y, int z, long delta) {
		if(running) {
			// add to step count, but keep it below actualInterval
			stepCount = ((int)((long)stepCount + delta)) % actualInterval;
			// create an increment amount
			int inc = (int) (actualInterval / spriteList.size());
			// figure out which index to display
			int idx = (int) (stepCount / inc);
			// draw the needed sprite at the area specified
			spriteList.get(idx).draw(x, y, z, delta);
		}
	}
	
	/**
	 * Finds the greatest common factor between the interval
	 * and the number of items in the animation list. Sets the
	 * actual interval of the Animation
	 */
	private void updateActual() {
		int lstSize = spriteList.size();
		if (lstSize > 0) {
			actualInterval = ((int) (interval / lstSize)) * lstSize;
		} else {
			actualInterval = interval;
		}
	}
}