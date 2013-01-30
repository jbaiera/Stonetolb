package com.stonetolb.engine.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.stonetolb.engine.component.movement.Velocity;
import com.stonetolb.engine.component.physics.Body;
import com.stonetolb.engine.component.physics.DynamicBody;
import com.stonetolb.engine.component.physics.KinematicBody;
import com.stonetolb.engine.component.physics.StaticBody;
import com.stonetolb.engine.component.position.Position;
import com.stonetolb.util.Vector2f;

public class CollisionSystem extends EntityProcessingSystem {

	private Map<Entity, Body> actives;
	
	private @Mapper ComponentMapper<Position> positionMapper;
	private @Mapper ComponentMapper<StaticBody> staticMapper;
	private @Mapper ComponentMapper<KinematicBody> kinematicMapper;
	private @Mapper ComponentMapper<DynamicBody> dynamicMapper;
	private @Mapper ComponentMapper<Velocity> velocityMapper;
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public CollisionSystem() {
		super(Aspect.getAspectForAll(Position.class).getAspectForOne(StaticBody.class, KinematicBody.class, DynamicBody.class));
		actives = new HashMap<Entity, Body>();
	}
	
	@Override
	protected void begin() {
		super.begin();
		
		//Updated all physics objects to their current positions (O(n))
		for(Entry<Entity, Body> eachEntry : actives.entrySet()) {
			Position pos = positionMapper.get(eachEntry.getKey());
			Vector2f delta = Vector2f.from(pos.getX(), pos.getY())
					.add(eachEntry.getValue().getOffset())
					.sub(eachEntry.getValue().getAABB().getPosition());
			eachEntry.getValue().transform(delta);
		}
	}
	
	@Override
	protected void process(Entity currentEntity) {
		
		if(staticMapper.getSafe(currentEntity) != null) { 
			//Skip this entity if it's collision component is static. It does not care about hitting other things
			return;
		}
		
		Body thisBody = actives.get(currentEntity);
		
		for(Entry<Entity, Body> other : actives.entrySet()) {
			if(other.getKey().equals(currentEntity)) {
				continue;
			}
			else 
			{
				Entity otherEntity = other.getKey();
				Body otherBody = other.getValue();
				Vector2f overlapVector = thisBody.getAABB().getOverlapVector(otherBody.getAABB());
				if(!overlapVector.equals(Vector2f.NULL_VECTOR)) {
					
					if(thisBody.getMass() < otherBody.getMass()) { 
						// This object is smaller and therefore moves						
						resolveCollision(currentEntity, thisBody, otherBody, overlapVector);
						
					} else if (thisBody.getMass() > otherBody.getMass()) { 
						// This object is bigger and therefore moves the other
						resolveCollision(otherEntity, otherBody, thisBody, overlapVector);
						
					} else if (thisBody.getMass() == otherBody.getMass()){
						// These objects are the same mass. Tie Break
						// Check for Movement
						Velocity currentVelocity = velocityMapper.getSafe(currentEntity);
						Velocity otherVelocity = velocityMapper.getSafe(otherEntity);
						boolean currentMoving = ((currentVelocity != null) && (currentVelocity.getVelocity() == 0F));
						boolean otherMoving = ((otherVelocity != null) && (otherVelocity.getVelocity() == 0F));
						
						// If one is moving and the other is not, fix the moving one.
						if(currentMoving && !otherMoving) {
							resolveCollision(currentEntity, thisBody, otherBody, overlapVector);
						} else if (!currentMoving && otherMoving) {
							resolveCollision(otherEntity, otherBody, thisBody, overlapVector);
						} else if ((currentMoving && otherMoving) || (!currentMoving && !otherMoving)) {
							// If both are still or both are moving, bounce apart on half their resolution vectors.
							resolveBounceCollision(currentEntity, thisBody, otherEntity, otherBody, overlapVector);
						}
					}
				}
			}
		}
	}
	
	private void resolveCollision(Entity entityToMove, Body bodyToMove, Body bodyThatPushes, Vector2f overlap) {
		Vector2f resolutionVector = bodyToMove.getAABB().getCollisionResolutionVector(bodyThatPushes.getAABB(), overlap);
		Vector2f newPositionOfBody = bodyToMove.transform(resolutionVector); //BODY UPDATED
		Vector2f newPositionOfPositionComponent = newPositionOfBody.sub(bodyToMove.getOffset());

		Position position = positionMapper.get(entityToMove);
		position.setPosition(newPositionOfPositionComponent.getX(), newPositionOfPositionComponent.getY()); //POSITION UPDATED
	}
	
	private void resolveBounceCollision(Entity firstEntity, Body firstBody, Entity secondEntity, Body secondBody, Vector2f overlap) {
		Vector2f firstResolutionVector = firstBody.getAABB().getCollisionResolutionVector(secondBody.getAABB(), overlap);
		Vector2f secondResolutionVector = secondBody.getAABB().getCollisionResolutionVector(firstBody.getAABB(), overlap);
		
		// Transform by 1/2 their resolution vectors
		Vector2f newPositionOfFirstBody = firstBody.transform(Vector2f.from(firstResolutionVector.getX()/2F, firstResolutionVector.getY()/2F));
		Vector2f newPositionOfSecondBody = secondBody.transform(Vector2f.from(secondResolutionVector.getX()/2F, secondResolutionVector.getY()/2F));
		
		Vector2f newPositionOfFirstPositionComponent = newPositionOfFirstBody.sub(firstBody.getOffset());
		Vector2f newPositionOfSecondPositionComponent = newPositionOfSecondBody.sub(secondBody.getOffset());
		
		Position firstPosition = positionMapper.get(firstEntity);
		Position secondPosition = positionMapper.get(secondEntity);
		
		firstPosition.setPosition(newPositionOfFirstPositionComponent.getX(), newPositionOfFirstPositionComponent.getY());
		secondPosition.setPosition(newPositionOfSecondPositionComponent.getX(), newPositionOfSecondPositionComponent.getY());
	}
	
	@Override
	protected void inserted(Entity e) {
		super.inserted(e);
		
		Body physicalBody;
		if (staticMapper.getSafe(e) != null) {
			physicalBody = staticMapper.get(e); 
		}
		else if (kinematicMapper.getSafe(e) != null)
		{
			physicalBody = kinematicMapper.get(e);
		}
		else if (dynamicMapper.getSafe(e) != null)
		{
			physicalBody = dynamicMapper.get(e);
		}
		else
		{
			/* Wait, how did you do that? */ 
			throw new IllegalStateException("Entity added to Physics System but has no Body Component : " + e);
		}
		
		actives.put(e, physicalBody);
	}
	
	@Override
	protected void removed(Entity e) {
		super.removed(e);
		actives.remove(e);
	}
}
