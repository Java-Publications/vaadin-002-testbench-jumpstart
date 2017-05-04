package junit.org.rapidpm.vaadin.jumpstart.gui;

import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.memoizer.Memoizer.memoize;
import static org.rapidpm.frp.model.Result.success;
import static org.rapidpm.frp.vaadin.addon.testbench.BrowserDriverFunctions.ipSupplierLocalIP;
import static org.rapidpm.frp.vaadin.addon.testbench.BrowserDriverFunctions.webDriver;
import static org.rapidpm.microservice.MainUndertow.DEFAULT_SERVLET_PORT;
import static org.rapidpm.microservice.MainUndertow.MYAPP;
import static org.rapidpm.microservice.MainUndertow.SERVLET_PORT_PROPERTY;
import static org.rapidpm.vaadin.testbench.addon.MicroserviceTestUtils.setUpMicroserviceProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.rapidpm.vaadin.testbench.addon.MicroserviceTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.TestBenchTestCase;

/**
 * Created by svenruppert on 07.04.17.
 */

@RunWith(value = Parameterized.class)
public class BaseTestbenchTest extends TestBenchTestCase {



    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTestbenchTest.class);

    @Rule public TestName testName = new TestName();


    static void initSystemProperties() {
        final String pointToStartFrom = new File("").getAbsolutePath();
        final String DATA_DRIVER_BASE_FOLDER = "/_data/driver/";
        final String OS = "osx";
        String basePath = pointToStartFrom + DATA_DRIVER_BASE_FOLDER + OS;
        System.setProperty("webdriver.chrome.driver", basePath + "/chrome/chromedriver");
        System.setProperty("webdriver.gecko.driver", basePath + "/gecko/geckodriver");
        System.setProperty("phantomjs.binary.path", basePath + "/phantomjs/phantomjs");
    }


    @BeforeClass
    public static void setUpClass() {
        initSystemProperties();
        setUpMicroserviceProperties();
    }

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
    @Parameterized.Parameters(name = "{index}: testPair - {0}")
    public static Object[] data() {
        return Context.streamOfConfig
            .get()
            .collect(Collectors.toList())
            .toArray();
    }

    @Before
    public void setUpBase()
        throws Exception {
        DI.clearReflectionModel();
        DI.activatePackages("org.rapidpm");
        DI.activatePackages(this.getClass());
        DI.activateDI(this);
        Main.deploy();
        ((CheckedExecutor) this::setUpTestbench)
            .execute()
            .bind(
                sucess -> {
                    LOGGER.info("test is ready to runn..");
                    saveScreenshot("001_before");
                },
                failed -> LOGGER.warn("setting up Testbench faild for !! "
                                      + dataConfig.getT1().get() + " - "
                                      + dataConfig.getT2().get() + " - "
                                      + dataConfig.getT3().get() + " - "
                                      + dataConfig.getT4().get()
                )
            );

    }

    public void setUpTestbench()
        throws Exception {

        LOGGER.info("Running Config " + dataConfig.getT1() + " "
                    + dataConfig.getT2() + " "
                    + dataConfig.getT3() + " "
                    + dataConfig.getT4());

        //browserType, platform, runningLocal, seleniumHubIP
        webDriver
            .apply(dataConfig.getT1(), dataConfig.getT2(),
                   dataConfig.getT3(), dataConfig.getT4())
            .ifPresent(this::setDriver);

        Optional.ofNullable(getDriver())
                .ifPresent(d -> d.get(baseURL() + "?restartApplication"));

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

    @After
    public void tearDown() throws Exception {
        ((CheckedExecutor) () -> saveScreenshot("002_after")).execute();
        ((CheckedExecutor) this::tearDownTestbench).execute();
        ((CheckedExecutor) Main::stop).execute();
        ((CheckedExecutor) DI::clearReflectionModel).execute();
    }

    public void tearDownTestbench()
        throws Exception {
        Optional
            .ofNullable(getDriver())
            .ifPresent(WebDriver::quit);
    }

    //TestBenchCommandExecutor
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

    private Supplier<String> memoizedLocalIpSupplier = memoize(ipSupplierLocalIP);

    private Supplier<String> memoizedServletPortSupplier = memoize(() -> System.getProperty(SERVLET_PORT_PROPERTY) == null
        ? DEFAULT_SERVLET_PORT + ""
        : System.getProperty(SERVLET_PORT_PROPERTY));

    public String baseURL() {
        return "http://" + memoizedLocalIpSupplier.get() + ":" + memoizedServletPortSupplier.get() + "/" + MYAPP;
    }

}
