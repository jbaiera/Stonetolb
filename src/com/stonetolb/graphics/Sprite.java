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
 * Sprite object that uses an OpenGL quad and a Texture
 * to render a given image to the screen.
 * 
 * @author james.baiera
 */
public class Sprite implements Drawable{
	
	/** The texture that stores the image for this sprite */
	private Texture	texture;

	/** The width in pixels of this sprite */
	private int	width;

	/** The height in pixels of this sprite */
	private int	height;
	
	/** The rendering mode for this drawable */
	private ImageRenderMode renderMode;
	
	private float zDistance;
		
	private static double ANGLE = 20.0;
	private static double TANGENT = Math.tan(ANGLE);
	
	/**
	 * Create a new sprite from a specified image file path.
	 *
	 * @param pRef A reference to the image on which this sprite should be based
	 */
	public Sprite(String pRef) throws IOException{
		this(pRef, ImageRenderMode.FLAT);
	}

	/**
	 * Create a new sprite from a given texture
	 * 
	 * @param pTexture A texture object which becomes the sprite 
	 */
	public Sprite(Texture pTexture) {
		this(pTexture, ImageRenderMode.FLAT);
	}
	
	/**
	 * Create a new sprite from a specified image file path.
	 *
	 * @param pRef A reference to the image on which this sprite should be based
	 * @param pRenderMode mode to draw the image on the screen
	 */
	public Sprite(String pRef, ImageRenderMode pRenderMode) throws IOException{
		this(TextureLoader.getInstance().getTexture(pRef), pRenderMode);
	}
	
	/**
	 * Create a new sprite from a given texture
	 * 
	 * @param pTexture A texture object which becomes the sprite
	 * @param pRenderMode Mode to draw the image on the screen in 
	 */
	public Sprite(Texture pTexture, ImageRenderMode pRenderMode) {
		texture = pTexture;
		height = pTexture.getImageHeight();
		width = pTexture.getImageWidth();
		renderMode = pRenderMode;
		zDistance = (float)( ( (double)height ) * -TANGENT * renderMode.getModeMultiplier());
	}
	
	/**
	 * Get the width of this sprite in pixels
	 *
	 * @return The width of this sprite in pixels
	 */
	public int getWidth() {
		return texture.getImageWidth();
	}

	/**
	 * Get the height of this sprite in pixels
	 *
	 * @return The height of this sprite in pixels
	 */
	public int getHeight() {
		return texture.getImageHeight();
	}

	/**
	 * Draw the sprite at the specified location
	 *
	 * @param x The x location at which to draw this sprite
	 * @param y The y location at which to draw this sprite
	 * @param z The z location at which to draw the sprite
	 * @param delta The amount of time that has past since last frame drawn
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