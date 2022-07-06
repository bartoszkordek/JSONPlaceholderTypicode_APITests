package jsonPlaceholderTypicode.apiTests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources",
        glue = {"jsonPlaceholderTypicode/apiTests"},
        tags = "@APITest"
)
public class TestRunner {
}
