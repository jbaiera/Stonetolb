package com.stonetolb.engine.physics;

/**
 * Used to signal the engine how a collision resolution
 * was handled. NORMAL is for a standard resolution. 
 * PREVIOUSLY is for a successful resolution that took
 * place already during the update.
 * <p>
 * This is used to keep the engine from infinitely checking
 * if objects have resolved yet.
 * 
 * @author james.baiera
 *
 */
public enum CollisionResolution {
	NORMAL,
	PREVIOUSLY;
}
