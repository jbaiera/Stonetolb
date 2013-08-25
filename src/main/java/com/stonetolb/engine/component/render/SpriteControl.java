package com.stonetolb.engine.component.render;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Component;
import com.google.common.base.Objects;
import com.stonetolb.render.Drawable;
import com.stonetolb.render.NullDrawable;

/**
 * Component that stores Drawable aspects that an Entity may 
 * use to represent itself visually. Entity states are stored
 * in a key that is then mapped to the specific visual representation.
 * 
 * @author james.baiera
 *
 */
public class SpriteControl extends Component {
	private final Map<MovementContext, Drawable> actionMapping;
	private Drawable noOp;
	
	/**
	 * Creates a SpriteControl using a null drawable as the defaul NoOp
	 */
	public SpriteControl() {
		this(NullDrawable.getInstance());
	}
	
	/**
	 * Creates a new SpriteControl using the designated drawable as the
	 * NoOp (no operation) drawable
	 * @param pNoOp
	 */
	public SpriteControl(Drawable pNoOp) {
		noOp = pNoOp;
		actionMapping = new HashMap<MovementContext, Drawable>();
	}
	
	/**
	 * Maps the given drawable instance to the speed and direction given.
	 * When the entity this component is attached to matches the given
	 * criteria, the rendered instance is changed to be the given drawable.
	 * 
	 * @param pDrawable
	 * @param pSpeed
	 * @param pDirection
	 * @return
	 */
	public SpriteControl addAction(Drawable pDrawable, int pSpeed, float pDirection) {
		return addAction(new MovementContext(pDirection, pSpeed), pDrawable);
	}
	
	/**
	 * Adds a mapping of movement to drawable
	 * 
	 * @param pMovement
	 * @param pDrawable
	 * @return this SpriteControl for command chaining
	 */
	private SpriteControl addAction(MovementContext pMovement, Drawable pDrawable) {
		actionMapping.put(pMovement, pDrawable);
		return this;
	}
	
	/**
	 * Returns the drawable object keyed by the given movement parameters.
	 * 
	 * @param pSpeed
	 * @param pDirection
	 * @return
	 */
	public Drawable getDrawable(int pSpeed, float pDirection) {
		return getDrawable(new MovementContext(pDirection, pSpeed));
	}
	
	/**
	 * Returns the drawable keyed by the movement context. If the 
	 * map does not contain the key, then which ever drawable is 
	 * set as the noOp will be returned.
	 * @param pMovement
	 * @return
	 */
	private Drawable getDrawable(MovementContext pMovement) {
		Drawable returnVal;
		returnVal = actionMapping.get(pMovement);
		if (returnVal == null) {
			returnVal = noOp;
		}
		
		return returnVal;
	}
	
	/**
	 * Sets the NoOp (no operation) sprite for this control
	 * @param pDrawable
	 * @return this SpriteControl for command chaining
	 */
	public SpriteControl setNoOp(Drawable pDrawable) {
		noOp = pDrawable;
		return this;
	}
	

	/**
	 * Used to as a memory efficient key on animations for movements 
	 * based on the entity's state.
	 * 
	 * @author james.baiera
	 *
	 */
	private class MovementContext
	{
		private float direction;
		private int speed;
		
		public MovementContext(float pDirection, int pSpeed)
		{
			direction = pDirection;
			speed = pSpeed;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(direction, speed);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof MovementContext))
				return false;
			MovementContext other = (MovementContext) obj;
			return Objects.equal(direction, other.direction)
					&& Objects.equal(speed, other.speed);
		}
	}
}
