package com.stonetolb.engine.component.movement;

import com.artemis.Component;

/**
 * Component that represents a rotational orientation of an
 * entity. Rotation values are angle values increasing clockwise 
 * with zero degrees pointed towards the right of the screen.
 * 
 * @author james.baiera
 * 
 */
public class Rotation extends Component {
	private double rotation;
	
	public Rotation(float pRotation) {
		rotation = pRotation;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public void setRotation(double pRotation) {
		rotation = pRotation;
	}
}
