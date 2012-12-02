package com.stonetolb.engine.profiles;


public class WorldProfile {

	public static int RUN = 150;
	public static int WALK = 75;
	public static int STOP = 0;
	
	/**
	 * Used to key animations for movements based on the entity's state
	 * 
	 * @author james.baiera
	 *
	 */
	public static class MovementContext
	{
		private float direction;
		private int speed;
		
		public MovementContext(float pDirection, int pSpeed)
		{
			direction = pDirection;
			speed = pSpeed;
		}
		
		@Override
		public int hashCode()
		{
			int prime = 31;
			int result = 17;
			result = prime * result + Float.floatToIntBits(direction);
			result = prime * result + speed;
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			else if (obj == null) 
			{
				return false;
			}
			else if(this.getClass().isInstance(obj))
			{
				MovementContext other = (MovementContext) obj;
				return direction == other.direction
						&& speed == other.speed;
			}
			else 
			{
				return false;
			}
		}
	}
	
	public enum WorldDirection {
		UP(0,0,-1),
		RIGHT(1,1,0),
		DOWN(2,0,1),
		LEFT(3,-1,0),
		STILL(-1, 0, 0);
		
		private WorldDirection(float pDirection, int pXFactor, int pYFactor) {
			direction = pDirection;
			xFactor = pXFactor;
			yFactor = pYFactor;
		}
		
		private int xFactor;
		private int yFactor;
		private float direction;
		
		public float getDirection() {
			return direction;
		}
		
		public int getXFactor() {
			return xFactor;
		}
		
		public int getYFactor() {
			return yFactor;
		}
	}
}
