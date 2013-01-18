package com.stonetolb.engine.component.position;

import com.artemis.Component;

public class PositionComponent extends Component{
	private float xpos;
	private float ypos;
	
	public PositionComponent(float pXpos, float pYpos) {
		xpos = pXpos;
		ypos = pYpos;
	}
	
	public float getX() {
		return xpos;
	}
	
	public void setX(float pNew) {
		xpos = pNew;
	}
	
	public float getY() {
		return ypos;
	}
	
	public void setY(float pNew) {
		ypos = pNew;
	}
	
	public void setPosition(PositionComponent other) {
		xpos = other.xpos;
		ypos = other.ypos;
	}
	
	public void setPosition(float x, float y) {
		xpos = x;
		ypos = y;
	}
}
