package junit.org.rapidpm.vaadin.ui.app;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AddressBook01Test extends AddressBook {

  public static final String FIRST_NAME = "Alfred";
  public static final String LAST_NAME = "Mueller";

  @Test
  public void test001() {
    getDriver().get(url);

    final CustomerFormPageObject newEntry = createNewEntry();
    newEntry.setFirstName(FIRST_NAME);
    newEntry.setLastName(LAST_NAME);
    newEntry.saveEntry();

    dataGrid().getCell(6 , 0).click();
    Assert.assertNotEquals(FIRST_NAME , activeCustomerForm().getFirstName());
    Assert.assertNotEquals(LAST_NAME , activeCustomerForm().getLastName());

    dataGrid().getCell(0 , 0).click();

    Assert.assertFalse(dataGrid().isSelected());

    Assert.assertEquals(FIRST_NAME , dataGrid().getCell(0 , 0).getText());
    Assert.assertEquals(LAST_NAME , dataGrid().getCell(0 , 1).getText());
    Assert.assertEquals(FIRST_NAME , activeCustomerForm().getFirstName());
    Assert.assertEquals(LAST_NAME , activeCustomerForm().getLastName());
  }


}
