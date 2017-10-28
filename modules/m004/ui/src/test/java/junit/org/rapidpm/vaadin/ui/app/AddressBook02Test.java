package junit.org.rapidpm.vaadin.ui.app;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AddressBook02Test extends AddressBook {

  @Test
  public void test001() {
    getDriver().get(url);

    //filter
    filterTextField().setValue("Lara");

    Assert.assertEquals("Lara" , getFirstNameAtIndex(0));
    Assert.assertEquals("Martin" , getLastNameAtIndex(0));

    Assert.assertEquals(1L , dataGrid().getRowCount());

    clearFilterBTN().click();

    Assert.assertTrue(dataGrid().getRowCount() > 1);
  }

}
