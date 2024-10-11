package com.skillstorm.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/skillstorm/cucumber")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = "junit.jupiter.execution.parallel.enabled", value = "true")
@ConfigurationParameter(key = "junit.jupiter.execution.parallel.mode.default", value = "concurrent")
@ConfigurationParameter(key = "junit.jupiter.execution.parallel.config.strategy", value = "dynamic")
public class CucumberTestRunner {
    // Empty class body, Cucumber and JUnit will handle the test execution
}

