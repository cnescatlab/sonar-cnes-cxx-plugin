package fr.sonar.plugins.cxx.tests;

import fr.cnes.sonar.plugins.cxx.sensors.CxxSensor;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for the CxxSensor class
 * @author begarco
 */
public class CxxSensorTest {

    /**
     * Path to the mock pom for creating a mock context for test
     */
    private static final String PATHNAME = "src/test/files/";
    /**
     * Sensor to test
     */
    private CxxSensor sensor;

    /**
     * Prepare attributes for testing correctly each test.
     */
    @Before
    public void prepare() {
        sensor = new CxxSensor();
    }

    /**
     * Assert that the sensor's description is correct.
     */
    @Test
    public void describeTest() {
        SensorDescriptor sensorDescriptor = mock(SensorDescriptor.class);
        sensor.describe(sensorDescriptor);
        verify(sensorDescriptor).name(CxxSensor.NAME);
    }

    /**
     * Assert that the execution of the scan happened correctly
     * by checking the number of issues.
     */
    @Test
    public void executeTest() {
        SensorContextTester context = SensorContextTester.create(new File(PATHNAME));

        sensor.execute(context);

        assert(context.allIssues().isEmpty());
    }

}
