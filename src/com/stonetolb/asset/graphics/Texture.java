package com.stonetolb.asset.graphics;

import static org.lwjgl.opengl.GL11.*;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * A texture to be bound within OpenGL. This object is responsible for
 * keeping track of a given OpenGL texture and for calculating the
 * texturing mapping coordinates of the full image.
 * <p>
 * Since textures need to be powers of 2, the actual texture may be
 * considerably enlarged than the source image and hence the texture
 * mapping coordinates need to be adjusted to match up the drawing the
 * sprite against the texture.
 * 
 * @author james.baiera
 */
public class Texture {

	/** The GL target type */
	private final int target;

	/** The GL texture ID */
	private final int textureID;

	/** The width of the image */
	private final int imgWidth;

	/** The height of the image */
	private final int imgHeight;
	
	/** The width of the texture */
	private final int texWidth;

	/** The height of the texture */
	private final int texHeight;
	
	/** The offset x position */
	private final int xOffset;
	
	/** The offset y position */
	private final int yOffset;
	
	/** The ratio of the width of the image to the texture */
	private final float imgWidthRatio;

	/** The ratio of the height of the image to the texture */
	private final float imgHeightRatio;
	
	/** The ratio of the x offset to the texture size */
	private final float imgXOrigin;
	
	/** The ratio of the y offset to the texture size */
	private final float imgYOrigin;
	
	public static class Builder {
		private Integer target = null;
		private Integer textureID = null;
		private Integer textureWidth = null;
		private Integer textureHeight = null;
		private Integer imageWidth = null;
		private Integer imageHeight = null;
		
		private Builder(int target, int textureId) {
			this.target = target;
			this.textureID = textureId;
		}
		
		public Builder setImageHeight(int imageHeight) {
			this.imageHeight = imageHeight;
			return this;
		}
		
		public Builder setImageWidth(int imageWidth) {
			this.imageWidth = imageWidth;
			return this;
		}
		
		public Builder setTextureHeight(int textureHeight) {
			this.textureHeight = textureHeight;
			return this;
		}
		
		public Builder setTextureWidth(int textureWidth) {
			this.textureWidth = textureWidth;
			return this;
		}
		
		public Texture build() {
			Preconditions.checkState(target != null, "Target was not set");
			Preconditions.checkState(textureID != null, "Texture ID was not set");
			Preconditions.checkState(textureWidth != null, "Texture Width was not set");
			Preconditions.checkState(textureHeight != null, "Texture Height was not set");
			Preconditions.checkState(imageWidth != null, "Image Width was not set");
			Preconditions.checkState(imageHeight != null, "Image Height was not set");
			
			return new Texture(target, textureID, textureWidth, textureHeight, imageWidth, imageHeight, 0, 0);
		}
	}
	
	/**
	 * Gets the builder for the Texture Object.
	 * @param target - The Texture's target
	 * @param textureId - The Texture's ID
	 * @return a builder
	 */
	public static Builder builder(int target, int textureId) {
		return new Builder(target, textureId);
	}

	/**
	 * Create a new texture
	 *
	 * @param target The GL target
	 * @param textureID The GL texture ID
	 */
	private Texture(int target, int textureID, int texWidth, int texHeight, int imgWidth, int imgHeight, int xOffset, int yOffset) {
		Preconditions.checkArgument(texWidth > 0, "Invalid texture width : " + texWidth);
		Preconditions.checkArgument(texHeight > 0, "Invalid texture height : " + texHeight);
		
		this.target = target;
		this.textureID = textureID;
		
		// No offset
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		
		// update origin points
		this.imgXOrigin = ((float) this.xOffset) / this.texWidth;
		this.imgYOrigin = ((float) this.yOffset) / this.texHeight;
			
		this.imgHeightRatio = ((float) this.imgHeight) / this.texHeight;
		this.imgWidthRatio = ((float) this.imgWidth) / this.texWidth;
	}

	/**
	 * Bind the specified GL context to a texture
	 */
	public void bind() {
		glBindTexture(target, textureID);
	}
	
	public Texture getSubTexture(int x, int y, int w, int h)
	{
		Preconditions.checkArgument((0 <= x && x <= imgWidth), "X coordinate is out of image bounds");
		Preconditions.checkArgument((0 <= y && y <= imgHeight), "Y coordinate is out of image bounds");
		Preconditions.checkArgument((0 <= w && x+w <= imgWidth), "Width is out of image bounds");
		Preconditions.checkArgument((0 <= h && y+h <= imgHeight), "Height is out of image bounds");
		
		return new Texture(target, textureID, texWidth, texHeight, w, h, x, y);
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
		
	@Override
	public int hashCode() {
		return Objects.hashCode(
					target
					, textureID
					, imgWidth
					, imgHeight
					, texWidth
					, texHeight
					, xOffset
					, yOffset
					, imgWidthRatio
					, imgHeightRatio
					, imgXOrigin
					, imgYOrigin
				);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Texture))
			return false;
		Texture other = (Texture) obj;
		return Objects.equal(target, other.target)
				&& Objects.equal(textureID, other.textureID)
				&& Objects.equal(imgWidth, other.imgWidth)
				&& Objects.equal(imgHeight, other.imgHeight)
				&& Objects.equal(texWidth, other.texWidth)
				&& Objects.equal(texHeight, other.texHeight)
				&& Objects.equal(xOffset, other.xOffset)
				&& Objects.equal(yOffset, other.yOffset)
				&& Objects.equal(imgWidthRatio, other.imgWidthRatio)
				&& Objects.equal(imgHeightRatio, other.imgHeightRatio)
				&& Objects.equal(imgXOrigin, other.imgXOrigin)
				&& Objects.equal(imgYOrigin, other.imgYOrigin)
				;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("Texture [target=" + target + ", textureID=" + textureID)
				.append(", imgWidth=" + imgWidth + ", imgHeight=" + imgHeight)
				.append(", texWidth=" + texWidth + ", texHeight=" + texHeight)
				.append(", xOffset=" + xOffset + ", yOffset=" + yOffset)
				.append(", imgWidthRatio=" + imgWidthRatio + ", imgHeightRatio=")
				.append(imgHeightRatio + ", imgXOrigin=" + imgXOrigin)
				.append(", imgYOrigin=" + imgYOrigin + "]")
				.toString();
	}
	
	
}