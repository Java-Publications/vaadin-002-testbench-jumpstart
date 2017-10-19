package junit.org.rapidpm.vaadin.ui.app;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AddressBook003Test extends BaseVaadinTestClass {
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
