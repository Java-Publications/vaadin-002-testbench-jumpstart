package junit.org.rapidpm.vaadin.ui.app;

import org.junit.Assert;
import org.junit.Test;
import org.rapidpm.vaadin.srv.CustomerServiceImpl;

/**
 *
 */
public class AddressBook05Test extends AddressBook {



  @Test
  public void test001() throws Exception {
    getDriver().get(url);
    Assert.assertEquals(dataGrid().getRowCount() , CustomerServiceImpl.getInstance().findAll().size());

  }


}
