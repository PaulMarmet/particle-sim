import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SimSettings {
	static JFrame storedWindow;
	
	public static void mainPanel(JFrame window) {
		storedWindow = window;
		
		SimConfigPane[] configs = {
				new SimConfigPane("Select the radius for the orbs(above 0): ",
						"DEFAULT_ORB_RADIUS", "float", 0),
				new SimConfigPane("Select the random range for the orbs: ",
						"RADIUS_RANGE", "float", 0),
				new SimConfigPane("Select the force of gravity: ",
						"FORCE_GRAVITY", "float", 0),
				new SimConfigPane("What types of gravity are active: ",
						"GRAVITY_DIRECTION", "int", 0),
				new SimConfigPane("Prevent orbs from going off-screen: ",
						"BOUNDS", 1),
				new SimConfigPane("Select the 'bounce' coefficient(between 0 and 1): ",
						"BOUNCE_COEFFICIENT", "float", 0)
		};
		
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout((3 + (configs.length * 2)), 2));
		myPanel.add(new JLabel("Press space to spawn orbs"));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Press backspace to remove all orbs"));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Press esc to reopen this menu"));
		myPanel.add(new JLabel(""));
		
		for(int i = 0; i < configs.length; i++)
		{
			if(configs[i].type == 0)
				configs[i].addPaneT(myPanel);
			else if(configs[i].type == 1)
				configs[i].addPaneC(myPanel);
		}

		int result = JOptionPane.showConfirmDialog(storedWindow, myPanel, 
				"Please enter new configs", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			for(int i = 0; i < configs.length; i++) {
				if(configs[i].type == 0 && (configs[i].textField.getText() != null && !configs[i].textField.getText().isEmpty()))
				{
					if(configs[i].dataType == "float")
					{Config.set(configs[i].config, Float.parseFloat(configs[i].textField.getText()));}
					else if(configs[i].dataType == "int")
					{Config.set(configs[i].config, Integer.parseInt(configs[i].textField.getText()));}
				}
				if(configs[i].type == 1)
				{
					Config.set(configs[i].config, configs[i].checkBox.isSelected());
				}
			}
		}
		Orb.refresh();
	}
	
}