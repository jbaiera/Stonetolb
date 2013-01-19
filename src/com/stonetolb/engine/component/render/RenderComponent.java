package com.stonetolb.engine.component.render;

import com.artemis.Component;
import com.stonetolb.graphics.Drawable;
import com.stonetolb.graphics.NullDrawable;

public class RenderComponent extends Component {
	private Drawable drawable;
	
	public RenderComponent(Drawable pDrawable) {
		setDrawable(pDrawable);
	}
	
	public Drawable getDrawable() {
		return drawable;
	}
	
	public void setDrawable(Drawable pDrawable) {
		drawable = pDrawable == null ? NullDrawable.getInstance() : pDrawable;
	}
}
