package junit.org.rapidpm.vaadin.jumpstart.gui.screen.login;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.rapidpm.vaadin.jumpstart.gui.screen.login.LoginScreenCustom;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.NotificationElement;
import com.vaadin.testbench.elements.TextFieldElement;
import junit.org.rapidpm.vaadin.jumpstart.gui.BaseUITest;

/**
 * Created by svenruppert on 07.04.17.
 */
public class LoginScreenCustomTest extends BaseUITest {

    @Test
    public void test001()
        throws Exception {
        $(TextFieldElement.class).id(LoginScreenCustom.USERNAME_FIELD).setValue("admin");
        $(TextFieldElement.class).id(LoginScreenCustom.PASSWORD_FIELD).setValue("admin");
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id(LoginScreenCustom.LANGUAGE_COMBO);
        comboBoxElement.selectByText(propertyService.resolve("login.language.en"));
        $(ButtonElement.class).id(LoginScreenCustom.LOGIN_BUTTON).click();
        System.out.println("comboBoxElement = " + comboBoxElement);
    }

    @Test
    public void test002()
        throws Exception {
        //    saveScreenshot("before");
        $(TextFieldElement.class).id(LoginScreenCustom.USERNAME_FIELD).setValue("admin");
        $(TextFieldElement.class).id(LoginScreenCustom.PASSWORD_FIELD).setValue("xx");
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id(LoginScreenCustom.LANGUAGE_COMBO);
        comboBoxElement.selectByText(propertyService.resolve("login.language.en"));
        $(ButtonElement.class).id(LoginScreenCustom.LOGIN_BUTTON).click();

        NotificationElement notification = $(NotificationElement.class).first();
        assertEquals(propertyService.resolve("login.failed"),
                     notification.getCaption());
        assertEquals(propertyService.resolve("login.failed.description"),
                     notification.getDescription());
        assertEquals("warning", notification.getType());
    }
}
