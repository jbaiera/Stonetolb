package com.stonetolb.resource.io;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

/**
 * Context object used for retrieving Files and carrying out IO Operations.
 *
 * @author james.baiera
 */
public interface IOContext {

	Image loadImageFromResource(String resourcePath) throws IOException;

	URL getURLForResource(String resourcePath) throws IOException;
}
