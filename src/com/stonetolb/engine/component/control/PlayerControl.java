package com.stonetolb.engine.component.control;

import com.artemis.Component;
import com.stonetolb.engine.profiles.WorldProfile.Control;

public class PlayerControl extends Component {
	private Control controls;
	
	public PlayerControl(Control pControls) {
		controls = pControls;
	}
	
	public Control getControls() {
		return controls;
	}
}
