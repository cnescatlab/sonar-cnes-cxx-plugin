package fr.cnes.sonar.plugins.cxx;

import fr.cnes.sonar.plugins.cxx.sensors.CxxSensor;
import fr.cnes.sonar.plugins.cxx.utils.StringManager;
import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;

import static fr.cnes.sonar.plugins.cxx.utils.StringManager.string;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 * @author begarco
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

        // define a property to give the folder containing headers
        // define the builder
        PropertyDefinition.Builder builder = PropertyDefinition.builder(string(StringManager.INC_PROP_DEF_KEY));
        // set attributes
        builder.name(string(StringManager.INC_PROP_DEF_NAME));
        builder.description(string(StringManager.INC_PROP_DEF_DESC));
        builder.defaultValue(string(StringManager.INC_PROP_DEF_DEFAULT));
        // build the property
        PropertyDefinition propertyDefinition = builder.build();
        // add it to SonarQube context
        context.addExtension(propertyDefinition);
    }
}
