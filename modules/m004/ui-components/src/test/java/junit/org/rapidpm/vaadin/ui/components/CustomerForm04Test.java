package junit.org.rapidpm.vaadin.ui.components;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.rapidpm.vaadin.shared.CustomerStatus;

/**
 *
 */

public class CustomerForm04Test extends CustomerFormPageObject {

  @Test
  public void test001() {
    getDriver().get(url);

    Assert.assertNotNull(statusSelect());
    Assert.assertEquals(CustomerStatus.values().length + 1 , statusSelect().getOptions().size());

    statusSelect().selectByText(CustomerStatus.Contacted.name());
    saveEntry();
    clickSwitchButton();
    Assert.assertEquals(statusSelect().getValue() , CustomerStatus.Contacted.name());

  }
}