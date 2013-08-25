package com.stonetolb.resource.io;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * IOContext object that uses the current ResourceFolder as the root of
 * resource searches and retrievals.
 *
 * @author james.baiera
 */
public class ResourceFolderIOContext implements IOContext {

	private static final ResourceFolderIOContext INSTANCE
			= new ResourceFolderIOContext();

	public static ResourceFolderIOContext getContext() {
		return INSTANCE;
	}

	private ResourceFolderIOContext() { /* EMPTY CONSTRUCTOR */ }

	@Override
	public URL getURLForResource(String resourcePath)
	throws IOException
	{
		URL url = getClass().getClassLoader().getResource(resourcePath);
		if (url == null) {
			throw new IOException("Cannot find: " + resourcePath);
		}
		return url;
	}

	@Override
	public Image loadImageFromResource(String resourcePath)
	throws IOException
	{
		return new ImageIcon(getURLForResource(resourcePath)).getImage();
	}
}
