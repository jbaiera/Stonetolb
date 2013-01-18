package com.stonetolb.engine.component.movement;

import com.artemis.Component;

public class Velocity extends Component {
	private float velocity;
	
	public Velocity(float pVelocity) {
		velocity = pVelocity;
	}
	
	public float getVelocity() {
		return velocity;
	}
	
	public void setVelocity(float pVelocity) {
		velocity = pVelocity;
	}
}
