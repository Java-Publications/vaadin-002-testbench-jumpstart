package junit.org.rapidpm.vaadin.ui;

import org.openqa.selenium.WebDriver;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridElement;

public class AddressBook extends TestBenchTestCase {

    public AddressBook(WebDriver driver) {
        setDriver(driver);
    }

    public String getLastNameAtIndex(int index) {
        return $(GridElement.class).first()
                                   .getCell(index, 1).getText();
    }

    public String getFirstNameAtIndex(int index) {
        return $(GridElement.class).first()
                                   .getCell(index, 0).getText();
    }

    public EntryForm selectEntryAtIndex(int index) {
        $(GridElement.class).first()
                            .getCell(index, 0).click();
        return new EntryForm(getDriver());
    }

    public EntryForm createNewEntry() {
        $(ButtonElement.class).caption("Add new customer").first().click();
        return new EntryForm(getDriver());
    }
}
