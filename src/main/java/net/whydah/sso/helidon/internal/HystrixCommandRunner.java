package net.whydah.sso.helidon.internal;

import com.netflix.hystrix.HystrixCommand;

public class HystrixCommandRunner {

    String execute(HystrixCommand<String> command) {
        return command.execute();
    }
}
