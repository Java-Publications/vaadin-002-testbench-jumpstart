package junit.org.rapidpm.vaadin.ui.app;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AddressBook04Test extends AddressBook {

  @Test
  public void test001() {
    getDriver().get(url);

    final CustomerFormPageObject customerFormPageObject = selectEntryAtIndex(1);

    final CustomerFormPageObject newEntry = createNewEntry();

    Assert.assertFalse(dataGrid().isSelected());

  }


}
