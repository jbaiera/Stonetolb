package com.stonetolb.engine.component.movement;

import com.artemis.Component;

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
