package net.whydah.sso.helidon.internal;

import net.whydah.sso.application.types.ApplicationCredential;
import net.whydah.sso.session.WhydahApplicationSession;
import net.whydah.sso.session.WhydahUserSession;

/**
 * CredentialStore will manage authentication sessions for the Service aka Application
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class CredentialStore {
    private static WhydahApplicationSession applicationSession = null;
    private static WhydahUserSession adminUserSession = null;
    private final String securityTokenServiceUri;
    private final ApplicationCredential applicationCredential;
    //    private final UserCredential adminUserCredential;
    private final String userAdminServiceUri;

    public CredentialStore(String securityTokenServiceUri,
                           String userAdminServiceUri,
                           String applicationId,
                           String applicationName,
                           String applicationSecret) {
        this.securityTokenServiceUri = securityTokenServiceUri;
        this.userAdminServiceUri = userAdminServiceUri;
        this.applicationCredential = new ApplicationCredential(applicationId, applicationName, applicationSecret);
//        this.adminUserCredential = new UserCredential(adminUserId, adminUserSecret);

    }

    public String getApplicationTokenId() {
        if (applicationSession == null) {
            applicationSession = WhydahApplicationSession.getInstance(securityTokenServiceUri, userAdminServiceUri, applicationCredential);
        }
        return applicationSession.getActiveApplicationTokenId();
    }

    public WhydahApplicationSession getApplicationSession() {
        if (applicationSession == null) {
            applicationSession = WhydahApplicationSession.getInstance(securityTokenServiceUri, applicationCredential);
        }
        return applicationSession;
    }

    /*
        public String getAdminUserTokenId() {
        if (adminUserSession == null) {
            adminUserSession = getAdminUserSession();
        }
        return adminUserSession.getActiveUserTokenId();
    }

    public WhydahUserSession getAdminUserSession() {
        if (adminUserSession == null) {
            adminUserSession = new WhydahUserSession(getApplicationSession(), adminUserCredential);
        }
        return adminUserSession;
    }

     */
}
