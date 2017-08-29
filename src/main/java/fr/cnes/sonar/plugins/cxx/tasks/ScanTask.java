package fr.cnes.sonar.plugins.cxx.tasks;

import fr.cnes.sonar.plugins.cxx.utils.StringManager;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.utils.text.JsonWriter;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static fr.cnes.sonar.plugins.cxx.utils.StringManager.*;

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
public class ScanTask implements RequestHandler {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Loggers.get(ScanTask.class);
    /**
     * Maximum time a job can take in minutes
     */
    private static final int JOB_LIMIT = 120;
    /**
     * Name of the temporary script which contains the tools' command lines
     */
    private static final String CAT_CXX_SCRIPT_SH = "cat-cxx-script.sh";
    /**
     * Message displayed when the task is terminated
     */
    private static final String MSG_END = "C/C++ tools were executed.";
    /**
     * Delimiter in url
     */
    private static final String SLASH = "/";
    /**
     * Logged message when a file can not be deleted
     */
    private static final String FILE_DELETION_ERROR = "The following file could not be deleted: %s.";
    /**
     * Logged message when a file can not be set as executable
     */
    private static final String FILE_PERMISSIONS_ERROR = "Permissions of the following file could not be changed: %s.";

    /**
     * Create a temporary script containing dedicated command executing all cxx tools
     * @param project repository containing the source code
     * @param src sub repository with sources
     * @param inc sub repository with includes
     * @return The created file
     */
    private File createScript(String project, String src, String inc) {

        String sources = src;
        String includes = inc;

        // if these strings are empty we use the local directory
        if(sources.isEmpty()) {
            // sources are researched since the local repository
            sources = ".";
        }
        if(includes.isEmpty()) {
            // includes are researched since the local repository
            includes = ".";
        }

        // construct commands for tools (cppcheck, vera, rats) from the pattern
        // cppcheck
        String cppcheckCommand = string(StringManager.CPPCHECK_COMMAND_PATTERN);
        cppcheckCommand += " 2> "+string(CPPCHECK_REPORT_FILENAME);
        cppcheckCommand = cppcheckCommand.replaceFirst(string(StringManager.SRC_PLACEHOLDER), sources);
        cppcheckCommand = cppcheckCommand.replaceFirst(string(StringManager.INC_PLACEHOLDER), includes);
        // vera
        String veraCommand = string(StringManager.VERA_COMMAND_PATTERN);
        veraCommand = veraCommand.replaceFirst(string(StringManager.SRC_PLACEHOLDER), sources);
        // rats
        String ratsCommand = string(StringManager.RATS_COMMAND_PATTERN)+" > "+string(RATS_REPORT_FILENAME);
        ratsCommand = ratsCommand.replaceFirst(string(StringManager.SRC_PLACEHOLDER), sources);

        // create script in a file located in the project's repository
        final File scriptOutput = new File(string(CNES_WORKSPACE)+ SLASH +project+ SLASH +CAT_CXX_SCRIPT_SH);

        // Write all command lines in a single temporary script
        try (
                FileWriter script = new FileWriter(scriptOutput)
        ){
            script.write("#!/bin/bash -e");
            script.write("\ncd "+string(CNES_WORKSPACE)+SLASH+project);
            script.write(string(CNES_LOG_SEPARATOR)+veraCommand);
            LOGGER.info(veraCommand);
            script.write(string(CNES_LOG_SEPARATOR)+ratsCommand);
            LOGGER.info(ratsCommand);
            script.write(string(CNES_LOG_SEPARATOR)+cppcheckCommand);
            LOGGER.info(cppcheckCommand);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        // give execution rights on the script
        if(!scriptOutput.setExecutable(true)) {
            LOGGER.error(String.format(FILE_PERMISSIONS_ERROR, scriptOutput.getName()));
        }

        return scriptOutput;
    }

    /**
     * Execute an environment command
     * @param command command to execute on the system
     * @return logs
     * @throws IOException when a stream use goes wrong
     * @throws InterruptedException when a command is not finished
     */
    protected String executeCommand(String command) throws IOException, InterruptedException {
        // log the command to execute
        LOGGER.info(command);

        // prepare a string builder for the output gathering
        final StringBuilder output = new StringBuilder();

        // create a new process
        final Process p;
        // result to return
        String result = "";
        // execute the process on the runtime
        p = Runtime.getRuntime().exec(command);
        // wait for the end of the process
        p.waitFor();

        // number of minutes since the process starting
        int timestamp = 0;
        // true if the process has ended
        boolean isEnded = false;

        // while the process is not finished, we wait in the worst case one minute
        // and we log current output, if the job is not finished we wait again until
        // we reach the limit or the job stop
        while (!isEnded && timestamp < JOB_LIMIT) {
            isEnded = p.waitFor(1, TimeUnit.MINUTES);
            try (
                    // collect input
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    // collect errors
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {

                // append input stream to output
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append('\n');
                }
                // append error stream to output
                while ((line = reader2.readLine()) != null) {
                    output.append(line).append('\n');
                }
            }

            // log output
            result = output.toString();
            LOGGER.info(result);
            timestamp++;
        }

        // we destroy the job
        p.destroy();

        // return the output logs
        return result;
    }

    /**
     * Execute the c++ tools
     * @param request The request of the client
     * @param response The response which will be returned
     */
    @Override
    public void handle(Request request, Response response) throws Exception {

        // get project's information from the request's parameters
        final String projectName = request.mandatoryParam(string(ANALYZE_PROJECT_KEY));
        String src = request.param(string(ANALYZE_SRC_KEY));
        String inc = request.param(string(ANALYZE_INC_KEY));

        if(inc==null) {
            inc = "";
        }
        if(src==null) {
            src = "";
        }

        // create the temporary script to run cxx tools
        File script = createScript(projectName, src, inc);

        // log of the tools' execution
        String logs = "";

        // execute analysis with tools
        // results are placed in following files:
        // + Cppcheck: cppcheck-report.xml
        // + Vera: vera-report.xml
        // + RATS: rats-report.xml
        try {
            logs = executeCommand(string(CNES_WORKSPACE)+SLASH+projectName+SLASH+CAT_CXX_SCRIPT_SH);
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

        // delete temporary script
        if(!script.delete()) {
            LOGGER.error(String.format(FILE_DELETION_ERROR, script.getName()));
        }

        LOGGER.info(MSG_END);

        // write the json response
        final JsonWriter jsonWriter = response.newJsonWriter();
        jsonWriter.beginObject();
        // add logs to response
        jsonWriter.prop(string(ANALYZE_RESPONSE_LOG), logs+string(CNES_LOG_SEPARATOR)+MSG_END);
        jsonWriter.endObject();
        jsonWriter.close();
    }
}
