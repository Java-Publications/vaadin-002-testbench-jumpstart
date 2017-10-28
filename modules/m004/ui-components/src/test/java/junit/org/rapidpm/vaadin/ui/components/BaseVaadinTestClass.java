package junit.org.rapidpm.vaadin.ui.components;

import static org.rapidpm.frp.StringFunctions.notEmpty;
import static org.rapidpm.frp.StringFunctions.notStartsWith;
import static org.rapidpm.frp.Transformations.not;
import static org.rapidpm.microservice.MainUndertow.REST_HOST_PROPERTY;
import static org.rapidpm.microservice.MainUndertow.REST_PORT_PROPERTY;
import static org.rapidpm.microservice.MainUndertow.SERVLET_HOST_PROPERTY;
import static org.rapidpm.microservice.MainUndertow.SERVLET_PORT_PROPERTY;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.function.Supplier;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.rapidpm.ddi.DI;
import org.rapidpm.dependencies.core.net.PortUtils;
import org.rapidpm.frp.Transformations;
import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.frp.functions.CheckedSupplier;
import org.rapidpm.microservice.Main;
import org.rapidpm.microservice.MainUndertow;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;

/**
 *
 */
public abstract class BaseVaadinTestClass extends TestBenchTestCase {


  protected String url;

//  @BeforeClass
//  public static void setUpClass() {
//    final String pointToStartFrom = new File("").getAbsolutePath();
//    final String dataDriverBaseFolder = "/_data/webdrivers/";
//    final String os = "osx";

//    String basePath = pointToStartFrom + dataDriverBaseFolder + os;
//    String basePath = pointToStartFrom + dataDriverBaseFolder;
  //TODO - operating system specific
//    System.setProperty("webdriver.chrome.driver" , basePath + "/chromedriver-mac-64bit");
//    System.setProperty("webdriver.gecko.driver" , basePath + "/geckodriver-mac-64bit");
//    System.setProperty("webdriver.safari.driver" , basePath + "/geckodriver-mac-64bit");
//    System.setProperty("webdriver.opera.driver" , basePath + "/operadriver-mac-64bit/operadriver");
//    System.setProperty("phantomjs.binary.path" , basePath + "/phantomjs-mac-64bit");
//  }

  @Before
  public void setUp()
      throws Exception {
    final PortUtils utils = new PortUtils();
    System.setProperty(REST_PORT_PROPERTY , utils.nextFreePortForTest() + "");
    System.setProperty(SERVLET_PORT_PROPERTY , utils.nextFreePortForTest() + "");
    System.setProperty(SERVLET_HOST_PROPERTY , ipSupplierLocalIP.get());
    System.setProperty(REST_HOST_PROPERTY , ipSupplierLocalIP.get());
    System.setProperty(MainUndertow.SHIRO_ACTIVE_PROPERTY , "false");
    url = "http://" + ipSupplierLocalIP.get() + ":" + System.getProperty(SERVLET_PORT_PROPERTY) + MainUndertow.MYAPP;

    DI.clearReflectionModel();
    DI.activatePackages("org.rapidpm");
    DI.activatePackages(this.getClass());
    DI.activateDI(this);
    Main.deploy();


    final URL hubURL = new URL("http://" + ipSupplierLocalIP.get() + ":4444/wd/hub");
    final RemoteWebDriver remoteWebDriver = new RemoteWebDriver(hubURL , DesiredCapabilities.chrome());
    final WebDriver webDriver = TestBench.createDriver(remoteWebDriver);

    setDriver(webDriver);
//    setDriver(new ChromeDriver());
//    setDriver(new FirefoxDriver());
//    setDriver(new SafariDriver());
//    setDriver(new OperaDriver());
//    getDriver().manage().window().maximize();
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


  Supplier<String> ipSupplierLocalIP = () -> {
    final CheckedSupplier<Enumeration<NetworkInterface>> checkedSupplier = NetworkInterface::getNetworkInterfaces;

    return Transformations.<NetworkInterface>enumToStream()
        .apply(checkedSupplier.getOrElse(Collections::emptyEnumeration))
        .map(NetworkInterface::getInetAddresses)
        .flatMap(iaEnum -> Transformations.<InetAddress>enumToStream().apply(iaEnum))
        .filter(inetAddress -> inetAddress instanceof Inet4Address)
        .filter(not(InetAddress::isMulticastAddress))
        .map(InetAddress::getHostAddress)
        .filter(notEmpty())
        .filter(adr -> notStartsWith().apply(adr , "127"))
        .filter(adr -> notStartsWith().apply(adr , "169.254"))
        .filter(adr -> notStartsWith().apply(adr , "255.255.255.255"))
        .filter(adr -> notStartsWith().apply(adr , "255.255.255.255"))
        .filter(adr -> notStartsWith().apply(adr , "0.0.0.0"))
        //            .filter(adr -> range(224, 240).noneMatch(nr -> adr.startsWith(valueOf(nr))))
        .findFirst().orElse("localhost");
  };

}
