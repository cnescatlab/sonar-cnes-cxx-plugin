package fr.cnes.sonar.plugins.cxx.sensors;

import fr.cnes.sonar.plugins.cxx.utils.StringManager;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import static fr.cnes.sonar.plugins.cxx.utils.StringManager.string;

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

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(CxxSensor.class.getName());
    /**
     * Public name of the sensor
     */
    public static final String NAME = "CNES C/C++ Sensor";
    /**
     * First language the sensor can analyze
     */
    private static final String C_LANGUAGE = "c";
    /**
     * Second language the sensor can analyze
     */
    private static final String CPP_LANGUAGE = "c++";


    /**
     * Describe on which languages, files, etc. this Sensor must be applied.
     * @param sensorDescriptor Data describing the current sensor,
     *                         they have to be changed to set the sensor.
     */
    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name(NAME);
        sensorDescriptor.onlyOnLanguages(C_LANGUAGE, CPP_LANGUAGE);
    }

    /**
     * Code executed by the sensor when called by sonar-scanner, SonarLint or maven.
     * @param sensorContext Given by the API and contains all needed data.
     */
    @Override
    public void execute(SensorContext sensorContext) {
        // retrieve settings provided in sonar.properties & sonar-project.properties
        Settings settings = sensorContext.settings();

        String src = settings.getString(string(StringManager.SRC_PROPERTY_KEY));
        String include = settings.getString(string(StringManager.INC_PROPERTY_KEY));

        // construct commands for tools (cppcheck, vera, rats) from the pattern
        // cppcheck
        String cppcheckCommand = string(StringManager.CPPCHECK_COMMAND_PATTERN);
        cppcheckCommand = cppcheckCommand.replaceFirst(string(StringManager.SRC_PLACEHOLDER), src);
        cppcheckCommand = cppcheckCommand.replaceFirst(string(StringManager.INC_PLACEHOLDER),include);
        // vera
        String veraCommand = string(StringManager.VERA_COMMAND_PATTERN);
        veraCommand = veraCommand.replaceFirst(string(StringManager.SRC_PLACEHOLDER), src);
        // rats
        String ratsCommand = string(StringManager.RATS_COMMAND_PATTERN);
        ratsCommand = ratsCommand.replaceFirst(string(StringManager.SRC_PLACEHOLDER), src);

        // execute analysis with tools
        // results are placed in following files:
        // + Cppcheck: cppcheck-report.xml
        // + Vera: vera-report.xml
        // + RATS: rats-report.xml
        try {
            executeCommand(cppcheckCommand);
            executeCommand(veraCommand);
            executeCommand(ratsCommand);
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        // set values for reports' filename
        settings.setProperty(string(StringManager.CPPCHECK_REPORT_PATH_KEY), string(StringManager.CPPCHECK_REPORT_FILENAME));
        settings.setProperty(string(StringManager.VERA_REPORT_PATH_KEY), string(StringManager.VERA_REPORT_FILENAME));
        settings.setProperty(string(StringManager.RATS_REPORT_PATH_KEY), string(StringManager.RATS_REPORT_FILENAME));
    }

    /**
     * Execute an environment command
     * @param command command to execute on the system
     * @return logs
     * @throws IOException when a stream use goes wrong
     * @throws InterruptedException when a command is not finished
     */
    private String executeCommand(String command) throws IOException, InterruptedException {
        // log the command to execute
        LOGGER.info(command);

        // prepare a string builder for the output gathering
        StringBuilder output = new StringBuilder();

        // create a new process
        Process p;
        // execute the process on the runtime
        p = Runtime.getRuntime().exec(command);

        String result;


            try (
                    // collect input
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    // collect errors
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(p.getErrorStream()))
            ) {
                do {
                    LOGGER.info("coucou");
                // append input stream to output
                String line;
//                while ((line = reader.readLine()) != null) {
//                    output.append(line).append('\n');
//                }
//                // append error stream to output
//                while ((line = reader2.readLine()) != null) {
//                    output.append(line).append('\n');
//                }
                    output.append(reader.readLine());

                    // log output
                    result = output.toString();
                    LOGGER.info(result);
                } while (p.isAlive());
            }

        // wait for the end of the process
        p.waitFor();

        // return the output logs
        return result;
    }
}
