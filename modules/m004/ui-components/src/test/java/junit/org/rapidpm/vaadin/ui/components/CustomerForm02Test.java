package junit.org.rapidpm.vaadin.ui.components;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.TextFieldElement;

/**
 *
 */

public class CustomerForm02Test extends CustomerFormPageObject {

  @Test
  public void test001() {
    getDriver().get(url);
    final FormLayoutElement customerForm = $(FormLayoutElement.class).id(TestUI.CUSTOMER_FORM);
    Assert.assertTrue(customerForm.isDisplayed());
    deleteEntry();
    Assert.assertEquals(0 , getDriver().findElements(new By.ById(TestUI.CUSTOMER_FORM)).size());

    final String id = $(TextFieldElement.class).id(TestUI.ID).getValue();
    Assert.assertEquals("-1" , id);
  }


  @Test
  public void test002() {
    getDriver().get(url);
    final FormLayoutElement customerForm = $(FormLayoutElement.class).id(TestUI.CUSTOMER_FORM);
    Assert.assertTrue(customerForm.isDisplayed());
    saveEntry();
    Assert.assertEquals(0 , getDriver().findElements(new By.ById(TestUI.CUSTOMER_FORM)).size());

    final String id = $(TextFieldElement.class).id(TestUI.ID).getValue();
    Assert.assertEquals("2" , id);
  }


}