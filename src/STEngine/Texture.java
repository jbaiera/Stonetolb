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

	/** The GL target type */
	private int		target;

	/** The GL texture ID */
	private int		textureID;

	/** The width of the image */
	private int		imgWidth;

	/** The height of the image */
	private int		imgHeight;
	
	/** The width of the texture */
	private int		texWidth;

	/** The height of the texture */
	private int		texHeight;
	
	/** The number of x sections */
	private int 	xSection;
	
	/** The number of y sections */
	private int 	ySection;

	/** The offset x position */
	private int 	xOffset;
	
	/** The offset y position */
	private int 	yOffset;
	
	/** The ratio of the width of the image to the texture */
	private float	imgWidthRatio;

	/** The ratio of the height of the image to the texture */
	private float	imgHeightRatio;
	
	/** The ratio of the x offset to the texture size */
	private float 	imgXOrigin;
	
	/** The ratio of the y offset to the texture size */
	private float 	imgYOrigin;
	

	/**
	 * Create a new texture
	 *
	 * @param target The GL target
	 * @param textureID The GL texture ID
	 */
	public Texture(int target, int textureID) {
		this.target = target;
		this.textureID = textureID;
		
		// only one sub texture which is itself
		this.xSection = 1;
		this.ySection = 1;
		
		// No offset
		this.xOffset = 0;
		this.yOffset = 0;
		
		// init origins just in case
		this.imgXOrigin = 0;
		this.imgYOrigin = 0;
		
		// update origin points
		this.updateXOrigin();
		this.updateYOrigin();
	}

	/**
	 * Bind the specified GL context to a texture
	 */
	public void bind() {
		glBindTexture(target, textureID);
	}
	
	public Texture getSubTexture(int a, int b) {
		if((a < xSection) && (b < ySection)) {
			Texture subtex = new Texture(target, textureID);
			subtex.xOffset = a;
			subtex.yOffset = b;
			subtex.setHeight(imgHeight/ySection);
			subtex.setWidth(imgWidth/xSection);
			subtex.setTextureHeight(texHeight);
			subtex.setTextureWidth(texWidth);
			return subtex;
		} else {
			return this;
		}
	}

	/**
	 * Set the height of the image
	 *
	 * @param height The height of the image
	 */
	public void setHeight(int height) {
		this.imgHeight = height;
		setHeight();
	}

	/**
	 * Set the width of the image
	 *
	 * @param width The width of the image
	 */
	public void setWidth(int width) {
		this.imgWidth = width;
		setWidth();
	}

	/**
	 * Get the height of the original image
	 *
	 * @return The height of the original image
	 */
	public int getImageHeight() {
		return imgHeight;
	}

	/**
	 * Get the width of the original image
	 *
	 * @return The width of the original image
	 */
	public int getImageWidth() {
		return imgWidth;
	}
	
	/**
	 * Get the height of the effective image texture
	 *
	 * @return The height of physical texture
	 */
	public float getHeight() {
		return (imgYOrigin + imgHeightRatio);
	}

	/**
	 * Get the width of the effective image texture
	 *
	 * @return The width of physical texture
	 */
	public float getWidth() {
		return (imgXOrigin + imgWidthRatio);
	}

	public float getXOrigin() {
		return imgXOrigin;
	}
	
	public float getYOrigin() {
		return imgYOrigin;
	}
	
	/**
	 * Sets how many subtextures span the x axis
	 * 
	 * @param Sx number of subtextures nested in the x axis
	 */
	public void setXSections(int Sx) {
		this.xSection = Sx;
	}
	
	/**
	 * Sets how many subtextures span the y axis
	 * 
	 * @param Sy number of subtextures nested in the y axis
	 */
	public void setYSections(int Sy) {
		this.ySection = Sy;
	}
	
	/**
	 * Set the height of the texture
	 *
	 * @param texHeight The height of the texture
	 */
	public void setTextureHeight(int texHeight) {
		this.texHeight = texHeight;
		setHeight();
		updateYOrigin();
	}

	/**
	 * Set the width of this texture
	 *
	 * @param texWidth The width of the texture
	 */
	public void setTextureWidth(int texWidth) {
		this.texWidth = texWidth;
		setWidth();
		updateXOrigin();
	}

	/**
	 * Set the height of the texture. This will update the
	 * ratio also.
	 */
	private void setHeight() {
		if (texHeight != 0) {
			imgHeightRatio = ((float) imgHeight) / texHeight;
		}
	}

	/**
	 * Set the width of the texture. This will update the
	 * ratio also.
	 */
	private void setWidth() {
		if (texWidth != 0) {
			imgWidthRatio = ((float) imgWidth) / texWidth;
		}
	}
	
	private void updateXOrigin() {
		if(texWidth != 0) {
			int rawOffset = xOffset * imgWidth;
			imgXOrigin = ((float) rawOffset) / texWidth;
		}
	}
	
	private void updateYOrigin() {
		if(texHeight != 0) {
			int rawOffset = yOffset * imgHeight;
			imgYOrigin = ((float) rawOffset) / texHeight;
		}
	}
}