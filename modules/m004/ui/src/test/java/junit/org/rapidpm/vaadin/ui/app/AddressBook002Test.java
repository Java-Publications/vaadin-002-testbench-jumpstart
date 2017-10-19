package junit.org.rapidpm.vaadin.ui.app;

import org.junit.Assert;
import org.junit.Test;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.TextFieldElement;

/**
 *
 */
public class AddressBook002Test extends BaseVaadinTestClass {

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


}
