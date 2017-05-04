package junit.my.vaadin.app;

import static org.rapidpm.vaadin.testbench.addon.MicroserviceTestUtils.baseURL;
import static org.rapidpm.vaadin.testbench.addon.MicroserviceTestUtils.setUpMicroserviceProperties;
import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.rapidpm.ddi.DI;
import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.microservice.Main;
import org.rapidpm.vaadin.testbench.addon.MicroserviceTestUtils;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class AddressBookTest extends TestBenchTestCase {

    static void initSystemProperties() {
        final String pointToStartFrom = new File("").getAbsolutePath();
        final String DATA_DRIVER_BASE_FOLDER = "/_data/driver/";
        final String OS = "osx";
        String basePath = pointToStartFrom + DATA_DRIVER_BASE_FOLDER + OS;
        System.setProperty("webdriver.chrome.driver", basePath + "/chrome/chromedriver");
        System.setProperty("webdriver.gecko.driver", basePath + "/gecko/geckodriver");
        System.setProperty("phantomjs.binary.path", basePath + "/phantomjs/phantomjs");
    }

    @Before
    public void setUp()
        throws Exception {
        initSystemProperties();
        setUpMicroserviceProperties();

        DI.clearReflectionModel();
        DI.activatePackages("org.rapidpm");
        DI.activatePackages("my.vaadin");
        DI.activatePackages(this.getClass());
        DI.activateDI(this);
        Main.deploy();
        //        ((CheckedExecutor) this::setUpTestbench)
        //            .execute()
        //            .bind(
        //                sucess -> LOGGER.info("test is ready to runn.."),
        //                failed -> LOGGER.warn("setting up Testbench faild for !! "
        //                                      + dataConfig.getT1().get() + " - "
        //                                      + dataConfig.getT2().get() + " - "
        //                                      + dataConfig.getT3().get() + " - "
        //                                      + dataConfig.getT4().get()
        //                )
        //            );

        setDriver(new ChromeDriver());
//        setDriver(new PhantomJSDriver());
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
        getDriver().get(baseURL.get());
        Assert.assertTrue($(GridElement.class).exists());
    }

    @Test
    public void testFormShowsCorrectData() {
        getDriver().get(baseURL.get());

        // 1. Find the Table
        GridElement table = $(GridElement.class).first();

        // 2. Store the first name and last name values shown
        // in the first row of the table for later comparison
        String firstName = table.getCell(0, 0).getText();
        String lastName = table.getCell(0, 1).getText();

        // 3. Click on the first row
        table.getCell(0, 0).click();

        // 4. Assert that the values in the first name and
        // last name fields are the same as in the table
        Assert.assertEquals(firstName, $(FormLayoutElement.class).$(TextFieldElement.class).first().getValue());
        Assert.assertEquals(lastName, $(FormLayoutElement.class).$(TextFieldElement.class).get(1).getValue());
    }

    @Test
    public void testEnterNew() {
        getDriver().get(baseURL.get());

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
        table.getCell(6, 0).click();

        // 6. Assert that the entered name is not in the text
        // fields any longer
        Assert.assertNotEquals("Tyler", $(FormLayoutElement.class).
                                                                      $(TextFieldElement.class).first().getValue());
        Assert.assertNotEquals("Durden", $(FormLayoutElement.class).
                                                                       $(TextFieldElement.class).get(1).getValue());

        // 7. Click on the first row
        table.getCell(0, 0).click();

        // 8. Verify that the first row and form
        // contain "Tyler Durden"
        Assert.assertEquals("Tyler", table.getCell(0, 0).getText());
        Assert.assertEquals("Durden", table.getCell(0, 1).getText());
        Assert.assertEquals("Tyler", $(FormLayoutElement.class).
                                                                   $(TextFieldElement.class).first().getValue());
        Assert.assertEquals("Durden", $(FormLayoutElement.class).
                                                                    $(TextFieldElement.class).get(1).getValue());
    }

    @Test
    public void testEnterNewPageObjects() {
        getDriver().get(baseURL.get());

        AddressBook addressBook = new AddressBook(getDriver());

        EntryForm form = addressBook.createNewEntry();
        form.setFirstName("Tyler");
        form.setLastName("Durden");
        form.saveEntry();

        // Select some other entry
        form = addressBook.selectEntryAtIndex(6);

        // Assert that the entered name is not in the
        // text fields any longer
        Assert.assertNotEquals("Tyler", form.getFirstName());
        Assert.assertNotEquals("Durden", form.getLastName());

        // Verify that the first row and form contain
        // "Tyler Durden"
        form = addressBook.selectEntryAtIndex(0);
        Assert.assertEquals("Tyler", addressBook.getFirstNameAtIndex(0));
        Assert.assertEquals("Durden", addressBook.getLastNameAtIndex(0));
        Assert.assertEquals("Tyler", form.getFirstName());
        Assert.assertEquals("Durden", form.getLastName());
    }
}