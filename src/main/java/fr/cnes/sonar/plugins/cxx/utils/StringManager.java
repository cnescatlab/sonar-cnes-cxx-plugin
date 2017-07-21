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
     * Property key for the key of include property definition
     */
    public static final String INC_PROP_DEF_KEY = "def.prop.inc.key";
    /**
     * Property key for the name of include property definition
     */
    public static final String INC_PROP_DEF_NAME = "def.prop.inc.name";
    /**
     * Property key for the description of include property definition
     */
    public static final String INC_PROP_DEF_DESC = "def.prop.inc.desc";
    /**
     * Property key for the default value of include property definition
     */
    public static final String INC_PROP_DEF_DEFAULT = "def.prop.inc.default";
    /**
     * Property key for the src
     */
    public static final String SRC_PROPERTY_KEY = "property.key.src";
    /**
     * Property key for the inc
     */
    public static final String INC_PROPERTY_KEY = "property.key.inc";
    /**
     * Property key for the cppcheck command pattern
     */
    public static final String CPPCHECK_COMMAND_PATTERN = "cppcheck.command.pattern";
    /**
     * Property key for the vera command pattern
     */
    public static final String VERA_COMMAND_PATTERN = "vera.command.pattern";
    /**
     * Property key for the rats command pattern
     */
    public static final String RATS_COMMAND_PATTERN = "rats.command.pattern";
    /**
     * Property key for the cppcheck report path
     */
    public static final String CPPCHECK_REPORT_PATH_KEY = "cppcheck.report.path.key";
    /**
     * Property key for the cppcheck report filename
     */
    public static final String CPPCHECK_REPORT_FILENAME = "cppcheck.report.filename";
    /**
     * Property key for the vera report path
     */
    public static final String VERA_REPORT_PATH_KEY = "vera.report.path.key";
    /**
     * Property key for the vera report filename
     */
    public static final String VERA_REPORT_FILENAME = "vera.report.filename";
    /**
     * Property key for the rats report path
     */
    public static final String RATS_REPORT_PATH_KEY = "rats.report.path.key";
    /**
     * Property key for the rats report filename
     */
    public static final String RATS_REPORT_FILENAME = "rats.report.filename";
    /**
     * Property key for the source placeholder
     */
    public static final String SRC_PLACEHOLDER = "placeholder.src";
    /**
     * Property key for the include placeholder
     */
    public static final String INC_PLACEHOLDER = "placeholder.include";
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
