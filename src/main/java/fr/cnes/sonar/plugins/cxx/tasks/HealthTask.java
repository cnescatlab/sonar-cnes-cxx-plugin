package fr.cnes.sonar.plugins.cxx.tasks;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;

import static fr.cnes.sonar.plugins.cxx.utils.StringManager.HEALTH_RESPONSE_STATUS;
import static fr.cnes.sonar.plugins.cxx.utils.StringManager.string;

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
    public void handle(Request request, Response response) {

        // write the json response
        final JsonWriter jsonWriter = response.newJsonWriter();
        jsonWriter.beginObject();
        // add logs to response
        jsonWriter.prop(string(HEALTH_RESPONSE_STATUS), OK);
        jsonWriter.endObject();
        jsonWriter.close();
    }
}
