package com.stonetolb.engine.component.render;

import com.stonetolb.engine.Entity;
import com.stonetolb.graphics.Animation;
import com.stonetolb.graphics.NullDrawable;
import com.stonetolb.graphics.StatefulDrawable;

public class AnimationRenderComponent extends RenderComponent {

	StatefulDrawable animation;
	
	public AnimationRenderComponent(String pId, StatefulDrawable pAnimation) {
		super(pId);
		if (pAnimation != null) {
			animation = pAnimation;
		} else {
			//Create a low impact animation
			animation = Animation.builder().addFrame(NullDrawable.INSTANCE, 5000).build();
		}
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
