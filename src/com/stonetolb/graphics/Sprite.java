package com.stonetolb.graphics;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.IOException;

import com.stonetolb.graphics.engine.TextureLoader;

/**
 * The Sprite object takes care of rendering a Texture object
 * as an OpenGL Quad to the screen.
 * 
 * @author james.baiera
 */
public class Sprite implements Drawable{
	
	/** The texture that stores the image for this sprite */
	private final Texture texture;

	/** The width in pixels of this sprite */
	private final int width;

	/** The height in pixels of this sprite */
	private final int height;
	
	/** The rendering mode for this drawable */
	private final ImageRenderMode renderMode;
	
	/** The height of the top two image quad points */
	private final float zDistance;
		
	/** All images stand at a 20 degree angle */
	private static double ANGLE = 20.0;
	
	/** Trigonometry was useful after all */
	private static double TANGENT = Math.tan(ANGLE);
	
	/**
	 * Create a new sprite from a specified image file path, as a flat sprite.
	 *
	 * @param pRef - A reference to the image on which this sprite should be based.
	 * @throws IOException In event the image cannot be loaded.
	 */
	public Sprite(String pRef) throws IOException{
		this(pRef, ImageRenderMode.FLAT);
	}

	/**
	 * Create a new sprite from a given {@linkplain Texture} object.
	 * 
	 * @param pTexture - A {@link Texture} object which becomes the sprite.
	 */
	public Sprite(Texture pTexture) {
		this(pTexture, ImageRenderMode.FLAT);
	}
	
	/**
	 * Create a new sprite from a specified image file path, with the provided
	 * {@link ImageRenderMode}.
	 *
	 * @param pRef - A reference to the image on which this sprite should be based.
	 * @param pRenderMode - mode to draw the image on the screen
	 */
	public Sprite(String pRef, ImageRenderMode pRenderMode) throws IOException{
		this(TextureLoader.getInstance().getTexture(pRef), pRenderMode);
	}
	
	/**
	 * Create a new sprite from a given texture, drawn in the specified 
	 * {@link ImageRenderMode}.
	 * 
	 * @param pTexture - A {@link Texture} object which becomes the sprite
	 * @param pRenderMode - Mode to draw the image on the screen.
	 */
	public Sprite(Texture pTexture, ImageRenderMode pRenderMode) {
		texture = pTexture;
		height = pTexture.getImageHeight();
		width = pTexture.getImageWidth();
		renderMode = pRenderMode;
		zDistance = (float)( ( (double)height ) * -TANGENT * renderMode.getModeMultiplier());
	}
	
	/**
	 * Get the width of this sprite in pixels.
	 *
	 * @return The width of this sprite in pixels.
	 */
	public int getWidth() {
		return texture.getImageWidth();
	}

	/**
	 * Get the height of this sprite in pixels.
	 *
	 * @return The height of this sprite in pixels.
	 */
	public int getHeight() {
		return texture.getImageHeight();
	}

	/**
	 * {@inheritDoc Drawable}
	 */
	@Override
	public void draw(int x, int y, int z, long delta) {
		
		// store the current model matrix
		glPushMatrix();

		// bind to the appropriate texture for this sprite
		texture.bind();

		// translate to the right location and prepare to draw
		glTranslatef(x, y, z);

		// draw a quad textured to match the sprite
		glBegin(GL_QUADS);
		{
			glTexCoord2f(texture.getXOrigin(), texture.getYOrigin());
			glVertex3f(0, 0, zDistance);

			glTexCoord2f(texture.getXOrigin(), texture.getHeight());
			glVertex3f(0, height, 0);

			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex3f(width, height, 0);

			glTexCoord2f(texture.getWidth(), texture.getYOrigin());
			glVertex3f(width, 0, zDistance);
		}
		glEnd();

		// restore the model view matrix to prevent contamination
		glPopMatrix();
	}
	
	/**
	 * {@inheritDoc Drawable}
	 */
	@Override
	public void accept(Critic critic) {
		critic.analyze(this);
	}
	
	@Override
	public String toString() {
		return "Sprite [texture=" + texture + ", width=" + width + ", height="
				+ height + ", renderMode=" + renderMode + ", zDistance="
				+ zDistance + "]";
	}

}