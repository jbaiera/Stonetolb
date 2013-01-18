package com.stonetolb.engine.component.movement;

import com.artemis.Component;

public class VelocityComponent extends Component {
	private float velocity;
	
	public VelocityComponent(float pVelocity) {
		velocity = pVelocity;
	}
	
	public float getVelocity() {
		return velocity;
	}
	
	public void setVelocity(float pVelocity) {
		velocity = pVelocity;
	}
}
