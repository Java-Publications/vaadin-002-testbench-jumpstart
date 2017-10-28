package junit.org.rapidpm.vaadin.ui.components;

import org.junit.Assert;
import org.junit.Test;
import com.vaadin.testbench.elements.TextFieldElement;

/**
 *
 */
public class CustomerForm01Test extends CustomerFormPageObject {

  public static final String LAST_NAME_ONE = "AAAA";
  public static final String LAST_NAME_TWO = "BBBB";

  @Test
  public void test001() {
    getDriver().get(url);

    setLastName(LAST_NAME_ONE);
    saveEntry();
    Assert.assertEquals(LAST_NAME_ONE , $(TextFieldElement.class).id(TestUI.LAST_NAME).getValue());
    clickSwitchButton();
    Assert.assertEquals(LAST_NAME_ONE , getLastName());
    setLastName(LAST_NAME_TWO);
    clickRegisterButton(); //Registrations off
    saveEntry();
    Assert.assertEquals(LAST_NAME_ONE , $(TextFieldElement.class).id(TestUI.LAST_NAME).getValue());
    clickSwitchButton();
    Assert.assertEquals(LAST_NAME_TWO , getLastName());
//    setLastName(LAST_NAME_TWO); is from last time -> statefull component
    clickRegisterButton();
    saveEntry();
    Assert.assertEquals(LAST_NAME_TWO , $(TextFieldElement.class).id(TestUI.LAST_NAME).getValue());


  }

  @Test
  public void test002() {
    getDriver().get(url);
    saveEntry();

    Assert.assertEquals("2" , $(TextFieldElement.class).id(TestUI.ID).getValue());
    clickSwitchButton();
    deleteEntry();
    Assert.assertEquals("-1" , $(TextFieldElement.class).id(TestUI.ID).getValue());
    clickSwitchButton();
    saveEntry();
    Assert.assertEquals("0" , $(TextFieldElement.class).id(TestUI.ID).getValue());
    clickSwitchButton();
    clickRegisterButton(); // registrations off
    deleteEntry();
    Assert.assertEquals("0" , $(TextFieldElement.class).id(TestUI.ID).getValue());
    clickSwitchButton();
    clickRegisterButton(); // registrations on
    deleteEntry();
    Assert.assertEquals("-1" , $(TextFieldElement.class).id(TestUI.ID).getValue());

  }
}