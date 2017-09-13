/*
 * This file is part of cnescxx.
 *
 * cnescxx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * cnescxx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with cnescxx.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.cnes.sonar.plugins.cxx;

import fr.cnes.sonar.plugins.cxx.utils.StringManager;
import fr.cnes.sonar.plugins.cxx.ws.CnesWs;
import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinition.Builder;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 * @author lequal
 */
public class CnesCxxPlugin implements Plugin {

    /**
     * Definition of the plugin:
     * add the cxx sensor that launches all tools.
     *
     * @param context Execution context of the plugin
     */
    @Override
    public void define(final Context context) {
        // add the service to execute c/cpp tools
        context.addExtension(CnesWs.class);

        // retrieve all information about property to set
        // property key
        final String propertyKey = StringManager.string(StringManager.INC_PROP_DEF_KEY);
        // property name
        final String propertyName = StringManager.string(StringManager.INC_PROP_DEF_NAME);
        // property description
        final String propertyDescription = StringManager.string(StringManager.INC_PROP_DEF_DESC);
        // property default value
        final String propertyDefValue = StringManager.string(StringManager.INC_PROP_DEF_DEFAULT);

        // define a property to give the folder containing headers
        // define the builder
        final Builder builder = PropertyDefinition.builder(propertyKey);
        // set attributes
        builder.name(propertyName);
        builder.description(propertyDescription);
        builder.defaultValue(propertyDefValue);
        // build the property
        final PropertyDefinition propertyDefinition = builder.build();
        // add it to SonarQube context
        context.addExtension(propertyDefinition);
    }
}
