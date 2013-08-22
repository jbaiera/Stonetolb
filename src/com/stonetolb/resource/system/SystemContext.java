package com.stonetolb.resource.system;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Context object used to abstract away static system calls, such as OpenGL
 * render calls.
 *
 * @author james.baiera
 */
public interface SystemContext {

	// TEXTURE OPERATIONS -----------------------------------------------------

	/**
	 * Binds a texture to rendering memory.
	 */
	void bindTexture(int target, int textureId);

	void textureParameterInt(int target, int param, int value);

	void generateTextures(IntBuffer texureIDBuffer);

	void textureImage2d(int target,
						int level,
						int internalFormat,
						int width,
						int height,
						int border,
						int format,
						int type,
						ByteBuffer textureBuffer
					   );

	// MATRIX OPERATIONS ------------------------------------------------------
	void setMatrixMode(int matrixModeId);

	void loadIdentityMatrix();

	void createOrthogonal(double left, double right, double bottom, double top, double near, double far);

	void pushMatrix();

	void popMatrix();

	// DRAWING OPERATIONS -----------------------------------------------------
	void translate(int x, int y, int z);

	void penDown(int mode);

	void addTextureCoordinate(float s, float t);

	void addVertex3f(float x, float y, float z);

	void penUp();

}
