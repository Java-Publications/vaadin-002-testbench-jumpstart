package junit.org.rapidpm.vaadin.ui.components;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.FormLayoutElement;

/**
 *
 */

public class CustomerForm05Test extends CustomerFormPageObject {

  @Test
  public void test001() {
    getDriver().get(url);

    final List<WebElement> elements = getDriver().findElements(new By.ById(TestUI.CUSTOMER_FORM));
    Assert.assertEquals(1 , elements.size());

    // test save Shortcut
    firstnameTF().sendKeys(Keys.chord(Keys.BACK_SPACE));
    getDriver().findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.ENTER));
    Assert.assertEquals(0 , getDriver().findElements(new By.ById(TestUI.CUSTOMER_FORM)).size());

    clickSwitchButton();
    Assert.assertFalse($(FormLayoutElement.class).$(ButtonElement.class).caption("Delete").all().isEmpty());
    Assert.assertTrue(getFirstName().isEmpty());

  }
}