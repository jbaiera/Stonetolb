package com.stonetolb.render;

import org.lwjgl.opengl.GL11;

import com.stonetolb.engine.component.render.CameraMount;
import com.stonetolb.util.Vector2f;

/**
 * The Camera is a special class that enforces a one to one relationship of Vantage
 * to camera mount.
 * <p>
 * Camera is a static manager class that is capable of registering a Vantage and a
 * CameraMount object. The Vantage is used for view point manipulation, whilst the CameraMount
 * is used to link the Camera to an Entity.
 * <p>
 * Camera comes initialized out of the box, starting with a basic {@link FixedVantage} 
 * instance and no CameraMount object registered.
 * 
 * @author james.baiera
 *
 */
public final class Camera {
	private static volatile Vantage ACTIVE = FixedVantage.create();
	private static volatile CameraMount MOUNT = null;
	
	/**
	 * Registers the given Vantage object to the active Camera.
	 * <br>
	 * This will be the only mutator that sets the Vantage. 
	 * @param vantage Vantage to be set. Ignores null values.
	 */
	public static final synchronized void setVantage(Vantage vantage) {
		if(vantage != null) {
			ACTIVE = vantage;
		}
	}
	
	/**
	 * Returns a handle to the currently regsitered Vantage instance
	 * @return
	 */
	public static final Vantage getInstance() {
		return ACTIVE;
	}
	
	/**
	 * Attaches Camera to given CameraMount instance. 
	 * 
	 * @param mnt Mount to attach to. Ignores null values.
	 * <br> If you need to clear this value, call {@link Camera#detach() detach} method
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
	 * @param mnt
	 * @return
	 */
	public static final synchronized boolean isAttachedTo(CameraMount mnt) {
		return MOUNT == mnt;
	}
	
	/**
	 * Detaches the Camera from it's mount, and setting the Camera position back to origin
	 */
	public static final synchronized void detach() {
		MOUNT = null;
		ACTIVE.updatePosition(Vector2f.NULL_VECTOR);
	}

	/**
	 * 
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
