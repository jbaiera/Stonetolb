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
 * <p>
 * Entities may have parent Entities. This creates a locational dependancy only.
 * Entities will continue to display their coordinates as a displacement from the origin, 
 * but instead of using the true origin of the screen, Entities that have parents will 
 * treat their parent's position as their origin. This leads to two possible Entity locations: <br><br>
 * 1. Relative Location : The Entity's relative distance from it's origin <br>
 * 2. Absolute Location : The Entity's distance from the world origin <br>
 * <p>
 * Care should be taken when using an Entity's location. Relative Location is Read - Write, but
 * Absolute Location is Read Only. Entities should only ever update their Relative Location.
 * 
 * 
 * @author comet
 *
 */
public class Entity {
	private String id;
	
	protected Pair<Float, Float> position;
	protected Pair<Float, Float> absolute;
	protected float direction;
	protected int speed;
	protected Entity sceneParent;
	
	protected RenderComponent renderComponent;
	
	protected Map<String,EntityComponent> components;
	
	/**
	 * Creates Entity with specified Parent Entity
	 * 
	 * @param pId Identifier for Entity Object. Does not need to be unique, but it is advised
	 * @param pSceneParent Parent of the Entity Object.
	 */
	public Entity(String pId, Entity pSceneParent) {
		id = pId;
		
		position = new Pair<Float, Float>(0.0f, 0.0f);
		direction = 0;
		speed = 0;
		
		sceneParent = pSceneParent;
		
		updateAbsoluteLocation();
		
		components = new HashMap<String,EntityComponent>();
	}
	
	/**
	 * Creates Entity with no Parent
	 * 
	 * @param pId
	 */
	public Entity(String pId)
	{
		this(pId, null);
	}
	
	/**
	 * Adds the given component to the Entity. Due to the underlying implementation,
	 * The order that the objects have been added in has no bearing on which order
	 * they are updated.
	 * 
	 * @param pNewComponent
	 */
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
	
	public Pair<Float, Float> getAbsolute() {
		return absolute;
	}
	
	public float getDirection() {
		return direction;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setPosition(Pair<Float, Float> pPosition) {
		position = pPosition;
		updateAbsoluteLocation();
	}
	
	public void setDirection(float pDirection) {
		direction = pDirection;
	}
	
	public void setSpeed(int pSpeed) {
		speed = pSpeed;
	}
	
	public Entity getParent() {
		return sceneParent;
	}
	
	/**
	 * Called to update the Entity Object's state based on
	 * the components the entity is built out of.
	 * 
	 * @param delta
	 */
	public void update(long delta) {
		updateAbsoluteLocation();
		EntityComponent current;
		for(String key : components.keySet()) {
			current = components.get(key);
			if (current != null) {
				current.update(delta);
			}
		}
	}
	
	/**
	 * Renders the Entity based off of the environment data
	 * 
	 * @param delta
	 */
	public void render(long delta) {
		updateAbsoluteLocation();
		if (renderComponent != null) {
			renderComponent.render(delta);
		}
	}
	
	private void updateAbsoluteLocation() {
		// Get the Parent's absolute location and add your own position to it
		if (sceneParent != null) {
			Pair<Float, Float> parentLocation = sceneParent.getAbsolute();
			absolute.x = parentLocation.x + position.x;
			absolute.y = parentLocation.y + position.y;
		}
		else 
		{
			absolute = position;
		}
	}
}
