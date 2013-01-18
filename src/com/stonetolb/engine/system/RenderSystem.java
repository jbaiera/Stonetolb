package com.stonetolb.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.stonetolb.engine.component.position.Position;
import com.stonetolb.engine.component.render.RenderComponent;

public class RenderSystem extends EntityProcessingSystem {

	private @Mapper ComponentMapper<Position> positionMap;
	private @Mapper ComponentMapper<RenderComponent> renderMap; 
	
	public RenderSystem() {
		super(Aspect.getAspectForAll(Position.class, RenderComponent.class));
	}
	
	@Override
	protected void process(Entity arg0) {
		Position position = positionMap.get(arg0);
		RenderComponent render = renderMap.get(arg0);
		
		render.getDrawable().draw((int)position.getX(), (int)position.getY(), 0, (long)world.getDelta());
	}

}
