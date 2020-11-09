package net.whydah.sso.helidon.internal;

import io.helidon.security.Subject;
import io.helidon.security.SubjectType;
import net.whydah.sso.helidon.Authentication;

import java.lang.annotation.Annotation;

public class TestStubs {

    public static Authentication buildStubUser() {
        Authentication stubUserAuth = new Authentication() {
            @Override
            public String value() {
                return "stubUser";
            }

            @Override
            public SubjectType type() {
                return SubjectType.USER;
            }

            @Override
            public String[] roles() {
                return new String[]{"stub_user"};
            }

            @Override
            public String[] scopes() {
                return new String[0];
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Authentication.class;
            }
        };
        return stubUserAuth;
    }

    public static Authentication buildStubService() {
        Authentication stubServiceAuth = new Authentication() {
            @Override
            public String value() {
                return "stubService";
            }

            @Override
            public SubjectType type() {
                return SubjectType.SERVICE;
            }

            @Override
            public String[] roles() {
                return new String[]{"stub_service"};
            }

            @Override
            public String[] scopes() {
                return new String[0];
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Authentication.class;
            }
        };
        return stubServiceAuth;
    }
}
