import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SimConfigPane {
	int type;
	String dataType;
	JTextField textField = new JTextField(5);
	JCheckBox checkBox = new JCheckBox();
	String desc;
	String config;
	
	public SimConfigPane(String _desc, String _config, int _type)
	{
		desc = _desc;
		config = _config;
		type = _type;
	}
	public SimConfigPane(String _desc, String _config, String _dataType, int _type)
	{
		desc = _desc;
		config = _config;
		dataType = _dataType;
		type = _type;
	}
	
	public void addPaneT(JPanel panel)
	{
		panel.add(new JLabel(desc));
		panel.add(new JLabel(""));
		panel.add(new JLabel("Current: "+Config.getF(config)));
		panel.add(textField);
	}
	public void addPaneC(JPanel panel)
	{
		panel.add(new JLabel(desc));
		panel.add(new JLabel(""));
		panel.add(new JLabel("Current: "+Config.getBool(config)));
		checkBox.setSelected(Config.getBool(config));
		panel.add(checkBox);
	}
}
