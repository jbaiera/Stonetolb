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
	private Texture	texture;
	private int 	width;
	private int 	height;
	
	/**
	 * Create a new sprite from a specified image
	 * 
	 * @param ref
	 */
	public Sprite(String ref) {
		try {
			texture = TextureLoader.getInstance().getTexture("sprites/" + ref);
			width = texture.getImageWidth();
			height = texture.getImageHeight();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
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
	public int getHeight(){
		return texture.getImageHeight();
	}
	
	/**
	 * Draw the sprite at the specified location
	 * 
	 * @param x The x location at which to draw this sprite
	 * @param y The y location at which to draw this sprite
	 */
	public void draw(int x, int y) {
		//push a matrix in
		glPushMatrix();	
		//bind to the texture for this sprite
		texture.bind();
		// Move to the start location
		glTranslatef(x,y,0);
		//begin drawing
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(0,0);
			
			glTexCoord2f(0, texture.getHeight());
			glVertex2f(0,height);
			
			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex2f(width,height);
			
			glTexCoord2f(texture.getWidth(), 0);
			glVertex2f(width, 0);
		glEnd();
		glPopMatrix();
	}
}






























