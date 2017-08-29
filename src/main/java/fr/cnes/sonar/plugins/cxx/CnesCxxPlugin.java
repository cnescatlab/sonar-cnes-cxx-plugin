package fr.cnes.sonar.plugins.cxx;

import fr.cnes.sonar.plugins.cxx.utils.StringManager;
import fr.cnes.sonar.plugins.cxx.ws.CnesWs;
import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinition.Builder;

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
        // add the service to execute c/cpp tools
        context.addExtension(CnesWs.class);

        // retrieve all information about property to set
        // property key
        final String propertyKey = string(StringManager.INC_PROP_DEF_KEY);
        // property name
        final String propertyName = string(StringManager.INC_PROP_DEF_NAME);
        // property description
        final String propertyDescription = string(StringManager.INC_PROP_DEF_DESC);
        // property default value
        final String propertyDefaultValue = string(StringManager.INC_PROP_DEF_DEFAULT);

        // define a property to give the folder containing headers
        // define the builder
        final Builder builder = PropertyDefinition.builder(propertyKey);
        // set attributes
        builder.name(propertyName);
        builder.description(propertyDescription);
        builder.defaultValue(propertyDefaultValue);
        // build the property
        final PropertyDefinition propertyDefinition = builder.build();
        // add it to SonarQube context
        context.addExtension(propertyDefinition);
    }
}
