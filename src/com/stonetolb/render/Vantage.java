package com.stonetolb.render;

import com.stonetolb.util.Vector2f;

public interface Vantage {

	public void updatePosition(Vector2f target);
	public void setPosition(Vector2f target);
	public void update(long delta);
}
