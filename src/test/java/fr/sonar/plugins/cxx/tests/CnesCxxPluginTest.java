package fr.sonar.plugins.cxx.tests;

import fr.cnes.sonar.plugins.cxx.CnesCxxPlugin;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.junit.Assert.assertEquals;

/**
 * Test for the CnesCxxPlugin class
 * @author begarco
 */
public class CnesCxxPluginTest {

    /**
     * Instance of the plugin to test
     */
    private CnesCxxPlugin cnesCxxPlugin;

    /**
     * Prepare each test by creating a new CnesCxxPlugin
     */
    @Before
    public void prepare() {
        cnesCxxPlugin = new CnesCxxPlugin();
    }

    /**
     * Assert that the plugin subscribe correctly to SonarQube
     * by checking the good number of extensions.
     */
    @Test
    public void sonarqubePluginDefinitionTest() {
        final SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(6,3), SonarQubeSide.SERVER);
        final Plugin.Context context = new Plugin.Context(runtime);
        cnesCxxPlugin.define(context);
        assertEquals(context.getExtensions().size(), 2);
    }

}
