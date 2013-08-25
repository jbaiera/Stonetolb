package com.stonetolb.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.stonetolb.engine.component.movement.Rotation;
import com.stonetolb.engine.component.movement.Velocity;
import com.stonetolb.engine.component.render.RenderComponent;
import com.stonetolb.engine.component.render.SpriteControl;
import com.stonetolb.render.Critic;
import com.stonetolb.render.Drawable;
import com.stonetolb.render.StatefulDrawable;

/**
 * System used to change an Entity's visual representation based on 
 * its state.
 * 
 * @author james.baiera
 *
 */
public class SpriteControlSystem extends EntityProcessingSystem implements Critic{
	private @Mapper ComponentMapper<SpriteControl> spriteControlMap;
	private @Mapper ComponentMapper<RenderComponent> renderComponentMap;
	private @Mapper ComponentMapper<Velocity> velocityMap;
	private @Mapper ComponentMapper<Rotation> rotationMap;
	
	@SuppressWarnings("unchecked")
	public SpriteControlSystem() {
		super(Aspect.getAspectForAll(SpriteControl.class, RenderComponent.class, Velocity.class, Rotation.class));
	}

	@Override
	protected void process(Entity arg0) {
		SpriteControl spriteControl = spriteControlMap.get(arg0);
		RenderComponent renderComponent = renderComponentMap.get(arg0);
		Velocity velocity = velocityMap.get(arg0);
		Rotation rotation = rotationMap.get(arg0);
		
		// Get the drawable for the context
		Drawable newDrawable 
			= spriteControl.getDrawable((int)velocity.getVelocity(), 
										(float)rotation.getRotation()
										); 
		
		// If Different :
		if (!newDrawable.equals(renderComponent.getDrawable())) {
			// Analyze it
			newDrawable.accept(this);
		
			// Set it
			renderComponent.setDrawable(newDrawable);
		}
	}

	@Override
	public void analyze(Drawable image) {
		// Do Nothing
	}

	@Override
	public void analyze(StatefulDrawable image) {
		//Dispose and ready the animation for drawing
		image.dispose();
		image.ready();
	}

}
