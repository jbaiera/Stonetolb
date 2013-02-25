package com.stonetolb.graphics;

/**
 * Designates the Rendering Style of an image.
 * <p>
 * <li>FLAT - Indicating that an image will display with all
 * four corners of it's quad at zero depth.
 * <li>STANDING - Indicating that an image will display with
 * the top two corners lifted towards the camera at a 45 degree
 * angle, allowing it to 'stand' in front of other images.
 * 
 * @author james.baiera
 */
public enum ImageRenderMode {
	FLAT(0),
	STANDING(1);
	
	private int multiplier;
	
	/**
	 * Default Constructor
	 * @param pMultiplier - value of the "standing" factor of a sprite
	 */
	private ImageRenderMode(int pMultiplier) {
		multiplier = pMultiplier;
	}
	
	/**
	 * Returns multiplier to modify the height of the top of a Sprite object.
	 * Multiply the sprite's 'vertical factor' by this number to either include
	 * it (x1) or exclude it (x0). 
	 * @return multiplier
	 */
	public int getModeMultiplier() {
		return multiplier;
	}
}
