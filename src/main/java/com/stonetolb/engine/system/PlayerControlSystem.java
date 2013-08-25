package com.stonetolb.engine.system;

import org.lwjgl.input.Keyboard;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.stonetolb.engine.component.control.PlayerControl;
import com.stonetolb.engine.component.movement.Rotation;
import com.stonetolb.engine.component.movement.Velocity;

/**
 * System used to listen for input and modify an Entity's state accordingly
 *  
 * @author james.baiera
 *
 */
public class PlayerControlSystem extends EntityProcessingSystem {
	private @Mapper ComponentMapper<Velocity> velocityMap;
	private @Mapper ComponentMapper<Rotation> rotationMap;
	
	private static double up = 270D;
	private static double down = 90D;
	private static double right = 0D;
	private static double left = 180D;
	private static float walk = 75f;
	
	@SuppressWarnings("unchecked")
	public PlayerControlSystem() {
		super(Aspect.getAspectForAll(PlayerControl.class, Velocity.class, Rotation.class));
	}
	
	@Override
	protected void process(Entity arg0) {
		Velocity vel = velocityMap.get(arg0);
		Rotation rot = rotationMap.get(arg0);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			rot.setRotation(up);
			vel.setVelocity(walk);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			rot.setRotation(down);
			vel.setVelocity(walk);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			rot.setRotation(left);
			vel.setVelocity(walk);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			rot.setRotation(right);
			vel.setVelocity(walk);
		} else {
			vel.setVelocity(0);
		}
	}

}
