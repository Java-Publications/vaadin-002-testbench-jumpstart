package junit.org.rapidpm.vaadin.ui.app;

import org.junit.Assert;
import org.junit.Test;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.TextFieldElement;

/**
 *
 */
public class AddressBook001Test extends BaseVaadinTestClass {
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


}
