package com.stonetolb.render;

import org.lwjgl.opengl.GL11;

import com.stonetolb.engine.component.render.CameraMount;
import com.stonetolb.util.Vector2f;

/**
 * The Camera is a special class that represents the rendering world's camera object.
 * <p>
 * Camera is a static manager class that is capable of registering a {@link Vantage} and a
 * {@link CameraMount} object. The {@link Vantage} object controls how the camera moves what 
 * perspective the Camera uses, whilst the {@link CameraMount} is used to link the Camera 
 * to an Entity.
 * <p>
 * Camera comes initialized out of the box, located at (0,0), starting with a basic 
 * {@link FixedVantage} instance and no {@link CameraMount} object registered.
 * 
 * @author james.baiera
 *
 */
public final class Camera {
	private static volatile Vantage ACTIVE = FixedVantage.create();
	private static volatile CameraMount MOUNT = null;
	
	/**
	 * Registers the given Vantage object to the active Camera.
	 * 
	 * @param vantage - Vantage to be set. Ignores null.
	 */
	public static final synchronized void setVantage(Vantage vantage) {
		if(vantage != null) {
			ACTIVE = vantage;
		}
	}
	
	/**
	 * Returns a handle to the currently registered Vantage instance.
	 * 
	 * @return Vantage object in use by this Camera.
	 */
	public static final Vantage getInstance() {
		return ACTIVE;
	}
	
	/**
	 * Attaches Camera to given CameraMount instance. 
	 * 
	 * @param mnt - Mount to attach to. Ignores null.
	 * <br> If you need to clear this value, prefer to use {@link Camera#detach()} instead.
	 * @return The CameraMount in the parameter field. Passes through for method chaining.
	 */
	public static final synchronized CameraMount attachTo(CameraMount mnt) {
		if(mnt != null) {
			MOUNT = mnt;
		}
		return mnt;
	}
	
	/**
	 * Checks if Camera is attached to the given CameraMount
	 * 
	 * @param mnt - CameraMount object to test.
	 * @return true if attached to given mount, false if not.
	 */
	public static final synchronized boolean isAttachedTo(CameraMount mnt) {
		return MOUNT == mnt;
	}
	
	/**
	 * Detaches the Camera from it's mount, and sets the Camera position back to origin
	 */
	public static final synchronized void detach() {
		MOUNT = null;
		ACTIVE.updatePosition(Vector2f.NULL_VECTOR);
	}

	/**
	 * Helper method to move the Camera.
	 */
	static final void moveCamera(Vector2f position, int screenWidth, int screenHeight) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(
				  (double)position.getX()
				, (double)position.getX() + (double)screenWidth
				, (double)position.getY() + (double)screenHeight
				, (double)position.getY()
				, 2000
				, 2000 * -1
			);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}
