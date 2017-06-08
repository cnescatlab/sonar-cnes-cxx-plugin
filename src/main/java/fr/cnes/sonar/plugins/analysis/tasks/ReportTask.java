package fr.cnes.sonar.plugins.analysis.tasks;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Execute element to produce the report
 * @author garconb
 */
public class ReportTask extends AbstractTask {

    /**
     * Product the report
     * @param projectId Key of the project to report
     * @param projectQualityGate Quality Gate of the project to report
     * @param projectName  Name of the project to report
     * @param reportAuthor Author of the report
     * @param reportPath Output folder
     * @param reportTemplate template to use for the processing
     * @return logs of the task
     * @throws IOException when a file writing goes wrong
     * @throws InterruptedException when a command is not finished
     */
    public String report(String projectId, String projectQualityGate,
                         String projectName, String reportAuthor, String reportPath,
                         String reportTemplate) throws IOException, InterruptedException {
        // construct the command string to run analysis
        String command = "java -jar " + "/opt/sonar/extensions/cnes/sonar-report-cnes.jar " +
                "--sonar.url http://localhost:9000 " +
                "--sonar.project.id \"" + projectId + "\" " +
                "--sonar.project.quality.gate \"" + projectQualityGate + "\" " +
                "--project.name \"" + projectName + "\" " +
                "--report.author \"" + reportAuthor + "\" " +
                "--report.date \"" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "\" " +
                "--report.path \"" + reportPath + "\" " +
                "--report.template \"" + reportTemplate + "\" " ;
        // log the command used
        log(command);
        // log the execution result
        log(executeCommand(command));

        // return the log
        return getLogs();
    }

}
