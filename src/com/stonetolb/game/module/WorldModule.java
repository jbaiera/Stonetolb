package com.stonetolb.game.module;

import com.artemis.Entity;
import com.artemis.World;
import com.stonetolb.engine.component.control.PlayerControl;
import com.stonetolb.engine.component.movement.Rotation;
import com.stonetolb.engine.component.movement.Velocity;
import com.stonetolb.engine.component.physics.DynamicBody;
import com.stonetolb.engine.component.physics.StaticBody;
import com.stonetolb.engine.component.position.Position;
import com.stonetolb.engine.component.render.CameraMount;
import com.stonetolb.engine.component.render.RenderComponent;
import com.stonetolb.engine.component.render.SpriteControl;
import com.stonetolb.engine.profiles.WorldProfile;
import com.stonetolb.engine.system.CameraSystem;
import com.stonetolb.engine.system.CollisionSystem;
import com.stonetolb.engine.system.MovementSystem;
import com.stonetolb.engine.system.PlayerControlSystem;
import com.stonetolb.engine.system.RenderSystem;
import com.stonetolb.engine.system.SpriteControlSystem;
import com.stonetolb.graphics.Animation;
import com.stonetolb.graphics.ImageRenderMode;
import com.stonetolb.graphics.NullDrawable;
import com.stonetolb.graphics.Sprite;
import com.stonetolb.graphics.Texture;
import com.stonetolb.graphics.engine.TextureLoader;
import com.stonetolb.render.Camera;
import com.stonetolb.render.FluidVantage;
import com.stonetolb.util.Vector2f;

/**
 * Implementation of an overworld movement game state.
 * Work In Progress
 * @author james.baiera
 *
 */
public class WorldModule implements Module {
	private Texture     sheet;
	private static int 	WIDTH = 32;
	private static int 	HEIGHT = 48;
	private static int  NULLWIDTH = 43;
	private static int  NULLHEIGHT = 29;
	
	private World world;
	
	private Entity newEnt;
	private RenderSystem rs;
	
	/**
	 * {@inheritDoc Module}
	 */
	@Override
	public void init() {

		// Camera setup
		Camera.setVantage(FluidVantage.create(0.05F));
		
		// Load texture resources
		try {
			this.sheet = TextureLoader.getInstance().getTexture("sprites/Vaughn/world/Vaughn.png");
		} catch(Exception e) {
			// TODO : Throw an actual exception
			System.out.println("BAD THINGS HAPPENED");
			e.printStackTrace();
			System.exit(1);
		}
	
		// Gotta make a way to procedurally generate this from a file input...
		// Create the Sprites and Animations first:
		Sprite standingToward = new Sprite(sheet.getSubTexture(0*WIDTH, 0*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING);
		Sprite standingLeft = new Sprite(sheet.getSubTexture(0*WIDTH, 1*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING);
		Sprite standingRight = new Sprite(sheet.getSubTexture(0*WIDTH, 2*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING);
		Sprite standingAway =  new Sprite(sheet.getSubTexture(0*WIDTH, 3*HEIGHT, WIDTH, HEIGHT), ImageRenderMode.STANDING);
		
		Animation.Builder builder = Animation.builder();
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
		
		// World initialization
		world = new World();
		rs = new RenderSystem(800,600);
		world.setSystem(rs, true);
		world.setSystem(new PlayerControlSystem());
		world.setSystem(new MovementSystem());
		world.setSystem(new SpriteControlSystem());
		world.setSystem(new CameraSystem());
		world.setSystem(new CollisionSystem());
		world.initialize();
		
		// Component creation 
		Position positionComponent = new Position(30, 30);
		CameraMount cameraMount = new CameraMount(WIDTH/2.0F, HEIGHT/2.0F);
		RenderComponent renderComponent = new RenderComponent(standingToward);
		SpriteControl spriteControl = new SpriteControl()
				.setNoOp(NullDrawable.getInstance())
				.addAction(
						new WorldProfile.MovementContext(
								WorldProfile.WorldDirection.DOWN.getDirection()
								, WorldProfile.Speed.WALK.getSpeed()
								)
					, toward.clone())
				.addAction(
						new WorldProfile.MovementContext(
								WorldProfile.WorldDirection.UP.getDirection()
								, WorldProfile.Speed.WALK.getSpeed()
								)
					, away.clone())
				.addAction(
						new WorldProfile.MovementContext(
								WorldProfile.WorldDirection.RIGHT.getDirection()
								, WorldProfile.Speed.WALK.getSpeed()
								)
					, right.clone())
				.addAction(
						new WorldProfile.MovementContext(
								WorldProfile.WorldDirection.LEFT.getDirection()
								, WorldProfile.Speed.WALK.getSpeed()
								)
					, left.clone())
				.addAction(
						new WorldProfile.MovementContext(
								WorldProfile.WorldDirection.DOWN.getDirection()
								, WorldProfile.Speed.STOP.getSpeed()
								)
					, standingToward)
				.addAction(
						new WorldProfile.MovementContext(
								WorldProfile.WorldDirection.UP.getDirection()
								, WorldProfile.Speed.STOP.getSpeed()
								)
					, standingAway)
				.addAction(
						new WorldProfile.MovementContext(
								WorldProfile.WorldDirection.RIGHT.getDirection()
								, WorldProfile.Speed.STOP.getSpeed()
								)
					, standingRight)
				.addAction(
						new WorldProfile.MovementContext(
								WorldProfile.WorldDirection.LEFT.getDirection()
								, WorldProfile.Speed.STOP.getSpeed()
								)
					, standingLeft);
		
		newEnt = world.createEntity();
		newEnt.addComponent(positionComponent);
		newEnt.addComponent(renderComponent);
		newEnt.addComponent(new Rotation(WorldProfile.WorldDirection.DOWN.getDirection()));
		newEnt.addComponent(new Velocity(WorldProfile.Speed.STOP.getSpeed()));
		newEnt.addComponent(new PlayerControl(WorldProfile.Control.ARROWS)); //Control profile does not matter. Moving away from that soon...
		newEnt.addComponent(spriteControl);
		newEnt.addComponent(Camera.attachTo(cameraMount));
		newEnt.addComponent(new DynamicBody(30, 30, WIDTH, HEIGHT/2, WIDTH/2, HEIGHT*3/4));
		newEnt.addToWorld();
		
		Camera.getInstance().setPosition(Vector2f.from(positionComponent.getY()+cameraMount.getXOffset(), positionComponent.getY()+cameraMount.getYOffset()+500));
		
		Entity brick = world.createEntity();
		brick.addComponent(new Position(-50, -50));
		brick.addComponent(new RenderComponent(NullDrawable.getInstance()));
		brick.addComponent(new StaticBody(-50, -50, NULLWIDTH, NULLHEIGHT, NULLWIDTH/2, NULLHEIGHT/2));
		brick.addToWorld();
	}

	/**
	 * {@inheritDoc Module}
	 */
	@Override
	public void step(long delta) {
		// Set delta in world object.
		world.setDelta(delta);
		
		// Run system logic over entities
		world.process();
	}

	/**
	 * {@inheritDoc Module}
	 */
	@Override
	public void render(long delta) {
		// Render in separate call
		rs.clearScreen();
		rs.process();
	}
	
	@Override
	public String toString() {
		return "WorldModule";
	}
}
