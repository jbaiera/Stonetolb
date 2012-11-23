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

package com.stonetolb.engine;

import java.util.HashMap;
import java.util.Map;

import com.stonetolb.engine.component.EntityComponent;
import com.stonetolb.engine.component.render.RenderComponent;
import com.stonetolb.util.Pair;

/**
 * Main object of the Entity Engine. Entities are objects that 
 * contain a physical state in the game engine. 
 * <p>
 * They are composed of different concrete {@link EntityComponent} 
 * that change it's behavior. They characteristically have only one 
 * {@link RenderComponent} which defines how they are rendered.
 * 
 * @author comet
 *
 */
public class Entity {
	private String id;
	
//	protected float xPosition;
//	protected float yPosition;
	protected Pair<Float, Float> position;
	protected int speed;
	protected float direction;
	
	protected RenderComponent renderComponent;
	
	protected Map<String,EntityComponent> components;
	
	public Entity(String pId) {
		id = pId;
		
		position = new Pair<Float, Float>(0.0f, 0.0f);
		speed = 0;
		direction = 0;
		
		components = new HashMap<String,EntityComponent>();
	}
	
	public void addComponent(EntityComponent pNewComponent) {
		if (RenderComponent.class.isInstance(pNewComponent)) {
			renderComponent = (RenderComponent) pNewComponent;
		}
		
		pNewComponent.setOwner(this);
		components.put(pNewComponent.getId(), pNewComponent);
	}
	
	public String getId() {
		return id;
	}
	
	public EntityComponent getComponent(String pId) {
		return components.get(pId);
	}
	
	public Pair<Float, Float> getPosition() {
		return position;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public float getDirection() {
		return direction;
	}
	
	public void setPosition(Pair<Float, Float> pPosition) {
		position = pPosition;
	}
	
	public void setSpeed(int pSpeed) {
		speed = pSpeed;
	}
	
	public void setDirection(float pDirection) {
		direction = pDirection;
	}
	
	/**
	 * Called to update the Entity Object's state based on
	 * the components the entity is built out of.
	 * 
	 * @param delta
	 */
	public void update(long delta) {
		EntityComponent current;
		for(String key : components.keySet()) {
			current = components.get(key);
			if (current != null) {
				current.update(delta);
			}
		}
	}
	
	public void render(long delta) {
		if (renderComponent != null) {
			renderComponent.render(delta);
		}
	}
}
