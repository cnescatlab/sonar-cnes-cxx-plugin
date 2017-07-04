package fr.cnes.sonar.plugins.cxx.sensors;

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;

/**
 * Class to run C/C++ tools during cxx.
 * This class is a sensor and is so called by sonar-scanner
 * at the same time than all other plugins.
 *
 * Used tools by this class are Vera++, RATS and Cppcheck.
 * They must be installed on the calling system.
 *
 * @author begarco
 */
public class CxxSensor implements Sensor {

    public static final String NAME = "CNES C/C++ Sensor";

    /**
     * Describe on which languages, files, etc. this Sensor must be applied.
     * @param sensorDescriptor Data describing the current sensor,
     *                         they have to be changed to set the sensor.
     */
    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name(NAME);
    }

    /**
     * Code executed by the sensor when called by sonar-scanner, SonarLint or maven.
     * @param sensorContext Given by the API and contains all needed data.
     */
    @Override
    public void execute(SensorContext sensorContext) {

    }
}
