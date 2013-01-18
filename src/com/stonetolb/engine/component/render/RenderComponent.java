package com.stonetolb.engine.component.render;

import com.artemis.Component;
import com.stonetolb.graphics.Drawable;

public class RenderComponent extends Component {
	private Drawable drawable;
	
	public RenderComponent(Drawable pDrawable) {
		drawable = pDrawable;
	}
	
	public Drawable getDrawable() {
		return drawable;
	}
}
