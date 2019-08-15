package com.gemicle.autotest.feature.steps;

import java.io.IOException;
import java.sql.SQLException;

import com.gemicle.autotest.feature.steps.serenity.PingSteps;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

public class PingStepDefinitions {

    @Steps
    PingSteps ping;

    @When("^I ping host '(.*)'$")
    public void pingByAddress(String host) throws IOException {
        ping.pingResource(host);
    }

    @Then("^Host '(.*)' should response in time$")
    public void checkResponseTime(String host) throws SQLException {
        ping.checkResponseTime(host);
    }
}
