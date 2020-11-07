package net.whydah.sso.helidon;

import io.helidon.security.*;
import io.helidon.security.spi.AuthenticationProvider;
import io.helidon.security.spi.SynchronousProvider;
import net.whydah.sso.helidon.internal.CredentialStore;
import net.whydah.sso.helidon.internal.WhydahApplicationClient;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static net.whydah.sso.helidon.internal.ConfigUtils.getConfigValue;
import static org.slf4j.LoggerFactory.getLogger;

public class WhydahProvider extends SynchronousProvider implements AuthenticationProvider {
    private static final Logger log = getLogger(WhydahProvider.class);

    private boolean whydahEnabled = false;
    private String whydahUri;
    private String securityTokenServiceUri;
    private String userAdminServiceUri;
    private String applicationId;
    private String applicationName;
    private String applicationSecret;
    private CredentialStore credentialStore;
    private WhydahApplicationClient whydahIntegration;


    public WhydahProvider() {
        String whydahEnabledValue = getConfigValue("whydah_enabled");
        if (Boolean.parseBoolean(whydahEnabledValue)) {
            whydahEnabled = true;
            log.info("Graph is enabled. Will read and write to the graph.");
            whydahUri = getConfigValue("whydah_uri");
            securityTokenServiceUri = whydahUri + "tokenservice/";
            userAdminServiceUri = whydahUri + "useradminservice/";
            applicationId = getConfigValue("whydah_application_id");
            applicationName = getConfigValue("whydah_application_name");
            applicationSecret = getConfigValue("whydah_application_secret");
            credentialStore = new CredentialStore(securityTokenServiceUri, userAdminServiceUri, applicationId, applicationName, applicationSecret);
            whydahIntegration = new WhydahApplicationClient(securityTokenServiceUri);
            String applicationTokenId = credentialStore.getApplicationTokenId();

            if (applicationTokenId == null || applicationTokenId.isEmpty()) {
                String message = String.format("Failed to logon to Wyhdah on uri: %s, applicationId: %s, applicationName: %s, applicationsSecret: %s", securityTokenServiceUri, applicationId, applicationName, applicationSecret);
                log.error(message);
                throw new RuntimeException(message);
            } else {
                log.trace("Application {} is loged on to Whydah. {}", applicationName);
            }
        }
    }

    @Override
    protected AuthenticationResponse syncAuthenticate(ProviderRequest providerRequest) {
        boolean hasAuthorization = providerRequest.env().headers().containsKey("Authorization");
        if (!hasAuthorization) {
            return AuthenticationResponse.failed("Missing Authorization Bearer Token.");
        } else {
            List<String> bearerTokens = providerRequest.env().headers().get("Authorization");
            String bearerToken = bearerTokens.get(0);

            String clientTokenId = bearerToken.substring(7);
            if (whydahEnabled) {
                Authentication serviceAuth = whydahIntegration.findServiceAuth(clientTokenId);
                Subject service = buildSubject(serviceAuth);
                return AuthenticationResponse.successService(service);
            } else {
                log.trace("Attemting to authenticate based on clienttTokenId: {} is not possible when Whydah is disabled.", clientTokenId);
                return AuthenticationResponse.abstain();
            }
        }
        /*
        List<SecurityLevel> securityLevels = providerRequest.endpointConfig().securityLevels();
        ListIterator<SecurityLevel> listIterator = securityLevels.listIterator(securityLevels.size());
        Subject user = null;
        Subject service = null;
        while (listIterator.hasPrevious()) {
            SecurityLevel securityLevel = listIterator.previous();
            List<Authentications> authenticationAnnots = securityLevel
                    .filterAnnotations(Authentications.class, EndpointConfig.AnnotationScope.METHOD);

            List<Authentication> authentications = new LinkedList<>();
            authenticationAnnots.forEach(atn -> authentications.addAll(Arrays.asList(atn.value())));


            if (!authentications.isEmpty()) {
                for (Authentication authentication : authentications) {
                    if (authentication.type() == SubjectType.USER) {
                        user = buildSubject(authentication);
                    } else {
                        service = buildSubject(authentication);
                    }
                }
                break;
            }
        }
        return AuthenticationResponse.success(user, service);

         */
    }

    private Subject buildSubject(Authentication authentication) {
        Subject.Builder subjectBuilder = Subject.builder();

        subjectBuilder.principal(Principal.create(authentication.value()));
        for (String role : authentication.roles()) {
            subjectBuilder.addGrant(Role.create(role));
        }
        for (String scope : authentication.scopes()) {
            subjectBuilder.addGrant(Grant.builder()
                    .name(scope)
                    .type("scope")
                    .build());
        }

        return subjectBuilder.build();
    }

    @Override
    public Collection<Class<? extends Annotation>> supportedAnnotations() {
        return Set.of(
                Authentication.class
        );
//        return CollectionsHelper.setOf(Authentication.class);
    }

    public boolean isWhydahEnabled() {
        return whydahEnabled;
    }
}
