package com.stonetolb.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.stonetolb.engine.component.position.Position;
import com.stonetolb.engine.component.render.CameraMount;
import com.stonetolb.render.Camera;

public class CameraSystem extends EntityProcessingSystem {
	private @Mapper ComponentMapper<Position> positionMap;
	private @Mapper ComponentMapper<CameraMount> mountMap;
	
	public CameraSystem() {
		super(Aspect.getAspectForAll(Position.class, CameraMount.class));
	}
	
	@Override
	protected void process(Entity arg0) {
		Position pos = positionMap.get(arg0);
		CameraMount mnt = mountMap.get(arg0);
		
		Camera cam = Camera.getCamera();
		
		if (cam.isAttachedTo(mnt)) {
			cam.updatePosition(
					  pos.getX() + mnt.getXOffset()
					, pos.getY() + mnt.getYOffset()
					);
		}
	}

}
