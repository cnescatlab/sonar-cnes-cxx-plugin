package fr.sonar.plugins.cxx.tests;

import fr.cnes.sonar.plugins.cxx.ws.CnesWs;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.server.ws.WsTester;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test for the CnesWs class
 * @author lequal
 */
public class CnesWsTest {

    private WebService.Controller controller;

    /**
     * Executed each time before running a single test
     */
    @Before
    public void prepare() {
        final WebService ws = new CnesWs();

        // WsTester is available in the Maven artifact
        // org.codehaus.sonar:sonar-plugin-api
        // with type "test-jar"
        final WsTester tester = new WsTester(ws);
        controller = tester.controller("api/cnescxx");
    }

    /**
     * Check that the controller has correct parameters
     */
    @Test
    public void controllerTest() {
        assertThat(controller).isNotNull();
        assertThat(controller.path()).isEqualTo("api/cnescxx");
        assertThat(controller.description()).isNotEmpty();
        assertThat(controller.actions().size()).isEqualTo(2);
    }

    /**
     * Check health web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void healthWebServiceTest() {
        final WebService.Action service = controller.action("health");
        assertThat(service).isNotNull();
        assertThat(service.key()).isEqualTo("health");
        assertThat(service.params().size()).isEqualTo(0);
        assert(service.isInternal());
        assert(!service.isPost());
    }

    /**
     * Check scan web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void reportWebServiceTest() {
        final WebService.Action service = controller.action("scan");
        assertThat(service).isNotNull();
        assertThat(service.key()).isEqualTo("scan");
        assertThat(service.params().size()).isEqualTo(3);
        assertThat(!service.isPost());
        assertThat(service.isInternal());
    }

}
