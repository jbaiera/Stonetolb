package com.stonetolb.render;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.IOException;

import com.stonetolb.asset.graphics.Texture;
import com.stonetolb.asset.graphics.TextureLoader;
import com.stonetolb.resource.system.SystemContext;

/**
 * The Sprite object takes care of rendering a Texture object
 * as an OpenGL Quad to the screen.
 * 
 * @author james.baiera
 */
public class Sprite implements Drawable{
	
	/** The texture that stores the image for this sprite */
	private final Texture texture;

	/** System Object used to make calls to graphics api */
	private final SystemContext system;

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
		system = texture.getSystem();
		height = pTexture.getImageHeight();
		width = pTexture.getImageWidth();
		renderMode = pRenderMode;
		zDistance = (float)( ( (double)height ) * -TANGENT * renderMode.getModeMultiplier());
	}

	/**
	 * {@inheritDoc Drawable}
	 */
	@Override
	public void draw(int x, int y, int z, long delta) {
		
		// store the current model matrix
		system.pushMatrix();

		// bind to the appropriate texture for this sprite
		texture.bind();

		// translate to the right location and prepare to draw
		system.translate(x, y, z);

		// draw a quad textured to match the sprite
		system.penDown(GL_QUADS);
		{
			system.addTextureCoordinate(texture.getXOrigin(), texture.getYOrigin());
			system.addVertex3f(0, 0, zDistance);

			system.addTextureCoordinate(texture.getXOrigin(), texture.getHeight());
			system.addVertex3f(0, height, 0);

			system.addTextureCoordinate(texture.getWidth(), texture.getHeight());
			system.addVertex3f(width, height, 0);

			system.addTextureCoordinate(texture.getWidth(), texture.getYOrigin());
			system.addVertex3f(width, 0, zDistance);
		}
		system.penUp();

		// restore the model view matrix to prevent contamination
		system.popMatrix();
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