package com.stonetolb.engine.component.render;

import com.artemis.Component;
import com.stonetolb.render.Camera;

public class CameraMount extends Component {
	private float xOffset;
	private float yOffset;
	
	public CameraMount(float pXOffset, float pYOffset) {
		xOffset = pXOffset;
		yOffset = pYOffset;
	}
	
	public void activate() {
		Camera.getCamera().attachTo(this);
	}
	
	public void deactivate() {
		Camera cam = Camera.getCamera();
		if (cam.isAttachedTo(this)) {
			cam.detach();
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
