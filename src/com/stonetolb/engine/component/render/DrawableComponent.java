package com.stonetolb.engine.component.render;

import com.artemis.Component;
import com.stonetolb.graphics.Drawable;

public class DrawableComponent extends Component {
	private Drawable drawable;
	
	public DrawableComponent(Drawable pDrawable) {
		drawable = pDrawable;
	}
	
	public Drawable getDrawable() {
		return drawable;
	}
}
