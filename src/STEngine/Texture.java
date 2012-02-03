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

import static org.lwjgl.opengl.GL11.*;

/**
 * A texture to be bound within OpenGL. This object is responsible for
 * keeping track of a given OpenGL texture and for calculating the
 * texturing mapping coordinates of the full image.
 * 
 * Since textures need to be powers of 2, the actual texture may be
 * considerably enlarged than the source image and hence the texture
 * mapping coordinates need to be adjusted to match up the drawing the
 * sprite against the texture.
 * 
 * @author comet
 */
public class Texture {
	private int target;
	private int textureID;
	private int height;
	private int width;
	private int texWidth;
	private int texHeight;
	private float widthRatio;
	private float heightRatio;
	
	/**
	 * Create a new texture
	 * 
	 * @param target
	 * @param textureID
	 */
	public Texture(int target, int textureID) {
		this.target = target;
		this.textureID = textureID;
	}
	
	/**
	 * Bind the specified GL context to a texture
	 */
	public void bind() {
		glBindTexture(target, textureID);
	}
	
	/**
	 * Set the height of the image
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
		setHeight();
	}
	
	/**
	 * Set the width of the image
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
		setWidth();
	}
	
	/**
	 * Get the height of the image
	 * 
	 * @return
	 */
	public int getImageHeight() {
		return height;
	}
	
	/**
	 * Get the width of the image
	 * @return
	 */
	public int getImageWidth() {
		return width;
	}
	
	/**
	 * Get the image to texture height ratio
	 * 
	 * @return
	 */
	public float getHeight() {
		return heightRatio;
	}
	
	/**
	 * Get the image to texture width ratio
	 * 
	 * @return
	 */
	public float getWidth() {
		return widthRatio;
	}
	
	/**
	 * Set the texture's height
	 * 
	 * @param texHeight
	 */
	public void setTextureHeight(int texHeight) {
		this.texHeight = texHeight;
		setHeight();
	}
	
	/**
	 * Set the texture's width
	 * 
	 * @param texWidth
	 */
	public void setTextureWidth(int texWidth) {
		this.texWidth = texWidth;
		setWidth();
	}
	
	/**
	 * Update the height ratio
	 */
	private void setHeight() {
		if (texHeight != 0) {
			heightRatio = ((float) height) / texHeight;
		}
	}
	
	/**
	 * update the width ratio
	 */
	private void setWidth() {
		if (texWidth != 0) {
			widthRatio = ((float) width) / texWidth;
		}
	}
}















































