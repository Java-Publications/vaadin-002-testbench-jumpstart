package junit.org.rapidpm.vaadin.ui.app;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 */
public class AddressBook00Test extends AddressBook {


  @Test
  public void test001() {
    getDriver().get(url);
    clearFilterBTN().showTooltip();
    WebElement ttip = findElement(By.className("v-tooltip"));
    assertEquals(ttip.getText() , "Clear the current filter");

  }

  @Test
  public void test002() {
    getDriver().get(url);
    final String placeholder = filterTextField().getAttribute("placeholder");
    assertEquals(placeholder , "filter by name...");

  }


  @Test
  public void test003() throws Exception {
    getDriver().get(url);
    final List<FormLayoutElement> allA = $(FormLayoutElement.class).recursive(true).all();
    Assert.assertEquals(0 , allA.size());
    final CustomerFormPageObject customerFormPageObject = selectEntryAtIndex(1);
    final List<FormLayoutElement> allB = $(FormLayoutElement.class).recursive(true).all();
    Assert.assertEquals(1 , allB.size());
    customerFormPageObject.saveEntry();
    final List<FormLayoutElement> allC = $(FormLayoutElement.class).recursive(true).all();
    Assert.assertEquals(0 , allC.size());
  }


}
