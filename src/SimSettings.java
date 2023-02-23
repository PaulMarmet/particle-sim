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
		myPanel.add(new JLabel("Current: "+Orb.defRadius));
		myPanel.add(radField);
		myPanel.add(new JLabel("Select the force of gravity: "));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Current: "+Orb.defGravity));
		myPanel.add(gravField);
		myPanel.add(new JLabel("Select the 'bounce' coefficient(between 0 and 1): "));
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Current: "+Orb.defRCoefficient));
		myPanel.add(coEffField);
		

		int result = JOptionPane.showConfirmDialog(storedWindow, myPanel, 
				"Please enter new configs", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
  			if(radField.getText() != null && !radField.getText().isEmpty()) {
  				Orb.defRadius = Float.parseFloat(radField.getText());
  			}
  			if(gravField.getText() != null && !gravField.getText().isEmpty()) {
  				Orb.defGravity = Float.parseFloat(gravField.getText());
  			}
  			if(coEffField.getText() != null && !coEffField.getText().isEmpty()) {
  				Orb.defRCoefficient = Float.parseFloat(coEffField.getText());
  			}
		}
	}
}