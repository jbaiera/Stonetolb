package com.stonetolb.engine.component.render;

import com.stonetolb.engine.Entity;
import com.stonetolb.graphics.StatefulDrawable;

public class AnimationRenderComponent extends RenderComponent {

	StatefulDrawable animation;
	
	public AnimationRenderComponent(String pId, StatefulDrawable pAnimation) {
		super(pId);
		animation = pAnimation;
	}
	
	@Override
	public void render(long delta) {
		animation.draw(
				  (int)parent.getAbsolute().x.floatValue()
				, (int)parent.getAbsolute().y.floatValue()
				, 0
				, delta
			);
	}

	@Override
	public void update(long delta) {
		//nothing
	}

	@Override
	public void setOwner(Entity pParent)
	{
		parent = pParent;
		animation.dispose();
		animation.ready();
	}
}
