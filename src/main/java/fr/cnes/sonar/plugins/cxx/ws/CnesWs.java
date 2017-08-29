package fr.cnes.sonar.plugins.cxx.ws;

import fr.cnes.sonar.plugins.cxx.tasks.HealthTask;
import fr.cnes.sonar.plugins.cxx.tasks.ScanTask;
import org.sonar.api.server.ws.WebService;

import static fr.cnes.sonar.plugins.cxx.utils.StringManager.*;

/**
 * Expose CNES plugin api
 * @author begarco
 */
public class CnesWs implements WebService {

    /**
     * Define the new web service
     * Define each controller and action
     * @param context Context of the WebService
     */
    @Override
    public void define(Context context) {
        // create the new controller for the cnes web service
        final NewController controller = context.createController(string(CNES_CTRL_KEY));
        // set minimal sonarqube version required
        controller.setSince(string(SONAR_VERSION));
        // set description of the controller
        controller.setDescription(string(CNES_CTRL_DESCRIPTION));

        // create the action for URL /api/cnescxx/scan
        scanAction(controller);

        // create the action for URL /api/cnescxx/health
        healthAction(controller);

        // important to apply changes
        controller.done();
    }

    /**
     * Add the action corresponding to the scan
     * @param controller controller to which add the action
     */
    private void scanAction(NewController controller) {
        final NewAction analysis = controller.createAction(string(ANALYZE_KEY));
        //set
        analysis.setDescription(string(ANALYZE_DESC));
        analysis.setSince(string(SONAR_VERSION));
        analysis.setPost(false);
        analysis.setInternal(true);
        // new scan task to handle the request and work on the code
        analysis.setHandler(new ScanTask());
        // create parameter of the action
        // key parameter
        NewParam newParam = analysis.createParam(string(ANALYZE_PROJECT_KEY));
        newParam.setDescription(string(ANALYZE_PROJECT_DESC));
        newParam.setRequired(true);

        // src parameter
        newParam = analysis.createParam(string(ANALYZE_SRC_KEY));
        newParam.setDescription(string(ANALYZE_SRC_DESC));

        // inc parameter
        newParam = analysis.createParam(string(ANALYZE_INC_KEY));
        newParam.setDescription(string(ANALYZE_INC_DESC));
    }

    /**
     * Add the action corresponding to a health check
     * @param controller controller to which add the action
     */
    private void healthAction(NewController controller) {
        final NewAction analysis = controller.createAction(string(HEALTH_KEY));
        //set
        analysis.setDescription(string(HEALTH_DESC));
        analysis.setSince(string(SONAR_VERSION));
        analysis.setPost(false);
        analysis.setInternal(true);
        // new scan task to handle the request and work on the code
        analysis.setHandler(new HealthTask());
    }

}