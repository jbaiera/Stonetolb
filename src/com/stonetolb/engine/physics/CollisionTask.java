package com.stonetolb.engine.physics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import com.stonetolb.engine.Entity;

class CollisionTask implements Callable<Set<CollisionEvent>> {
	
	private Entity objectKey;
	private Map<Entity,RigidBody> world;
	
	public CollisionTask(Entity pObjectKey, Map<Entity,RigidBody> pWorld) {
		objectKey = pObjectKey;
		world = Collections.synchronizedMap(pWorld);
	}
	
	@Override
	public Set<CollisionEvent> call() throws Exception {
		Set<CollisionEvent> collisions = new HashSet<CollisionEvent>();
		
		for(Entity other : Collections.synchronizedSet(world.keySet())) {
			if(!objectKey.equals(other)) {
				CollisionEvent event = world.get(objectKey).collidesWith(world.get(other));
				
				if (event != null) {
					collisions.add(event);
				}
			}
		}
		
		return collisions;
	}

}
