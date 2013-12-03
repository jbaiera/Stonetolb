package com.stonetolb.game.module.combat.components;

import com.artemis.Component;

/**
 * Component that denotes an actor's vital stats.
 */
public class Vitals extends Component {

	private int maxHealth;
	private int maxMana;
	private int maxStamina;

	private int currentHealth;
	private int currentMana;
	private int currentStamina;

	public Vitals(int maxH, int maxM, int maxS, int curH, int curM, int curS) {
		maxHealth = maxH;
		maxMana = maxM;
		maxStamina = maxS;
		currentHealth = curH;
		currentMana = curM;
		currentStamina = curS;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public void setCurrentMana(int currentMana) {
		this.currentMana = currentMana;
	}

	public void setCurrentStamina(int currentStamina) {
		this.currentStamina = currentStamina;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getCurrentMana() {
		return currentMana;
	}

	public int getCurrentStamina() {
		return currentStamina;
	}
}
