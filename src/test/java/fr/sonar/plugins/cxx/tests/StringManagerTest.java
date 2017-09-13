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

import fr.cnes.sonar.plugins.cxx.utils.StringManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for the StringManager class
 * @author lequal
 */
public class StringManagerTest {

	/**
	 * Just a test string to validate that a request on an invalid
	 * key does not work.
	 */
    private static final String NOT_EXIST = "I_DO_NOT_EXIST";

    /**
     * Assert that the same StringManager instance is returned each time
     * StringManager.getInstance() is called.
     */
    @Test
    public void singletonUniquenessTest() {
        final StringManager sm1 = StringManager.getInstance();
        final StringManager sm2 = StringManager.getInstance();

        assertEquals(sm1, sm2);
    }

    /**
     * Assert that you can use the main StringManager method 'string()' statically
     * without prior initialization.
     */
    @Test
    public void simpleStringRequestTest() {
        assertEquals("\n", StringManager.string(StringManager.CNES_LOG_SEPARATOR));
    }

    /**
     * Assert that an incorrect string requested to th StringManager
     * returned the string: "unknown string".
     */
    @Test
    public void unknownStringRequestTest() {
        assertEquals(StringManager.DEFAULT_STRING, StringManager.string(NOT_EXIST));
    }

}
