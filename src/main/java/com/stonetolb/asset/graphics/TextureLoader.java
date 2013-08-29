package com.stonetolb.asset.graphics;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.google.common.collect.Maps;
import com.stonetolb.resource.ResourceContext;
import org.lwjgl.BufferUtils;

/**
 * A utility class to load textures for OpenGL. This source is based
 * on a loader that can be found in the LWJGL example section (www.lwjgl.org)
 * that was based off of a loader in the Java Gaming Wiki (www.javagaming.org).
 * It has been simplified slightly for explicit 2D graphics use.
 * 
 * This loader reads a picture into the system, and converts it into a format
 * that is compatible with the OpenGL texture rules.
 * 
 * This class is a singleton, since there should only ever be one place that 
 * can return a Texture since they are cached as they are loaded.
 * 
 * @author james.baiera
 */
public class TextureLoader {
    /** The table of textures that have been loaded in this loader */
    private HashMap<String, Texture> table = new HashMap<String, Texture>();

    /** The color model including alpha for the GL image */
    private ColorModel glAlphaColorModel;

    /** The color model for the GL image */
    private ColorModel glColorModel;

	/** The context used for all system calls */
	ResourceContext resourceContext;

    /** Scratch buffer for texture ID's */
    private IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);

	/**
	 * TextureLoader registry. Contains any texture loader created keyed by it's system
	 */
	private static final Map<ResourceContext, TextureLoader> registry
			= Maps.newConcurrentMap();

	public static TextureLoader get(ResourceContext resourceContext) {
		TextureLoader value = registry.get(resourceContext);
		if(value == null) {
			value = new TextureLoader(resourceContext);
			registry.put(resourceContext, value);
		}
		return value;
	}

    public static TextureLoader getInstance() {
		return TextureLoader.get(ResourceContext.get());
    }
    
    /**
     * Create a new texture loader based on the game panel
     */
    private TextureLoader(ResourceContext resourceContext) {
		this.resourceContext = resourceContext;

		glAlphaColorModel
				= new ComponentColorModel(
						ColorSpace.getInstance(ColorSpace.CS_sRGB),
						new int[]{8, 8, 8, 8},
						true,
						false,
						ComponentColorModel.TRANSLUCENT,
						DataBuffer.TYPE_BYTE
					);

		glColorModel
				= new ComponentColorModel(
						ColorSpace.getInstance(ColorSpace.CS_sRGB),
	                    new int[] {8,8,8,0},
						false,
						false,
						ComponentColorModel.OPAQUE,
						DataBuffer.TYPE_BYTE
					);
    }

    /**
     * Create a new texture ID
     *
     * @return A new texture ID
     */
    private int createTextureID() {
      resourceContext.getSystemContext().generateTextures(textureIDBuffer);
      return textureIDBuffer.get(0);
    }

    /**
     * Load a texture
     *
     * @param resourceName - The location of the resource to load
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(String resourceName) 
    throws IOException 
    {
        Texture tex = table.get(resourceName);

        if (tex != null) {
            return tex;
        }

        tex = getTexture(resourceName,
                         GL_TEXTURE_2D,	// target
                         GL_RGBA,     	// dst pixel format
                         GL_LINEAR, 	// min filter (unused)
                         GL_LINEAR);

        table.put(resourceName,tex);

        return tex;
    }

    /**
     * Load a texture into OpenGL from a image reference on
     * disk.
     *
     * @param resourceName - The location of the resource to load
     * @param target - The GL target to load the texture against
     * @param dstPixelFormat - The pixel format of the screen
     * @param minFilter - The minimizing filter
     * @param magFilter - The magnification filter
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(String resourceName
    		, int target
			, int dstPixelFormat
			, int minFilter
			, int magFilter
    		) 
    throws IOException 
    {
        int srcPixelFormat;

        // create the texture ID for this texture
        int textureID = createTextureID();
        
        Texture.Builder tb = Texture.builder(target, textureID);

        // bind this texture
        resourceContext.getSystemContext().bindTexture(target, textureID);

		// Load the image and set dimensions
        BufferedImage bufferedImage = loadImage(resourceName);
        tb.setImageWidth(bufferedImage.getWidth());
        tb.setImageHeight(bufferedImage.getHeight());

		// Color mode of the image
        if (bufferedImage.getColorModel().hasAlpha()) {
        	// DEBUG: System.out.println("Get : Has Alpha!");
            srcPixelFormat = GL_RGBA;
        } else {
        	// DEBUG: System.out.println("Get : No Alpha!");
            srcPixelFormat = GL_RGB;
        }

        // convert that image into a byte buffer of texture data
        ByteBuffer textureBuffer = convertImageData(bufferedImage, tb);

		// Perform loading of texture into system memory...

		// Filters for 2D textures
        if (target == GL_TEXTURE_2D) {
			resourceContext
					.getSystemContext()
					.textureParameterInt(target,
										 GL_TEXTURE_MIN_FILTER,
										 minFilter
										);
			resourceContext
					.getSystemContext()
					.textureParameterInt(target,
										 GL_TEXTURE_MAG_FILTER,
										 magFilter
										);
		}

        // produce a texture from the byte buffer
		resourceContext
				.getSystemContext()
				.textureImage2d(target,
								0,
								dstPixelFormat,
								get2Fold(bufferedImage.getWidth()),
								get2Fold(bufferedImage.getHeight()),
								0,
								srcPixelFormat,
								GL_UNSIGNED_BYTE,
								textureBuffer
							   );

		// Set the texture's SystemContext reference to be the same as its parent loader
		tb.setSystemContext(resourceContext.getSystemContext());

        return tb.build();
    }

    /**
     * Get the closest greater power of 2 to the fold number
     *
     * @param fold The target number
     * @return The power of 2
     */
    private static int get2Fold(int fold) {
		if (fold <= 0) {
			return 0;
		}

		--fold;
		fold |= fold >> 1;
		fold |= fold >> 2;
		fold |= fold >> 4;
		fold |= fold >> 8;
		fold |= fold >> 16;
		fold |= fold >> 32;
		return ++fold;
    }

    /**
     * Convert the buffered image to a texture
     *
     * @param bufferedImage The image to convert to a texture
     * @param textureBuilder The texture builder to store the data into
     * @return A buffer containing the data
     */
    private ByteBuffer convertImageData(BufferedImage bufferedImage,Texture.Builder textureBuilder) {
        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;

        int texWidth = 2;
        int texHeight = 2;

        // find the closest power of 2 for the width and height
        // of the produced texture
        while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }

        textureBuilder.setTextureHeight(texHeight);
        textureBuilder.setTextureWidth(texWidth);

        // create a raster that can be used by OpenGL as a source
        // for a texture
        if (bufferedImage.getColorModel().hasAlpha()) {
        	// DEBUG: System.out.println("Convert : Has Alpha!");
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,true,new Hashtable<String,Object>());
        } else {
        	// DEBUG: System.out.println("Convert : No Alpha!");
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,true,new Hashtable<String, Object>());
        }

        // copy the source image into the produced image
        Graphics2D g = (Graphics2D) texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.drawImage(bufferedImage,0,0,null);

        // build a byte buffer from the temporary image
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();

        return imageBuffer;
    }

    /**
     * Load a given resource as a buffered image
     *
     * @param ref The location of the resource to load
     * @return The loaded buffered image
     * @throws IOException Indicates a failure to find a resource
     */
    private BufferedImage loadImage(String ref) throws IOException {
		Image img = resourceContext.getIOContext().loadImageFromResource(ref);
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }
}