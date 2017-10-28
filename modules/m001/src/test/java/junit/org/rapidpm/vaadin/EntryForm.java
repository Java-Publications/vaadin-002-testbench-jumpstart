package junit.org.rapidpm.vaadin;

import org.openqa.selenium.WebDriver;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class EntryForm extends TestBenchTestCase {

  public EntryForm(WebDriver driver) {
    setDriver(driver);
  }

  public String getLastName() {
    return $(FormLayoutElement.class).$(TextFieldElement.class).
        get(1).getValue();
  }

  public void setLastName(String lastName) {
    $(FormLayoutElement.class).$(TextFieldElement.class).
        get(1).setValue(lastName);
  }

  public String getFirstName() {
    return $(FormLayoutElement.class).$(TextFieldElement.class).
        first().getValue();
  }

  public void setFirstName(String firstName) {
    $(FormLayoutElement.class).$(TextFieldElement.class).
        first().setValue(firstName);
  }

  public void saveEntry() {
    $(ButtonElement.class).caption("Save").first().click();
  }
}
