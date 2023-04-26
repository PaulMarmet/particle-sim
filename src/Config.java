import java.io.*;
import java.util.Properties;

public class Config {
	static String configFilePath;
	static FileInputStream propsInput;
	static Properties prop;
    public static void main(String[] args) {
        try {
            configFilePath = "src/config.properties";
            propsInput = new FileInputStream(configFilePath);
            prop = new Properties();
            prop.load(propsInput);

            //System.out.println(prop.getProperty("DB_USER"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //get value from config file
    public static float getF(String str) {
		float data = Float.parseFloat(prop.getProperty(str));
    	return data;
    }
    public static int getInt(String str) {
		int data = Integer.parseInt(prop.getProperty(str));
    	return data;
    }
    public static boolean getBool(String str) {
		boolean data = Boolean.parseBoolean(prop.getProperty(str));
    	return data;
    }
    
    //set value to config file
    public static void set(String peram, float value) {
		prop.setProperty(peram, String.valueOf(value));
    }
    public static void set(String peram, int value) {
		prop.setProperty(peram, String.valueOf(value));
    }
    public static void set(String peram, boolean value) {
		prop.setProperty(peram, String.valueOf(value));
    }
}