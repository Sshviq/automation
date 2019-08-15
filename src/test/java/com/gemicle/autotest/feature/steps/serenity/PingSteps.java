package com.gemicle.autotest.feature.steps.serenity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;

import com.gemicle.autotest.feature.actions.DBActions;
import com.gemicle.autotest.feature.actions.StrategyTypes;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;

import static org.assertj.core.api.Assertions.assertThat;

public class PingSteps {
    private final String strategy;
    private final String threshold;
    private final static int port = 80;
    private static final int maxTimeout = 5000;

    public PingSteps() {
        this.strategy = System.getProperty("Strategy");
        this.threshold = System.getProperty("Threshold");
    }

    @Step
    public void pingResource(String host) throws IllegalStateException {
        long currentTime = System.currentTimeMillis();
        int responseTime;
        if (isReachable(host)) {
            responseTime = (int) (System.currentTimeMillis() - currentTime);
        } else throw new IllegalStateException("Host doesn't response - " + host);
        Serenity.setSessionVariable(host).to(responseTime);
    }

    @Step
    public void checkResponseTime(String host) throws SQLException {
        int responseTime = Serenity.sessionVariableCalled(host);
        StrategyTypes strategyType = StrategyTypes.valueOf(strategy);
        int expectedTime = strategyType.calculate(host, responseTime);
        DBActions dbActions = new DBActions();
        dbActions.writeTimeResponse(host, responseTime);
        checkTimeResponseNotExceedThreshold(responseTime, expectedTime);
    }

    private void checkTimeResponseNotExceedThreshold(int responseTime, int expectedTime) {
        int deviation = (responseTime * 100) / expectedTime - 100;
        assertThat(deviation).as("deviation in %")
                             .isLessThan(Integer.parseInt(threshold));
    }

    private static boolean isReachable(String addr) {
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(addr, port), maxTimeout);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
