package com.stonetolb.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.stonetolb.engine.component.position.Position;
import com.stonetolb.engine.component.render.CameraMount;
import com.stonetolb.render.Camera;
import com.stonetolb.util.Vector2f;

/**
 * System that handles Camera movement for any Entity with the Camera
 * attached to its CameraMount
 * 
 * @author james.baiera
 *
 */
public class CameraSystem extends EntityProcessingSystem {
	private @Mapper ComponentMapper<Position> positionMap;
	private @Mapper ComponentMapper<CameraMount> mountMap;
	
	@SuppressWarnings("unchecked")
	public CameraSystem() {
		super(Aspect.getAspectForAll(Position.class, CameraMount.class));
	}
	
	@Override
	protected void begin() {
		// TODO Auto-generated method stub
		super.begin();
		Camera.getInstance().updatePosition(Vector2f.NULL_VECTOR);
	}
	
	@Override
	protected void process(Entity arg0) {
		Position pos = positionMap.get(arg0);
		CameraMount mnt = mountMap.get(arg0);
		
		if (Camera.isAttachedTo(mnt)) {
			Camera.getInstance().updatePosition(
					Vector2f.from(
						  pos.getX() + mnt.getXOffset()
						, pos.getY() + mnt.getYOffset()
						)
					);
		}
	}
}
