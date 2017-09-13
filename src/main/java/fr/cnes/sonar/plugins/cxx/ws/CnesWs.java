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
import fr.cnes.sonar.plugins.cxx.utils.StringManager;
import org.sonar.api.server.ws.WebService;

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
    public void define(final Context context) {
        // create the new controller for the cnes web service
        final NewController controller = context.createController(
                StringManager.string(StringManager.CNES_CTRL_KEY));
        // set minimal sonarqube version required
        controller.setSince(StringManager.string(StringManager.SONAR_VERSION));
        // set description of the controller
        controller.setDescription(StringManager.string(StringManager.CNES_CTRL_DESCRIPTION));

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
    private void scanAction(final NewController controller) {
        final NewAction analysis = controller.createAction(
                StringManager.string(StringManager.ANALYZE_KEY));
        //set
        analysis.setDescription(StringManager.string(StringManager.ANALYZE_DESC));
        analysis.setSince(StringManager.string(StringManager.SONAR_VERSION));
        analysis.setPost(false);
        analysis.setInternal(true);
        // new scan task to handle the request and work on the code
        analysis.setHandler(new ScanTask());
        // create parameter of the action
        // key parameter
        NewParam newParam = analysis.createParam(
                StringManager.string(StringManager.ANALYZE_PROJECT_KEY));
        newParam.setDescription(StringManager.string(StringManager.ANALYZE_PROJECT_DESC));
        newParam.setRequired(true);

        // src parameter
        newParam = analysis.createParam(StringManager.string(StringManager.ANALYZE_SRC_KEY));
        newParam.setDescription(StringManager.string(StringManager.ANALYZE_SRC_DESC));

        // inc parameter
        newParam = analysis.createParam(StringManager.string(StringManager.ANALYZE_INC_KEY));
        newParam.setDescription(StringManager.string(StringManager.ANALYZE_INC_DESC));
    }

    /**
     * Add the action corresponding to a health check
     * @param controller controller to which add the action
     */
    private void healthAction(final NewController controller) {
        final NewAction analysis = controller.createAction(
                StringManager.string(StringManager.HEALTH_KEY));
        //set
        analysis.setDescription(StringManager.string(StringManager.HEALTH_DESC));
        analysis.setSince(StringManager.string(StringManager.SONAR_VERSION));
        analysis.setPost(false);
        analysis.setInternal(true);
        // new scan task to handle the request and work on the code
        analysis.setHandler(new HealthTask());
    }

}