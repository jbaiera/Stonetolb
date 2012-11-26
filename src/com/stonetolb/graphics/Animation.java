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

package com.stonetolb.graphics;


import java.util.ArrayList;
import java.util.List;


/**
 * Animation is a glorified list of Sprite objects, which,
 * based on a predetermined interval, changes which Sprite
 * is currently active. 
 * <p>
 * The interval the user specifies is
 * more or less just a suggestion, and the actual interval 
 * is the closest multiple of the list size to the stated 
 * interval. 
 * <p>
 * This object is handled just like a Sprite
 * in terms of drawing commands except you pass in the time since
 * the last frame.
 * 
 * @author comet
 */
public class Animation implements StatefulDrawable{
	
	/**
	 * Helper class used to construct animations from multiple procedure calls.
	 * Builder reset's state after each build, meaning that subsequent calls to building
	 * methods will not affect previously built objects.
	 * 
	 * @author james.baiera
	 *
	 */
	public static class AnimationBuilder {
		private List<Drawable> sprites;
		private int interval;
		
		private AnimationBuilder() {
			initBuilder();
		}
		
		private void initBuilder() {
			sprites = new ArrayList<Drawable>();
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
		public AnimationBuilder addFrame(Drawable pFrame) {
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
	
	// interval is an approxamation right now, since frames are equidistant in time from each other
	// This should be fixed later.
	
	private int interval;
	private int actualInterval;
	private int stepCount;
	private boolean running;
	protected List<Drawable> spriteList;
	
	private static int BASE_MILLISECONDS_PER_FRAME = 500; //Animation would last half a second
	
	public static AnimationBuilder builder() {
		return new AnimationBuilder();
	}
	
	private Animation(int pInterval) {
		interval = pInterval;
		actualInterval = pInterval;
		stepCount = 0;
		running = false;
	}
	
//	/**
//	 * Add a new Sprite to the end of the sprite list. This
//	 * updates the actual interval.
//	 * 
//	 * @param newSprite A sprite to add to the frame set
//	 */
//	private void addFrame(Sprite newSprite) {
//		spriteList.add(newSprite);
//		updateActual();
//	}
	
	private void start() {
		if(!running) {
			stepCount = 0;
			running = true;
		}
	}
	
	private void stop() {
		running = false;
	}

	@Override
	public void ready() {
		start();
	}
	
	@Override
	public void dispose() {
		stop();
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
	 * 
	 * TODO : Get away from using equal spaced keyframes by default.
	 * There should be a set of keyframe items which are frames associated
	 * with how long they should be displayed to the screen.
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