/* 
 * Copyleft (o) 2012 James Baiera
 * All wrongs reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.stonetolb.engine.physics;

import java.awt.Rectangle;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.stonetolb.engine.Entity;

/**
 * PhysicsManager is an object that manages all physical objects in
 * a game world. 
 * @author james.baiera
 * @deprecated Use {@link CollisionSystem} instead
 */
@Deprecated
public class PhysicsManager {
	private Deque<RigidBody> pool;
	private Map<Entity, RigidBody> active;
	private Set<CollisionTask> updated;
	private Set<CollisionTask> currentRound;
	private int managedObjects;
	private CompletionService<Set<CollisionEvent>> collisionService;
	
	private static int DEFAULT_OBJECT_POOL_SIZE = 20;
	private static int DEFAULT_THREAD_POOL_SIZE = 4;
	
	/**
	 * Construct manager with base pool size
	 */
	public PhysicsManager() {
		this(DEFAULT_OBJECT_POOL_SIZE);
	}
	
	/**
	 * Construct manager with given pool size
	 * 
	 * @param pStartingPoolSize
	 */
	public PhysicsManager(int pStartingPoolSize) {
		pool = new LinkedList<RigidBody>();
		active = new HashMap<Entity, RigidBody>();
		updated = new HashSet<CollisionTask>();
		currentRound = new HashSet<CollisionTask>();
		
		if(pStartingPoolSize > 0) {
			managedObjects = pStartingPoolSize;
		}
		else {
			managedObjects = DEFAULT_OBJECT_POOL_SIZE;
		}
		
		fillPool(managedObjects);
		
		ExecutorService exec = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
		collisionService = new ExecutorCompletionService<Set<CollisionEvent>>(exec);
	}
	
	/**
	 * Create the given amount of objects and add them to the
	 * object pool. Only if we're out of objects though.
	 * 
	 * @param pHowMuch
	 */
	private void fillPool(int pHowMuch) {
		if (pool.isEmpty()) {
			for(int count = 0; count < pHowMuch; count++) {
				pool.addLast(new RigidBody());
			}
		}
	}
	
	/**
	 * Submits a request for a {@link RigidBody} object that will be registered to
	 * this manager instance. Collision object is bound to the entity it belongs to
	 * so that in the future, collision messages may be dispatched to notify which
	 * Entities have actually collided.
	 * 
	 * @param pBounds
	 * @param pEvent
	 * @return
	 */
	public boolean requestRigidBody(Entity pKeyEntity, Rectangle pBounds, CollisionEvent pEvent) {
		if(pool.isEmpty()) {
			fillPool(managedObjects);
			managedObjects *= 2;
		}
		
		RigidBody toBeAdded = pool.removeLast();
		toBeAdded.init(pBounds, pEvent);
		active.put(pKeyEntity, toBeAdded);
		
		return true;
	}
	
	/**
	 * Given the key to a {@link RigidBody}, the manager removes it from the active
	 * set of objects and returns it to the object pool. Once this is called, the 
	 * {@link RigidBody} keyed by the given key should no longer be used by any created
	 * pointers to it.
	 * 
	 * @param key
	 */
	public void purgeRigidBody(Entity pKey) {
		RigidBody purged = active.remove(pKey);
		if(purged != null) {
			purged.clear();
			pool.addLast(purged);
		}
	}
	
	/**
	 * Given a key to a {@link RigidBody}, the world updates it's position and makes note
	 * that it has changed position recently
	 * 
	 * @param key
	 * @param x
	 * @param y
	 */
	public void updateObjectPosition(Entity key, int x, int y) {
		if (key != null) {
			RigidBody toUpdate = active.get(key);
			if (toUpdate != null) {
				toUpdate.updatePosition(x, y);
				updated.add(new CollisionTask(key, active));
			}
		}
	}
	
	/**
	 * Will run the update method until all collisions have been resolved.
	 * 
	 */
	public void resolveAllCollisions() {
		//Wait until all collisions have been resolved
		while(step() > 0);
		
		//Clear the update list
		updated.clear();
	}
	
	public void update() {
		step();
		updated.clear();
	}
	
	/**
	 * Launches a Collision Task for each updated RigidBody this round.
	 * 
	 * @return Number of collision events dispatched during the update<br>
	 * -1 on engine error<br>
	 * If non zero, there may be unresolved collisions
	 */
	private int step() {
		try {
			Set<CollisionEvent> collisions = new HashSet<CollisionEvent>();
			currentRound.addAll(updated);
			int numberOfNormalResolutions = 0;
			
			//Submit tasks
			for(CollisionTask task : currentRound) {
				collisionService.submit(task);
			}
			
			//Aggregate task returns
			for(int taskIndex = 0; taskIndex < currentRound.size(); ++taskIndex) {
				collisions.addAll(collisionService.take().get());
			}
			
			//Dispatch Collision Events
			for(CollisionEvent eachEvent : collisions) {
				//Call and check resolution
				if(eachEvent.onCollision() == CollisionResolution.NORMAL) {
					++numberOfNormalResolutions;
				}
			}
			
			return numberOfNormalResolutions;
			
		} catch (InterruptedException ie) {
			Logger logger = Logger.getLogger("PHYSICS");
			logger.severe("Physics Manager interrupted during collision analysis step");
		} catch (ExecutionException ee) {
			Logger logger = Logger.getLogger("PHYSICS");
			logger.severe("Error occured in collision task object");
		} finally {
			currentRound.clear();
		}
		
		return -1;
	}
	
}
