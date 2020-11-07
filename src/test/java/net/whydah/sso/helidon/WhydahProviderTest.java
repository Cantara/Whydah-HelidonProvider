package net.whydah.sso.helidon;

import junit.framework.TestCase;

public class WhydahProviderTest extends TestCase {

    private WhydahProvider whydahProvider;
    public void setUp() throws Exception {
        whydahProvider = new WhydahProvider();
    }

    public void testIsWhydahEnabled() {
        assertFalse(whydahProvider.isWhydahEnabled());
    }
}