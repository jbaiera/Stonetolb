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

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Implementation of sprite that uses an OpenGL quad and a Texture
 * to render a given image to the screen.
 * 
 * @author comet
 */
public class Sprite {

	/** The texture that stores the image for this sprite */
	private Texture	texture;

	/** The width in pixels of this sprite */
	private int			width;

	/** The height in pixels of this sprite */
	private int			height;

	/**
	 * Create a new sprite from a specified image file path.
	 *
	 * @param ref A reference to the image on which this sprite should be based
	 */
	public Sprite(String ref) {
		try {
			this.texture = TextureLoader.getInstance().getTexture("sprites/" + ref);
			this.width = texture.getImageWidth();
			this.height = texture.getImageHeight();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Create a new sprite from a given texture
	 * 
	 * @param tex A texture object which becomes the sprite 
	 */
	public Sprite(Texture tex) {
		this.texture = tex;
		this.height = tex.getImageHeight();
		this.width = tex.getImageWidth();
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
	 */
	public void draw(int x, int y) {
		// DEBUG: System.out.println("(" + texture.getXOrigin() + "," + texture.getYOrigin() + ") : (" + texture.getWidth() + "," + texture.getHeight() + ")");
		
		// store the current model matrix
		glPushMatrix();

		// bind to the appropriate texture for this sprite
		texture.bind();

		// translate to the right location and prepare to draw
		glTranslatef(x, y, 0);

		// draw a quad textured to match the sprite
		glBegin(GL_QUADS);
		{
			glTexCoord2f(texture.getXOrigin(), texture.getYOrigin());
			glVertex2f(0, 0);

			glTexCoord2f(texture.getXOrigin(), texture.getHeight());
			glVertex2f(0, height);

			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex2f(width, height);

			glTexCoord2f(texture.getWidth(), texture.getYOrigin());
			glVertex2f(width, 0);
		}
		glEnd();

		// restore the model view matrix to prevent contamination
		glPopMatrix();
	}
}