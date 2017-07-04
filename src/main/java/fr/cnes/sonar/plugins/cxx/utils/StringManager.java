package fr.cnes.sonar.plugins.cxx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized the management of strings
 *
 * @author begarco
 */
public class StringManager {
    /**
     * Separator between two log entries
     */
    public static final String CNES_LOG_SEPARATOR = "cnes.log.separator";
    /**
     * Logger of this class
     */
    private static final Logger LOGGER = Logger.getLogger(StringManager.class.getName());
    /**
     * Properties file for the current plugin
     */
    private static final String PLUGIN_PROPERTIES = "strings.properties";
    /**
     * Default string to return when a key is unknown
     */
    public static final String DEFAULT_STRING = "unknown string";
    /**
     * Unique instance of this class (singleton)
     */
    private static final StringManager ourInstance = new StringManager();
    /**
     * Gather all the properties concerning the plugin
     */
    private Properties properties = new Properties();

    /**
     * Private constructor to make a singleton of this class
     */
    private StringManager() {
        try {
            load();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Get the singleton
     *
     * @return unique instance of StringManager
     */
    public static StringManager getInstance() {
        return ourInstance;
    }

    /**
     * Get the value of a property through its key
     *
     * @param key Key of the string to string
     * @return the property as String or the DEFAULT_STRING
     */
    public static String string(String key) {
        return getInstance().properties.getProperty(key, DEFAULT_STRING);
    }

    /**
     * load properties from a specific file
     *
     * @throws IOException when an error occurred during file reading
     */
    public void load() throws IOException {
        // store properties
        this.properties = new Properties();

        // read the file
        // load properties file as a stream
        try (InputStream input = StringManager.class.getClassLoader().getResourceAsStream(PLUGIN_PROPERTIES)) {
            if (input != null) {
                // load properties from the stream in an adapted structure
                this.properties.load(input);
            }
        }
    }
}
