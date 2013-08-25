package com.stonetolb.engine.profiles;

import org.lwjgl.input.Keyboard;

/**
 * Module used to run any and all world movement and interaction
 * @author james.baiera
 *
 */
public class WorldProfile {
	public enum Control {
		ARROWS(   Keyboard.KEY_UP
				, Keyboard.KEY_DOWN
				, Keyboard.KEY_LEFT
				, Keyboard.KEY_RIGHT
			),
		WASD(	  Keyboard.KEY_W
				, Keyboard.KEY_S
				, Keyboard.KEY_A
				, Keyboard.KEY_D
			);
		
		private int up;
		private int down;
		private int left;
		private int right;
		
		private Control(int pUp, int pDown, int pLeft, int pRight)
		{
			up = pUp;
			down = pDown;
			left = pLeft;
			right = pRight;
		}
		
		public int keyUp()    { return up;    }
		public int keyDown()  { return down;  }
		public int keyLeft()  { return left;  }
		public int keyRight() { return right; }
	}
	
	public enum Speed {
		STOP(0),
		WALK(75),
		RUN(150);
		
		private int speed;
		
		private Speed(int pSpeed) {
			speed = pSpeed;
		}
		
		public int getSpeed() {
			return speed;
		}
	}
	
	
	/**
	 * Direction values are measured in degrees starting from the right and rotating clockwise
	 * @author james.baiera
	 *
	 */
	public enum WorldDirection {
		UP(270),
		RIGHT(0),
		DOWN(90),
		LEFT(180),
		STILL(90);
		
		private WorldDirection(float pDirection) {
			direction = pDirection;
		}
		
		private float direction;
		
		public float getDirection() {
			return direction;
		}
	}
}
