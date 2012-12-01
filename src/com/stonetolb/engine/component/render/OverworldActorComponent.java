package com.stonetolb.engine.component.render;

import java.util.HashMap;
import java.util.Map;

import com.stonetolb.graphics.NullDrawable;

public class OverworldActorComponent extends RenderComponent {

	public OverworldActorComponent(String pId){
		super(pId);
		actionMapping = new HashMap<MovementContext, RenderComponent>();
		noOpAction = new ImageRenderComponent("Drawing Missing", new NullDrawable());
		currentAction = noOpAction;
	}
	
	/**
	 * Used to key animations for movements based on the entity's state
	 * 
	 * @author james.baiera
	 *
	 */
	public static class MovementContext
	{
		private float direction;
		private boolean inMotion;
		
		public MovementContext(float pDirection, boolean pInMotion)
		{
			direction = pDirection;
			inMotion = pInMotion;
		}
		
		@Override
		public int hashCode()
		{
			int prime = 31;
			int result = 17;
			result = prime * result + Float.floatToIntBits(direction);
			result = prime * result + (inMotion ? 1 : 0);
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
						&& inMotion == other.inMotion;
			}
			else 
			{
				return false;
			}
		}
	}
	
	private Map<MovementContext,RenderComponent> actionMapping;
	private RenderComponent currentAction;
	private RenderComponent noOpAction;
	
	public void addAction(MovementContext pMovement, RenderComponent pComponent)
	{
		actionMapping.put(pMovement, pComponent);
	}
	
	@Override
	public void render(long delta) {
		currentAction.render(delta);
	}

	@Override
	public void update(long delta) {
		MovementContext context = new MovementContext(parent.getDirection(), (parent.getSpeed() != 0 ? true : false));
		currentAction = actionMapping.get(context);
		if(currentAction == null)
		{
			currentAction = noOpAction;
		}
		currentAction.setOwner(parent);
	}

}
