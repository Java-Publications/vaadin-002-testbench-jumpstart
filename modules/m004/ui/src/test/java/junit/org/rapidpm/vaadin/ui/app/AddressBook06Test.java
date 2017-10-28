package junit.org.rapidpm.vaadin.ui.app;

import org.junit.Assert;
import org.junit.Test;
import org.rapidpm.vaadin.srv.CustomerServiceImpl;
import com.vaadin.testbench.elements.GridElement;

/**
 *
 */
public class AddressBook06Test extends AddressBook {

  @Test
  public void test001() throws Exception {
    final int firstCount = CustomerServiceImpl.getInstance().findAll().size();
    getDriver().get(url);

    final CustomerFormPageObject newEntry = createNewEntry();
    newEntry.setLastName("X");
    newEntry.setFirstName("Y");
    newEntry.saveEntry();
    final GridElement gridElement = dataGrid();
    final long rowCount = gridElement.getRowCount();
    Assert.assertEquals(rowCount , firstCount + 1);

  }
}
