package com.stonetolb.render;

import com.stonetolb.util.IntervalQueue;

/**
 * Animation is a {@link StatefulDrawable} object that represents a list of 
 * {@link Sprite} that are displayed in quick succession to simulate motion.
 * <p>
 * Animations are backed by an Immutable {@link IntervalQueue} to store the 
 * images and their lifetimes. An Animation contains state data to keep track 
 * of frame display times as well as the currently active rendering objects.
 * <p>
 * It is a poor idea to reuse the same Animation across multiple objects. 
 * While the Animation itself is not thread safe, the list of frames that
 * it contains is. Furthermore, It is relatively cheap to retrieve a clone of
 * an Animation object in order to reuse it's underlying frame set. This 
 * should be the preferred course of action instead of sharing Animations.
 * 
 * @author james.baiera
 * 
 */
public class Animation implements StatefulDrawable {
	
	/**
	 * Builder object used to construct Animation Objects. This object 
	 * leverages an {@link IntervalQueue.Builder} to construct the
	 * underlying {@link IntervalQueue} that the Animation object uses.
	 * Subsequent calls to this builder after an object has been created
	 * will not affect any Animations previously built by this object.
	 * 
	 * @author james.baiera
	 *
	 */
	public static class Builder {
		private IntervalQueue.Builder<Drawable> frameListBuilder;

		/**
		 * Private constructor, called in static method of Animation.
		 */
		private Builder() {
			frameListBuilder = IntervalQueue.builder();
		}
		
		/**
		 * Adds a single frame to the animation.
		 * 
		 * @param pImage - Image to add
		 * @param pDuration - Time in milliseconds to display in the Animation
		 * @return this {@link Builder} 
		 */
		public Builder addFrame(Drawable pImage, int pDuration) {
			return addFrame(new KeyFrame(pImage, pDuration));
		}
		
		/**
		 * Adds a single {@link KeyFrame} to the animation.
		 * 
		 * @param pFrame - {@link KeyFrame} to add
		 * @return this {@link Builder}
		 */
		public Builder addFrame(KeyFrame pFrame) {
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
	public static class KeyFrame implements Drawable{
		private Drawable image;
		private int duration;
		
		/**
		 * Default {@link KeyFrame} Constructor
		 * 
		 * @param pImage - {@link Drawable} to render for the frame
		 * @param pDuration - Number of milliseconds to display image
		 */
		public KeyFrame(Drawable pImage, int pDuration) {
			image = pImage;
			duration = pDuration;
		}
		
		/**
		 * Returns {@link Drawable} for {@link KeyFrame}.
		 * @return {@link Drawable} contained in this {@link KeyFrame}.
		 */
		public Drawable getImage() {
			return image;
		}
		
		/**
		 * Returns duration to display the {@link KeyFrame}.
		 * @return length of time in milliseconds to display {@link KeyFrame}.
		 */
		public int getDuration() {
			return duration;
		}

		/**
		 * {@inheritDoc Drawable}
		 */
		@Override
		public void draw(int x, int y, int z, long delta) {
			image.draw(x, y, z, delta);
		}
		
		/**
		 * {@inheritDoc Drawable}
		 */
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
	 * Creates a new {@link Builder} and returns it
	 * 
	 * @return A new {@link Builder} object
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Creates a new Animation object with the same frame list as
	 * this.
	 * 
	 * @return new Animation object with the same list of frames
	 */
	@Override
	public Animation clone() {
		return new Animation(frameList);
	}
	
	/**
	 * Default Constructor
	 * @param pFrameList - {@link IntervalQueue} of the frames for this Animation.
	 */
	private Animation(IntervalQueue<Drawable> pFrameList) {
		actualInterval = pFrameList.getQueueLength();
		stepCount = 0;
		frameList = pFrameList;
		running = false;
	}

	/**
	 * {@inheritDoc StatefulDrawable}
	 */
	@Override
	public void ready() {
		if(!running) {
			stepCount = 0;
			running = true;
		}
	}

	/**
	 * {@inheritDoc StatefulDrawable}
	 */
	@Override
	public void dispose() {
		running = false;
	}
	
	/**
	 * Draw the animation at the x and y coordinate, displaying
	 * the sprite from the frame in the animation based on the 
	 * given time elapsed since the start of the Animation.
	 * 
	 * @param x - X coordinate at which to draw the animation
	 * @param y - Y coordinate at which to draw the animation
	 * @param delta - number of milliseconds passed since last frame
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

	/**
	 * {@inheritDoc Drawable}
	 */
	@Override
	public void accept(Critic critic) {
		critic.analyze(this);
	}
}