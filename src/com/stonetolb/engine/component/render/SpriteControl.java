package com.stonetolb.engine.component.render;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Component;
import com.stonetolb.engine.profiles.WorldProfile.MovementContext;
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
	 * Adds a mapping of movement to drawable
	 * 
	 * @param pMovement
	 * @param pDrawable
	 * @return this SpriteControl for command chaining
	 */
	public SpriteControl addAction(MovementContext pMovement, Drawable pDrawable) {
		actionMapping.put(pMovement, pDrawable);
		return this;
	}
	
	/**
	 * Returns the drawable keyed by the movement context. If the 
	 * map does not contain the key, then which ever drawable is 
	 * set as the noOp will be returned.
	 * @param pMovement
	 * @return
	 */
	public Drawable getDrawable(MovementContext pMovement) {
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
}
