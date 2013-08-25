package com.stonetolb.engine.component.control;

import com.artemis.Component;
import com.stonetolb.engine.profiles.WorldProfile.Control;

/**
 * Component used to note that an Entity is capable of receiving
 * player input.
 * 
 * @author james.baiera
 * 
 */
public class PlayerControl extends Component {
	private Control controls;
	
	public PlayerControl(Control pControls) {
		controls = pControls;
	}
	
	public Control getControls() {
		return controls;
	}
}
