package junit.org.rapidpm.vaadin;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.rapidpm.ddi.DI;
import org.rapidpm.dependencies.core.net.PortUtils;
import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.microservice.Main;
import org.rapidpm.microservice.MainUndertow;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class AddressBookTest extends TestBenchTestCase {

  private String url;


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

  @Test
  public void testAddressBook() {
    getDriver().get(url);
    Assert.assertTrue($(GridElement.class).exists());
  }

  @Test
  public void testFormShowsCorrectData() {
    getDriver().get(url);

    // 1. Find the Table
    GridElement table = $(GridElement.class).first();

    // 2. Store the first name and last name values shown
    // in the first row of the table for later comparison
    String firstName = table.getCell(0 , 0).getText();
    String lastName = table.getCell(0 , 1).getText();

    // 3. Click on the first row
    table.getCell(0 , 0).click();

    // 4. Assert that the values in the first name and
    // last name fields are the same as in the table
    Assert.assertEquals(firstName , $(FormLayoutElement.class).$(TextFieldElement.class).first().getValue());
    Assert.assertEquals(lastName , $(FormLayoutElement.class).$(TextFieldElement.class).get(1).getValue());
  }

  @Test
  public void testEnterNew() {
    getDriver().get(url);

    // 1. Click the "New contact" button
    $(ButtonElement.class).caption("Add new customer").first().click();

    // 2. Enter "Tyler" into the first name field
    $(FormLayoutElement.class).$(TextFieldElement.class).
        first().setValue("Tyler");

    // 3. Enter "Durden" into the last name field
    $(FormLayoutElement.class).$(TextFieldElement.class).
        get(1).setValue("Durden");

    // 4. Save the new contact by clicking "Save" button
    $(ButtonElement.class).caption("Save").first().click();

    // 5. Click on some other row
    GridElement table = $(GridElement.class).first();
    table.getCell(6 , 0).click();

    // 6. Assert that the entered name is not in the text
    // fields any longer
    Assert.assertNotEquals("Tyler" , $(FormLayoutElement.class).
                                                                   $(TextFieldElement.class).first().getValue());
    Assert.assertNotEquals("Durden" , $(FormLayoutElement.class).
                                                                    $(TextFieldElement.class).get(1).getValue());

    // 7. Click on the first row
    table.getCell(0 , 0).click();

    // 8. Verify that the first row and form
    // contain "Tyler Durden"
    Assert.assertEquals("Tyler" , table.getCell(0 , 0).getText());
    Assert.assertEquals("Durden" , table.getCell(0 , 1).getText());
    Assert.assertEquals("Tyler" , $(FormLayoutElement.class).
                                                                $(TextFieldElement.class).first().getValue());
    Assert.assertEquals("Durden" , $(FormLayoutElement.class).
                                                                 $(TextFieldElement.class).get(1).getValue());
  }

  @Test
  public void testEnterNewPageObjects() {
    getDriver().get(url);

    AddressBook addressBook = new AddressBook(getDriver());

    EntryForm form = addressBook.createNewEntry();
    form.setFirstName("Tyler");
    form.setLastName("Durden");
    form.saveEntry();

    // Select some other entry
    form = addressBook.selectEntryAtIndex(6);

    // Assert that the entered name is not in the
    // text fields any longer
    Assert.assertNotEquals("Tyler" , form.getFirstName());
    Assert.assertNotEquals("Durden" , form.getLastName());

    // Verify that the first row and form contain
    // "Tyler Durden"
    form = addressBook.selectEntryAtIndex(0);
    Assert.assertEquals("Tyler" , addressBook.getFirstNameAtIndex(0));
    Assert.assertEquals("Durden" , addressBook.getLastNameAtIndex(0));
    Assert.assertEquals("Tyler" , form.getFirstName());
    Assert.assertEquals("Durden" , form.getLastName());
  }
}