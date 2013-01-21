package com.stonetolb.engine.system;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.opengl.GL11;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.stonetolb.Game;
import com.stonetolb.engine.component.position.Position;
import com.stonetolb.engine.component.render.RenderComponent;

public class RenderSystem extends EntityProcessingSystem {
	private final int width;
	private final int height;
	
	private @Mapper ComponentMapper<Position> positionMap;
	private @Mapper ComponentMapper<RenderComponent> renderMap; 
	
	@SuppressWarnings("unchecked")
	public RenderSystem(int pScreenWidth, int pScreenHeight) {
		super(Aspect.getAspectForAll(Position.class, RenderComponent.class));
		width = pScreenWidth;
		height = pScreenHeight;
	}
	
	@Override
	protected void process(Entity arg0) {
		Position position = positionMap.get(arg0);
		RenderComponent render = renderMap.get(arg0);
		
		render.getDrawable().draw((int)position.getX(), (int)position.getY(), 0, (long)world.getDelta());
	}

	@Override
	protected void begin() {
		super.begin();
		
		// clear screen
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		
		// enable textures since we're going to use these for our sprites
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
		
		// Testing some states
		GL11.glClearDepth(1.0);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		glEnable(GL11.GL_ALPHA_TEST);
		glEnable(GL11.GL_CULL_FACE);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glViewport(0, 0, Game.getGame().getWindowWidth(), Game.getGame().getWindowHeight());
	}
	
	
}
