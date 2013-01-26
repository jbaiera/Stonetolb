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

import com.stonetolb.util.IntervalQueue;
import com.stonetolb.util.IntervalQueue.IntervalQueueBuilder;

/**
 * Animation is a StatefulDrawable object that represents a list of Sprites that
 * are displayed in quick succession to simulate motion. Animations are backed by 
 * an Immutable Interval Queue to store the images and their lifetimes. Animations
 * contain states to keep track of frame display times as well as actively rendering
 * objects. It is a poor idea to reuse the same Animation across multiple objects.
 * Animation is a thin object compared to it's immutable backing, so it is often wise
 * to run clone the Animation should you want to reuse the animation frame set.
 * 
 * @author james.baiera
 * 
 */
public class Animation implements StatefulDrawable{
	
	/**
	 * Builder object used to construct Animation Objects. This object 
	 * leverages an {@link IntervalQueueBuilder} to construct the
	 * underlying {@link IntervalQueue} that the Animation object uses.
	 * Subsequent calls to this builder after an object has been created
	 * will not affect any Animations previously built by this object.
	 * 
	 * @author james.baiera
	 *
	 */
	public static class AnimationBuilder {
		private IntervalQueueBuilder<Drawable> frameListBuilder;

		/**
		 * Private constructor, called in static method of Animation.
		 */
		private AnimationBuilder() {
			frameListBuilder = IntervalQueue.builder();
		}
		
		/**
		 * Adds a single frame to the animation. All frames are equally spaced.
		 * 
		 * @param pImage
		 * @return this AnimationBuilder 
		 */
		public AnimationBuilder addFrame(Drawable pImage, int pDuration) {
			return addFrame(new Keyframe(pImage, pDuration));
		}
		
		public AnimationBuilder addFrame(Keyframe pFrame) {
			frameListBuilder.append(pFrame, pFrame.getDuration());
			return this;
		}
		
		/**
		 * Returns a new Animation object based off of values previously entered into builder.
		 * 
		 * @return New Animation Object
		 */
		public Animation build() {
			//Build result
			return new Animation(frameListBuilder.build());
		}
	}
	
	/**
	 * Internal class for {@link Animation}. Used to couple frames with their duration.
	 * @author james.baiera
	 *
	 */
	public static class Keyframe implements Drawable{
		private Drawable image;
		private int duration;
		
		/**
		 * Default Keyframe Constructor
		 * 
		 * @param pImage Image to draw for the frame
		 * @param pDuration Number of milliseconds to display image
		 */
		public Keyframe(Drawable pImage, int pDuration) {
			image = pImage;
			duration = pDuration;
		}
		
		public Drawable getImage() {
			return image;
		}
		
		public int getDuration() {
			return duration;
		}

		@Override
		public void draw(int x, int y, int z, long delta) {
			image.draw(x, y, z, delta);
		}
		
		@Override
		public void accept(Critic critic) {
			critic.analyze(this);
		}
	}
	
	private int actualInterval;
	private int stepCount;
	private boolean running;
	protected IntervalQueue<Drawable> frameList;
	
	/**
	 * Creates a new {@link AnimationBuilder} and returns it
	 * 
	 * @return A new {@link AnimationBuilder} object
	 */
	public static AnimationBuilder builder() {
		return new AnimationBuilder();
	}
	
	/**
	 * Creates a new Animation object with the same immutable backing 
	 * as the original.
	 * 
	 * @return new Animation object with the same list of frames
	 */
	public Animation clone() {
		return new Animation(frameList);
	}
	
	private Animation(IntervalQueue<Drawable> pFrameList) {
		actualInterval = pFrameList.getQueueLength();
		stepCount = 0;
		frameList = pFrameList;
		running = false;
	}

	@Override
	public void ready() {
		if(!running) {
			stepCount = 0;
			running = true;
		}
	}
	
	@Override
	public void dispose() {
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
			
			// Find the drawable that will be at this point in time
			// and draw it
			Drawable toBeDrawn = frameList.getDataAt(stepCount);
			toBeDrawn = toBeDrawn == null ? NullDrawable.getInstance() : toBeDrawn; //null check
			
			toBeDrawn.draw(x, y, z, delta);
		}
	}

	@Override
	public void accept(Critic critic) {
		critic.analyze(this);
	}
}