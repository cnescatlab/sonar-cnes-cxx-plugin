package fr.cnes.sonar.plugins.cxx;

import fr.cnes.sonar.plugins.cxx.sensors.CxxSensor;
import org.sonar.api.Plugin;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 * @author garconb
 */
public class CnesCxxPlugin implements Plugin {

    /**
     * Definition of the plugin:
     * add the cxx sensor that launches all tools.
     *
     * @param context Execution context of the plugin
     */
    @Override
    public void define(Context context) {
        // add the sensor to execute c/cpp tools
        context.addExtension(CxxSensor.class);
    }
}
