package junit.org.rapidpm.vaadin.ui.app;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.chrome.ChromeDriver;
import org.rapidpm.ddi.DI;
import org.rapidpm.dependencies.core.net.PortUtils;
import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.microservice.Main;
import org.rapidpm.microservice.MainUndertow;
import com.vaadin.testbench.TestBenchTestCase;

/**
 *
 */
public class BaseVaadinTestClass extends TestBenchTestCase {

  protected String url;

  @BeforeClass
  public static void setUpClass() {
    final String pointToStartFrom = new File("").getAbsolutePath();
    final String dataDriverBaseFolder = "/_data/webdrivers/";
//    final String os = "osx";

//    String basePath = pointToStartFrom + dataDriverBaseFolder + os;
    String basePath = pointToStartFrom + dataDriverBaseFolder;
    //TODO - operating system specific
    System.setProperty("webdriver.chrome.driver" , basePath + "/chromedriver-mac-64bit");
    System.setProperty("webdriver.gecko.driver" , basePath + "/geckodriver-mac-64bit");
    System.setProperty("webdriver.safari.driver" , basePath + "/geckodriver-mac-64bit");
    System.setProperty("webdriver.opera.driver" , basePath + "/operadriver-mac-64bit/operadriver");
    System.setProperty("phantomjs.binary.path" , basePath + "/phantomjs-mac-64bit");

  }

  @Before
  public void setUp()
      throws Exception {
    System.setProperty(MainUndertow.REST_PORT_PROPERTY , new PortUtils().nextFreePortForTest() + "");
    System.setProperty(MainUndertow.SERVLET_PORT_PROPERTY , new PortUtils().nextFreePortForTest() + "");
    url = "http://127.0.0.1:" + System.getProperty(MainUndertow.SERVLET_PORT_PROPERTY) + MainUndertow.MYAPP + "/test"; //from Annotation Servlet
    System.out.println("url = " + url);

    DI.clearReflectionModel();
    DI.activatePackages("org.rapidpm");
    DI.activatePackages(this.getClass());
    DI.activateDI(this);
    Main.deploy();

    setDriver(new ChromeDriver());
//    setDriver(new FirefoxDriver());
//    setDriver(new SafariDriver());
//    setDriver(new OperaDriver());
    getDriver().manage().window().maximize();
  }

  @After
  public void tearDown()
      throws Exception {
    ((CheckedExecutor) () -> getDriver().quit()).execute();
    ((CheckedExecutor) Main::stop).execute();
    ((CheckedExecutor) DI::clearReflectionModel).execute();

  }

  //TODO inheritance is slowing down without benefits
//  @Test
//  public void testAddressBook() {
//    getDriver().get(url);
//    Assert.assertTrue($(GridElement.class).exists());
//  }
}
