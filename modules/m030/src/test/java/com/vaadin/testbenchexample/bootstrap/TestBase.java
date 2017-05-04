package com.vaadin.testbenchexample.bootstrap;

import static org.rapidpm.frp.vaadin.addon.testbench.BrowserDriverFunctions.initSystemProperties;
import static org.rapidpm.vaadin.testbench.addon.MicroserviceTestUtils.baseURL;
import static org.rapidpm.vaadin.testbench.addon.MicroserviceTestUtils.setUpMicroserviceProperties;
import static org.junit.Assert.assertFalse;
import static org.junit.runners.Parameterized.Parameters;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.success;
import static org.rapidpm.frp.vaadin.addon.testbench.BrowserDriverFunctions.webDriver;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.rapidpm.ddi.DI;
import org.rapidpm.frp.functions.CheckedConsumer;
import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.frp.matcher.Case;
import org.rapidpm.frp.model.Quad;
import org.rapidpm.frp.model.Result;
import org.rapidpm.microservice.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbenchexample.UsingHubITCase;

/**
 * Base class for all our tests, allowing us to change the applicable driver,
 * test URL or other configurations in one place. For an example of setting up a
 * hub configuration, see {@link UsingHubITCase}.
 */
@RunWith(value = Parameterized.class)
public class TestBase extends TestBenchTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestBase.class);

    @Rule public TestName testName = new TestName();



    @Test
    public void doNothing()
        throws Exception {
        Assert.assertTrue(true);
    }

    @Parameterized.Parameter
    //browserType, platform, runningLocal, seleniumHubIP
    public Quad<
        Supplier<String>,
        Supplier<Platform>,
        Supplier<Boolean>,
        Supplier<String>> dataConfig;

    //Single parameter, use Object[]
    @Parameters(name = "{index}: testPair - {0}")
    public static Object[] data() {
        return Context.streamOfConfig
            .get()
            .collect(Collectors.toList())
            .toArray();
    }

    @Before
    public void setUp()
        throws Exception {
        initSystemProperties();
        setUpMicroserviceProperties();

        DI.clearReflectionModel();
        DI.activatePackages("org.rapidpm");
        DI.activatePackages("com.vaadin.testbenchexample");
        DI.activatePackages(this.getClass());
        DI.activateDI(this);
        Main.deploy();
        ((CheckedExecutor) this::setUpTestbench)
            .execute()
            .bind(
                sucess -> {
                    LOGGER.info("test is ready to run..");
                    // Create a new Selenium driver - it is automatically extended to work
                    // with TestBench
                    //        WebDriver driver = new ChromeDriver();
                    //        setDriver(driver);

                    // Open the test application URL with the ?restartApplication URL
                    // parameter to ensure Vaadin provides us with a fresh UI instance.
                    getDriver().get(baseURL.get() + "?restartApplication");

                    // If you deploy using WTP in Eclipse, this will fail. You should
                    // update baseUrl to point to where the app is deployed.
                    String pageSource = getDriver().getPageSource();
                    String errorMsg = "Application is not available at " + baseURL.get() + ". Server not started?";
                    assertFalse(errorMsg, pageSource.contains("HTTP Status 404") ||
                                          pageSource.contains("can't establish a connection to the server"));

                },
                failed -> {
                    LOGGER.warn("setting up Testbench failed for !! "
                                + dataConfig.getT1().get() + " - "
                                + dataConfig.getT2().get() + " - "
                                + dataConfig.getT3().get() + " - "
                                + dataConfig.getT4().get()
                    );
                    Assert.fail("Init failed - was not able to set up TestBenchDriver");
                }
            );

    }

    @After
    public void tearDown()
        throws Exception {
        // Calling quit() on the driver closes the test browser.
        // When called like this, the browser is immediately closed on _any_
        // error. If you wish to take a screenshot of the browser at the time
        // the error occurred, you'll need to add the ScreenshotOnFailureRule
        // to your test and remove this call to quit().
        //        ((CheckedExecutor) () -> saveScreenshot("002_after")).execute();
        ((CheckedExecutor) () -> getDriver().quit()).execute();
        ((CheckedExecutor) Main::stop).execute();
        ((CheckedExecutor) DI::clearReflectionModel).execute();

    }

    public void setUpTestbench()
        throws Exception {

        LOGGER.info("Running Config : browserType, platform, runningLocal, seleniumHubIP -> " + dataConfig.getT1().get() + " "
                    + dataConfig.getT2().get() + " "
                    + dataConfig.getT3().get() + " "
                    + dataConfig.getT4().get());

        //browserType, platform, runningLocal, seleniumHubIP
        webDriver
            .apply(dataConfig.getT1(), dataConfig.getT2(),
                   dataConfig.getT3(), dataConfig.getT4())
            .ifPresent(this::setDriver);

        Optional.ofNullable(getDriver())
                .ifPresent(d -> d.get(baseURL.get() + "?restartApplication"));

        if (getDriver() instanceof PhantomJSDriver) {
            final Capabilities remoteWebDriverCapabilities = ((RemoteWebDriver) getDriver()).getCapabilities();
            if (remoteWebDriverCapabilities != null)
                if (remoteWebDriverCapabilities.getBrowserName().equals(BrowserType.PHANTOMJS)) {
                    getCommandExecutor().resizeViewPortTo(1280, 768);
                }
        }

        getCommandExecutor().enableWaitForVaadin();
        //        String pageSource = getDriver().getPageSource();
        //        String errorMsg = "Application is not available at " + baseURL() + ". Server not started?";
        //        Assert.assertFalse(errorMsg, pageSource.contains("HTTP Status 404") || pageSource.contains("can't establish a connection to the server"));
    }

    protected void saveScreenshot(String name) {
        String fileName = String.format("%s_%s.png", getClass().getSimpleName() + "_" + testName.getMethodName(), name);

        Optional
            .ofNullable(getDriver())
            .ifPresent((webDriver) -> {
                CheckedConsumer<WebDriver> execute = (driver) -> {
                    match(
                        matchCase(() -> success(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES))),
                        matchCase(() -> driver instanceof RemoteWebDriver,
                                  () -> success(((RemoteWebDriver) driver).getScreenshotAs(OutputType.BYTES))),
                        matchCase(() -> driver instanceof TestBenchDriverProxy,
                                  () -> takeScreenshot.apply((TestBenchDriverProxy) driver)))
                        .bind((bytes) -> fileWriter.accept(fileName, bytes), LOGGER::warn);
                    return null;
                };
                execute.apply(webDriver)
                       .bind(aVoid -> LOGGER.info("saveScreenshot was ok " + fileName),
                             LOGGER::warn);

            });
    }

    public Function<TestBenchDriverProxy, Result<byte[]>> takeScreenshot = (proxy) -> {
        final WebDriver actualDriver = proxy.getActualDriver();
        return Case.match(
            Case.matchCase(() -> Result.success(((TakesScreenshot) actualDriver).getScreenshotAs(OutputType.BYTES))),
            Case.matchCase(() -> actualDriver == null, () -> Result.failure("actualDriver is null")),
            Case.matchCase(() -> !(actualDriver instanceof TakesScreenshot), () -> Result.failure("actualDriver is not instanceof TakesScreenshot")));
    };

    public BiConsumer<String, byte[]> fileWriter = (fileName, bytes) -> {
        final File file = new File("target", fileName);

        ((CheckedExecutor) () -> {
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        })
            .execute()
            .bind(
                success -> LOGGER.info("file sucessfull writen " + file.getAbsolutePath()),
                LOGGER::warn);
    };

}
