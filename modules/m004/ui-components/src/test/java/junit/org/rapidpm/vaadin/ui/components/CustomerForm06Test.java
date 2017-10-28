package junit.org.rapidpm.vaadin.ui.components;

import org.junit.Assert;
import org.junit.Test;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 */
public class CustomerForm06Test extends CustomerFormPageObject {

  @Test
  public void test001() {
    getDriver().get(url);
    Assert.assertTrue(saveButton()
                          .getClassNames()
                          .contains("v-button-" + ValoTheme.BUTTON_PRIMARY));
  }
}