package com.stonetolb.engine.component.render;

import com.artemis.Component;
import com.stonetolb.engine.component.position.Position;
import com.stonetolb.render.Camera;

/**
 * Component used to attach a Camera object to a specific Entity
 * instance. Camera's are not moved by hand, they are attached to
 * an Entity with this component and a {@link Position} component
 * and their position is updated via system call.
 * 
 * @author james.baiera
 *
 */
public class CameraMount extends Component {
	private float xOffset;
	private float yOffset;
	
	public CameraMount(float pXOffset, float pYOffset) {
		xOffset = pXOffset;
		yOffset = pYOffset;
	}
	
	public void activate() {
		Camera.attachTo(this);
	}
	
	public void deactivate() {
		if (Camera.isAttachedTo(this)) {
			Camera.detach();
		}
	}
	
	public float getXOffset() {
		return xOffset;
	}
	
	public float getYOffset() {
		return yOffset;
	}
	
	public void setXOffset(float pXOffset) {
		xOffset = pXOffset;
	}
	
	public void setYOffset(float pYOffset) {
		yOffset = pYOffset;
	}
}
