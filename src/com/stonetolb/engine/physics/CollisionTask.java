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
