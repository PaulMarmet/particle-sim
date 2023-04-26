import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SimSettings {
	static JFrame storedWindow;
	
	public static void panel(JFrame window) {
		storedWindow = window;
		
		JTextField radField = new JTextField(5);
		JTextField gravField = new JTextField(5);
		JTextField coEffField = new JTextField(5);
		
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(9, 2));
		myPanel.add(new JLabel("Press space to spawn orbs"));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Press backspace to remove all orbs"));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Press esc to reopen this menu"));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Select the radius for the orbs(above 0): "));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Current: "+Config.getF("DEFAULT_ORB_RADIUS")));
		myPanel.add(radField);
		myPanel.add(new JLabel("Select the force of gravity: "));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Current: "+Config.getF("FORCE_GRAVITY")));
		myPanel.add(gravField);
		myPanel.add(new JLabel("Select the 'bounce' coefficient(between 0 and 1): "));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Current: "+Config.getF("BOUNCE_COEFFICIENT")));
		myPanel.add(coEffField);
		

		int result = JOptionPane.showConfirmDialog(storedWindow, myPanel, 
				"Please enter new configs", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
  			if(radField.getText() != null && !radField.getText().isEmpty()) {
  				Config.set("DEFAULT_ORB_RADIUS", Float.parseFloat(radField.getText()));
  			}
  			if(gravField.getText() != null && !gravField.getText().isEmpty()) {
  				Config.set("FORCE_GRAVITY", Float.parseFloat(gravField.getText()));
  			}
  			if(coEffField.getText() != null && !coEffField.getText().isEmpty()) {
  				Config.set("BOUNCE_COEFFICIENT", Float.parseFloat(coEffField.getText()));
  			}
		}
	}
	
	public JTextField newConfig(JPanel panel)
	{
		
		return null;
	}
}