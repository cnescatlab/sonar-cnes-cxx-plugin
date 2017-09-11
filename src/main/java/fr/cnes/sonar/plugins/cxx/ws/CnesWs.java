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
package fr.cnes.sonar.plugins.cxx.ws;

import fr.cnes.sonar.plugins.cxx.tasks.HealthTask;
import fr.cnes.sonar.plugins.cxx.tasks.ScanTask;
import org.sonar.api.server.ws.WebService;

import static fr.cnes.sonar.plugins.cxx.utils.StringManager.*;

/**
 * Expose CNES plugin api
 * @author lequal
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