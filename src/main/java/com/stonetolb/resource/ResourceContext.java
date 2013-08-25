package com.stonetolb.resource;

import com.stonetolb.resource.io.IOContext;
import com.stonetolb.resource.io.ResourceFolderIOContext;
import com.stonetolb.resource.system.OpenGLContext;
import com.stonetolb.resource.system.SystemContext;

/**
 * A ResourceContext is any complex context object that contains any number of
 * system oriented resources and services. This acts as an abstraction layer
 * between the software and the underlying systems and dependencies.
 *
 * @author james.baiera
 */
public class ResourceContext {

	private SystemContext systemContext;
	private IOContext ioContext;

	private static final ResourceContext DEFAULT
			= new ResourceContext(
					OpenGLContext.getContext(),
					ResourceFolderIOContext.getContext()
				);

	public static ResourceContext get() {
		return DEFAULT;
	}

	private ResourceContext(SystemContext systemContext, IOContext ioContext) {
		this.systemContext = systemContext;
		this.ioContext = ioContext;
	}

	public SystemContext getSystemContext() {
		return systemContext;
	}

	public IOContext getIOContext() {
		return ioContext;
	}
}
