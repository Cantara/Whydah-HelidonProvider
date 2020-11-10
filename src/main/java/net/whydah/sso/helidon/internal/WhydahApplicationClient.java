package net.whydah.sso.helidon.internal;

import io.helidon.security.SubjectType;
import net.whydah.sso.commands.appauth.CommandGetApplicationIdFromApplicationTokenId;
import net.whydah.sso.helidon.Authentication;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;

public class WhydahApplicationClient {
    private static final Logger log = getLogger(WhydahApplicationClient.class);
    private final URI securityTokenServiceUri;

    public WhydahApplicationClient(String securityTokenServiceUri) {
        this.securityTokenServiceUri = URI.create(securityTokenServiceUri);
    }

    public Authentication findServiceAuth(String clientTokenId) {
        Authentication serviceAuth = null;
        String applicationId = getApplicationTokenFromTokenId(clientTokenId);
        if (applicationId != null && !applicationId.isEmpty()) {
            serviceAuth = new Authentication() {

                public Class<? extends Annotation> annotationType() {
                    return Authentication.class;
                }

                @Override
                public String value() {
                    return applicationId;
                }

                @Override
                public SubjectType type() {
                    return SubjectType.SERVICE;
                }

                @Override
                public String[] roles() {
                    if (applicationId == null || applicationId.isEmpty()) {
                        return new String[0];
                    } else {
                        return new String[]{"service_verified"};
                    }
                }

                @Override
                public String[] scopes() {
                    return new String[]{"tfm2rec_read", "tfm2rec_write"};
                }
            };
        }
        return serviceAuth;
    }

    public String getApplicationTokenFromTokenId(String applicationTokenId) {
        String applicationId = new CommandGetApplicationIdFromApplicationTokenId(securityTokenServiceUri, applicationTokenId).execute();
        log.trace("Lookup application by applicationTokenId {}. Id found {}", applicationTokenId, applicationId);
        return applicationId;
    }
}
