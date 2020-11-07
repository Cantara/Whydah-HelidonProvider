package net.whydah.sso.helidon;

import io.helidon.security.Principal;
import io.helidon.security.SubjectType;
import io.helidon.security.abac.role.RoleValidator;
import io.helidon.security.annotations.Authenticated;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static org.slf4j.LoggerFactory.getLogger;

@Path("/whdyah/services")
@Authenticated
public class WhydahServicesAuthResource {
    private static final Logger log = getLogger(WhydahServicesAuthResource.class);

    @GET
//    @Authentication(value = "user",
//            roles = {"user_role"},
//            scopes = {"tfm2rec_read", "tfm2rec_write"})
//    @Authentication(value = "service",
//            type = SubjectType.SERVICE,
//            roles = {"service_role"},
//            scopes = {"tfm2rec_read", "tfm2rec_write"})
//    @RolesAllowed("service_verified") - Not working right now.
    @RoleValidator.Roles(value = "service_verified", subjectType = SubjectType.SERVICE)
    public String adminResource(@Context io.helidon.security.SecurityContext securityContext, @Context ContainerRequestContext requestContext) {
        if (securityContext.servicePrincipal().isPresent()) {
            log.trace("A service is authenticated.");
            Principal principal = securityContext.servicePrincipal().get();
            return "Welcome service by name: " + principal.getName();
        }
        SecurityContext secAtRec = requestContext.getSecurityContext();
        log.info("Request: {}", secAtRec);
        return "you are " + secAtRec.getUserPrincipal().getName();
    }
}
