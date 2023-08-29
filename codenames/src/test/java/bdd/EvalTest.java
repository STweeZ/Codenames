package bdd;

import static org.jbehave.core.io.CodeLocations.codeLocationFromPath;

import java.util.List;
import java.util.Properties;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.jupiter.api.Test;

public class EvalTest extends JUnitStories {

    public EvalTest() {
        // JUnitReportingRunner.recommendedControls(configuredEmbedder());
        configuredEmbedder().embedderControls().doGenerateViewAfterStories(false).doIgnoreFailureInStories(false)
                .doIgnoreFailureInView(false).doVerboseFailures(true);
    }

    @Override
    public Configuration configuration() {
        Properties viewResources = new Properties();
        viewResources.put("decorateNonHtml", "true");
        return new MostUsefulConfiguration()
                .useStoryReporterBuilder(new StoryReporterBuilder().withDefaultFormats()
                        .withViewResources(viewResources).withFormats(Format.ANSI_CONSOLE, Format.STATS)
                        .withFailureTrace(true).withFailureTraceCompression(true));
    }

    // Here we specify the steps classes
    @Override
    public InjectableStepsFactory stepsFactory() {
        // varargs, can have more that one steps classes
        return new InstanceStepsFactory(configuration(), new PlayerEval(),new TeamEval(),new CardEval(),new GameEval());
    }

    @Override
    protected List<String> storyPaths() {
        List<String> paths = new StoryFinder().findPaths(codeLocationFromPath("src/test/resources"),
                "**/*.story", "**/*excluded.story");
        return paths;
    }

    @Override
    @Test
    public void run() {
        // Compatibility JUnit 5
        super.run();
    }
}
