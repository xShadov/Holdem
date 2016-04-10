package MainMenu;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MyWindowListener extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
};