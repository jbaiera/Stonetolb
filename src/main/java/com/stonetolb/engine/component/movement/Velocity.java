package com.stonetolb.engine.component.movement;

import com.artemis.Component;

/**
 * Component that represents a speed that an Entity may posses.
 * Despite the name Velocity, no orientation is stored. See
 * {@link Rotation}
 * 
 * @author james.baiera
 *
 */
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
