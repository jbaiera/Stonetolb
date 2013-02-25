package com.stonetolb.engine.component.position;

import com.artemis.Component;

/**
 * Component used to represent an Entity's location in space.
 * 
 * @author james.baiera
 *
 */
public class Position extends Component{
	private int xpos;
	private int ypos;
	
	public Position(int pXpos, int pYpos) {
		xpos = pXpos;
		ypos = pYpos;
	}
	
	public int getX() {
		return xpos;
	}
	
	public void setX(int pNew) {
		xpos = pNew;
	}
	
	public int getY() {
		return ypos;
	}
	
	public void setY(int pNew) {
		ypos = pNew;
	}
	
	public void setPosition(Position other) {
		xpos = other.xpos;
		ypos = other.ypos;
	}
	
	public void setPosition(int x, int y) {
		xpos = x;
		ypos = y;
	}
}
