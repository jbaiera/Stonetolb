package com.stonetolb.engine.component.render;

import com.stonetolb.engine.component.EntityComponent;

public abstract class RenderComponent extends EntityComponent {
	
	public RenderComponent(String pId) {
		id = pId;
	}
	
	public abstract void render(long delta);
}
