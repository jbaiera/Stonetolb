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

package com.stonetolb.game.module;


import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;

import com.artemis.Entity;
import com.artemis.World;
import com.stonetolb.engine.component.control.KeyboardControlComponent;
import com.stonetolb.engine.component.control.PlayerControl;
import com.stonetolb.engine.component.movement.OverworldMovementComponent;
import com.stonetolb.engine.component.movement.Rotation;
import com.stonetolb.engine.component.movement.Velocity;
import com.stonetolb.engine.component.physics.CollisionComponent;
import com.stonetolb.engine.component.position.Position;
import com.stonetolb.engine.component.render.AnimationRenderComponent;
import com.stonetolb.engine.component.render.ImageRenderComponent;
import com.stonetolb.engine.component.render.OverworldActorComponent;
import com.stonetolb.engine.component.render.RenderComponent;
import com.stonetolb.engine.physics.PhysicsManager;
import com.stonetolb.engine.profiles.WorldProfile;
import com.stonetolb.engine.system.MovementSystem;
import com.stonetolb.engine.system.PlayerControlSystem;
import com.stonetolb.engine.system.RenderSystem;
import com.stonetolb.graphics.Animation;
import com.stonetolb.graphics.Animation.AnimationBuilder;
import com.stonetolb.graphics.ImageRenderMode;
import com.stonetolb.graphics.Sprite;
import com.stonetolb.graphics.Texture;
import com.stonetolb.graphics.engine.TextureLoader;
import com.stonetolb.render.Camera;
import com.stonetolb.render.entities.Actor;
import com.stonetolb.util.Pair;

/**
 * Implementation of an overworld movement game state
 * 
 * @author james.baiera
 *
 */
public class WorldModule implements Module {
	private Actor       vaughn;
	
	private Texture     sheet;
	private static int 	WIDTH = 32;
	private static int 	HEIGHT = 48;
	private static int  NULLWIDTH = 43;
	private static int  NULLHEIGHT = 29;
	
	private PhysicsManager physicsWorld;
	
	private com.stonetolb.engine.Entity vaughnTwo;
	private com.stonetolb.engine.Entity anchor;
	private com.stonetolb.engine.Entity origin;
	
	private World world;
	
	private Entity newEnt;
	private RenderSystem rs;
	
