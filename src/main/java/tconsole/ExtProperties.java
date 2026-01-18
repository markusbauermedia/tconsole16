package tconsole;

import java.util.Properties;
import java.io.FileInputStream;

/**
 * Extended version of the Java Properties class. Provides
 * a constructor which loads the properties from a file and
 * methods to read other types than String
 * 
 */
public class ExtProperties extends Properties {

    /**
     * Constructs an ExtProperties object, tries to initialize
     * it from the file 'fname'. Ignores any errors.
     * 
     * @param     fname    The properties file to loead.
     */
    public ExtProperties(String fname) {
        try {
            load(new FileInputStream(fname));
        } catch (Exception e) {
            // ignore that
        }
    }

    /**
     * Gets an int value as a property.
     * 
     * @param     key       The property key to look for
     * @param     defval    The default value to be used if the key does
     *                      not exist or the int value cannot be parsed
     * @return              THe int value
     */
    public int getInteger(String key, int defval) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (Exception e) {
            return defval;
        }
    }

    /**
     * Gets a String value as a property. duplicates getProperty(String, String).
     * 
     * @param     key       The property key to look for
     * @param     defval    The default value to be used if the key does
     *                      not exist.
     * @return              THe String value
     */
    public String getString(String key, String defval) {
        return getProperty(key, defval);
    }

}
