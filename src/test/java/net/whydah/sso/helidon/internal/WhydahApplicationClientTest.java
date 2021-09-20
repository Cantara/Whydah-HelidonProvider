package net.whydah.sso.helidon.internal;

import io.helidon.security.SubjectType;
import net.whydah.sso.helidon.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WhydahApplicationClientTest {

    private HystrixCommandRunner hystrixCommandRunner;
    private WhydahApplicationClient client;
    @BeforeEach
    public void setUp() throws Exception {
        hystrixCommandRunner = mock(HystrixCommandRunner.class);
        client = new WhydahApplicationClient(URI.create("http://localhost"), hystrixCommandRunner);
    }

    @Test
    public void findServiceAuthNonValid() {
        when(hystrixCommandRunner.execute(any())).thenReturn(null);
        assertNull(client.findServiceAuth("nonvalidtoken"));
    }

    @Test
    public void findServiceAuthValid() {
        when(hystrixCommandRunner.execute(any())).thenReturn("applicationId");
        Authentication serviceAuth = client.findServiceAuth("validtokenid");
        assertNotNull(serviceAuth);
        assertEquals("applicationId", serviceAuth.value());
        assertEquals(new String[]{"service_verified"}.toString(),serviceAuth.roles().toString());
        assertEquals(SubjectType.SERVICE, serviceAuth.type());
    }
}
