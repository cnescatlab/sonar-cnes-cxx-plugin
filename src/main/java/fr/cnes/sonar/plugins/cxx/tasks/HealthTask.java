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
package fr.cnes.sonar.plugins.cxx.tasks;

import fr.cnes.sonar.plugins.cxx.utils.StringManager;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;

/**
 * Class to run check the health of the system
 *
 * If all the system is ok, it should return a ok string in the status field
 *
 * @author lequal
 */
public class HealthTask implements RequestHandler {

    /**
     * Response when all is ok
     */
    private static final String OK = "ok";

    /**
     * Just return the status of the service
     * @param request The request of the client
     * @param response The response which will be returned
     */
    @Override
    public void handle(final Request request, final Response response) {

        // write the json response
        final JsonWriter jsonWriter = response.newJsonWriter();
        jsonWriter.beginObject();
        // add logs to response
        jsonWriter.prop(StringManager.string(StringManager.HEALTH_RESPONSE_STATUS), OK);
        jsonWriter.endObject();
        jsonWriter.close();
    }
}