	@Override
	public void init() {
		// Create an old style actor:
		try {
			this.sheet = TextureLoader.getInstance().getTexture("sprites/Vaughn/world/Vaughn.png");
		} catch(Exception e) {
			System.out.println("BAD THINGS HAPPENED");
			e.printStackTrace();
			System.exit(1);
		}
		
		physicsWorld = new PhysicsManager();
		
		vaughn = new Actor(200,100);
		int walkInterval = 800;
		
		OverworldActorComponent vaughnRender = new OverworldActorComponent("TestComponent");
		
		// Gotta make a way to procedurally generate this from a file input...
		
		//Create the Animations and Sprites first:
		Sprite standingToward = new Sprite(sheet.getSubTexture(0*WIDTH, 0*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING);
		Sprite standingLeft = new Sprite(sheet.getSubTexture(0*WIDTH, 1*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING);
		Sprite standingRight = new Sprite(sheet.getSubTexture(0*WIDTH, 2*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING);
		Sprite standingAway =  new Sprite(sheet.getSubTexture(0*WIDTH, 3*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING);
		
		AnimationBuilder builder = Animation.builder();
		Animation toward = builder
				.addFrame(new Sprite(sheet.getSubTexture(1*WIDTH, 0*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(new Sprite(sheet.getSubTexture(2*WIDTH, 0*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(new Sprite(sheet.getSubTexture(3*WIDTH, 0*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(standingToward, 175)
				.build();
		Animation left = builder
				.addFrame(new Sprite(sheet.getSubTexture(1*WIDTH, 1*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(new Sprite(sheet.getSubTexture(2*WIDTH, 1*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(new Sprite(sheet.getSubTexture(3*WIDTH, 1*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(standingLeft, 175)
				.build();
		Animation right = builder
				.addFrame(new Sprite(sheet.getSubTexture(1*WIDTH, 2*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(new Sprite(sheet.getSubTexture(2*WIDTH, 2*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(new Sprite(sheet.getSubTexture(3*WIDTH, 2*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(standingRight, 175)
				.build();
		Animation away = builder
				.addFrame(new Sprite(sheet.getSubTexture(1*WIDTH, 3*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(new Sprite(sheet.getSubTexture(2*WIDTH, 3*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(new Sprite(sheet.getSubTexture(3*WIDTH, 3*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING), 175)
				.addFrame(standingAway, 175)
				.build();
		
		//Construct the Old Actor
		//Clone the Animations since they'll be copied across the other entities
		vaughn.addAction("toward", toward.clone());
		vaughn.addAction("left", left.clone());
		vaughn.addAction("right", right.clone());
		vaughn.addAction("away", away.clone());
		vaughn.addAction("standingtoward", standingToward);
		vaughn.addAction("standingleft", standingLeft);
		vaughn.addAction("standingright", standingRight);
		vaughn.addAction("standingaway", standingAway);
		vaughn.setAction("standingtoward");
		vaughn.setVerticalMovement(0);
		vaughn.setHorizontalMovement(0);
		
		// Construct the New Entity's rendering objects
		vaughnRender.addAction(
				new WorldProfile.MovementContext(
						  WorldProfile.WorldDirection.DOWN.getDirection()
						, WorldProfile.Speed.WALK.getSpeed()
					)
				, new AnimationRenderComponent("toward", toward.clone())
			);
		
		
		vaughnRender.addAction(
				new WorldProfile.MovementContext(
						  WorldProfile.WorldDirection.LEFT.getDirection()
						, WorldProfile.Speed.WALK.getSpeed()
					)
				, new AnimationRenderComponent("left", left.clone())
			);
		
		
		vaughnRender.addAction(
				new WorldProfile.MovementContext(
						  WorldProfile.WorldDirection.RIGHT.getDirection()
						, WorldProfile.Speed.WALK.getSpeed()
					)
				, new AnimationRenderComponent("right", right.clone())
			);
		
		
		vaughnRender.addAction(
				new WorldProfile.MovementContext(
						  WorldProfile.WorldDirection.UP.getDirection()
						, WorldProfile.Speed.WALK.getSpeed()
					)
				, new AnimationRenderComponent("away", away.clone())
			);
		
		vaughnRender.addAction(
				new WorldProfile.MovementContext(
						  WorldProfile.WorldDirection.DOWN.getDirection()
						, WorldProfile.Speed.STOP.getSpeed()
					)
				, new ImageRenderComponent("standingtoward", standingToward)
			);
		
		vaughnRender.addAction(
				new WorldProfile.MovementContext(
						  WorldProfile.WorldDirection.LEFT.getDirection()
						, WorldProfile.Speed.STOP.getSpeed()
					)
				, new ImageRenderComponent("standingleft", standingLeft)
			);
		
		vaughnRender.addAction(
				new WorldProfile.MovementContext(
						  WorldProfile.WorldDirection.RIGHT.getDirection()
						, WorldProfile.Speed.STOP.getSpeed()
					)
				, new ImageRenderComponent("standingright", standingRight)
			);
		
		vaughnRender.addAction(
				new WorldProfile.MovementContext(
						  WorldProfile.WorldDirection.UP.getDirection()
						, WorldProfile.Speed.STOP.getSpeed()
					)
				, new ImageRenderComponent("standingaway", standingAway)
			);
		
		// null image entity that acts as the camera anchor
		anchor = new com.stonetolb.engine.Entity("Anchor");
		anchor.addComponent(new ImageRenderComponent("Nothing", null));
		anchor.addComponent(new KeyboardControlComponent("WASD", WorldProfile.Control.WASD));
		anchor.addComponent(new OverworldMovementComponent("Complex"));
		anchor.setPosition(new Pair<Float,Float>(150F, 150F));
		
		Camera.getCamera().setParent(anchor);
		
		// entity to sit right at 0,0 of the game world
		origin = new com.stonetolb.engine.Entity("Origin");
		origin.addComponent(new ImageRenderComponent("Nothing", null));
		origin.addComponent(
				new CollisionComponent("Basic Bounds"
						, new Rectangle(0,0,NULLWIDTH,NULLHEIGHT)
						, physicsWorld
						)
				);
		origin.setPosition(new Pair<Float, Float>(0F,0F));
		
		// entity that is parented to the world
		vaughnTwo = new com.stonetolb.engine.Entity("Second Vaughn", origin);
		vaughnTwo.addComponent(new KeyboardControlComponent("Arrows", WorldProfile.Control.ARROWS));
		vaughnTwo.addComponent(
				new OverworldMovementComponent("Basic"));
		vaughnTwo.addComponent(vaughnRender);
		vaughnTwo.addComponent(
				new CollisionComponent("Half Bounds"
						, new Rectangle(0,HEIGHT/2,WIDTH, HEIGHT/2)
						, physicsWorld
						)
				);
		vaughnTwo.setPosition(new Pair<Float,Float>(150F,0F));
		
		//Artemis integration
		world = new World();
		rs = new RenderSystem();
		world.setSystem(rs, true);
		world.setSystem(new PlayerControlSystem());
		world.setSystem(new MovementSystem());
		world.initialize();
		
		newEnt = world.createEntity();
		newEnt.addComponent(new Position(30, 30));
		newEnt.addComponent(new RenderComponent(standingToward));
		newEnt.addComponent(new Rotation(WorldProfile.WorldDirection.DOWN.getDirection()));
		newEnt.addComponent(new Velocity(WorldProfile.Speed.STOP.getSpeed()));
		newEnt.addComponent(new PlayerControl(WorldProfile.Control.ARROWS)); //Control profile does not matter. Moving away from that soon...
		newEnt.addToWorld();
	}

	@Override
	public void step(long delta) {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			vaughn.setVerticalMovement(-75);
			vaughn.setHorizontalMovement(0);
			vaughn.setAction("away");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			vaughn.setVerticalMovement(75);
			vaughn.setHorizontalMovement(0);
			vaughn.setAction("toward");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(-75);
			vaughn.setAction("left");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(75);
			vaughn.setAction("right");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(0);
			vaughn.setAction(Actor.EMPTY);
		} else {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(0);
			if(vaughn.getDrawingName() == "toward") {
				vaughn.setAction("standingtoward");
			} else if (vaughn.getDrawingName() == "away") {
				vaughn.setAction("standingaway");
			} else if (vaughn.getDrawingName() == "left") {
				vaughn.setAction("standingleft");
			} else if (vaughn.getDrawingName() == "right") {
				vaughn.setAction("standingright");
			}
		}
		
		//Process Entities
		vaughn.move(delta);
		origin.update(delta);
		anchor.update(delta);
		vaughnTwo.update(delta);
		
		//Process collisions
		physicsWorld.resolveAllCollisions();
		
		world.setDelta(delta);
		world.process();
	}

	@Override
	public void render(long delta) {
		//Render Entites
		vaughn.render(delta);
		origin.render(delta);
		anchor.render(delta);
		vaughnTwo.render(delta);
		
		rs.process();
	}
}
