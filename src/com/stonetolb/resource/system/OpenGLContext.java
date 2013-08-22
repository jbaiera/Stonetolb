package com.stonetolb.resource.system;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

/**
 * SystemContext implementation that forwards all calls to the LWJGL bindings
 * for OpenGL.
 *
 * @author james.baiera
 */
public class OpenGLContext implements SystemContext {

	private static final OpenGLContext INSTANCE = new OpenGLContext();

	/**
	 * Retrieves the context object instance.
	 *
	 * @return this singular system context object
	 */
	public static OpenGLContext getContext() {
		return INSTANCE;
	}

	private OpenGLContext() { /* EMPTY CONSTRUCTOR */};

	@Override
	public void bindTexture(int target, int textureId) {
		GL11.glBindTexture(target, textureId);
	}

	@Override
	public void textureParameterInt(int target, int param, int value) {
		GL11.glTexParameteri(target, param, value);
	}

	@Override
	public void generateTextures(IntBuffer texureIDBuffer) {
		GL11.glGenTextures(texureIDBuffer);
	}

	@Override
	public void textureImage2d(int target, int level, int internalFormat, int width, int height, int border, int format, int type, ByteBuffer textureBuffer) {
		GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, type, textureBuffer);
	}

	@Override
	public void setMatrixMode(int matrixModeId) {
		GL11.glMatrixMode(matrixModeId);
	}

	@Override
	public void loadIdentityMatrix() {
		GL11.glLoadIdentity();
	}

	@Override
	public void createOrthogonal(double left, double right, double bottom, double top, double near, double far) {
		GL11.glOrtho(left, right, bottom, top, near, far);
	}

	@Override
	public void pushMatrix() {
		GL11.glPushMatrix();
	}

	@Override
	public void popMatrix() {
		GL11.glPopMatrix();
	}

	@Override
	public void translate(int x, int y, int z) {
		GL11.glTranslatef(x, y, z);
	}

	@Override
	public void penDown(int mode) {
		GL11.glBegin(mode);

	}

	@Override
	public void addTextureCoordinate(float s, float t) {
		GL11.glTexCoord2f(s, t);
	}

	@Override
	public void addVertex3f(float x, float y, float z) {
		GL11.glVertex3f(x, y, z);
	}

	@Override
	public void penUp() {
		GL11.glEnd();
	}
}
