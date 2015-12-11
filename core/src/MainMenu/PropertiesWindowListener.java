package MainMenu;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

class PropertiesWindowListener extends WindowAdapter
{
	JFrame properties;
	PropertiesWindowListener(JFrame properties)
	{
		this.properties=properties;
	}
	public void windowClosing(WindowEvent e) {properties.dispose();}
    public void windowDeactivated(WindowEvent e) {properties.dispose();}
}
