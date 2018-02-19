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
package fr.sonar.plugins.cxx.tests;

import fr.cnes.sonar.plugins.cxx.ws.CnesWs;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.server.ws.WsTester;

import static org.junit.Assert.*;

/**
 * Test for the CnesWs class
 * @author lequal
 */
public class CnesWsTest {
	
	/**
	 * Parameters number expected for the web service
	 */
	private static final int WS_PARAMS_NUMBER = 3;

	/**
	 * Stubbed controller for testing
	 */
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
        assertNotNull(controller);
        assertEquals("api/cnescxx", controller.path());
        assertFalse(controller.description().isEmpty());
        assertEquals(2, controller.actions().size());
    }

    /**
     * Check health web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void healthWebServiceTest() {
        final WebService.Action service = controller.action("health");
        assertNotNull(service);
        assertEquals("health", service.key());
        assertEquals(0,service.params().size());
        assertTrue(service.isInternal());
        assertTrue(!service.isPost());
    }

    /**
     * Check scan web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void reportWebServiceTest() {
        final WebService.Action service = controller.action("scan");
        assertNotNull(service);
        assertEquals("scan", service.key());
        assertEquals(WS_PARAMS_NUMBER, service.params().size());
        assertFalse(service.isPost());
        assertTrue(service.isInternal());
    }

}
