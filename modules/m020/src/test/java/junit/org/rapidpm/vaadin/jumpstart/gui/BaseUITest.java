package junit.org.rapidpm.vaadin.jumpstart.gui;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.rapidpm.vaadin.jumpstart.gui.logic.properties.PropertyService;
import org.rapidpm.vaadin.jumpstart.gui.screen.login.LoginScreenCustom;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

/**
 * Created by svenruppert on 07.04.17.
 */
public class BaseUITest extends BaseTestbenchTest {

    @Test
    public void doNothing() throws Exception {
        Assert.assertTrue(true);
    }


    @Inject public PropertyService propertyService;

    public <T extends BaseTestbenchTest> T loginAsAdmin() {
        final VerticalLayoutElement loginElement = $(VerticalLayoutElement.class).id(LoginScreenCustom.LOGIN_SCREEN);

        $(TextFieldElement.class).id(LoginScreenCustom.USERNAME_FIELD).setValue("admin");
        $(TextFieldElement.class).id(LoginScreenCustom.PASSWORD_FIELD).setValue("admin");
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id(LoginScreenCustom.LANGUAGE_COMBO);
        comboBoxElement.selectByText(propertyService.resolve("login.language.en"));
        $(ButtonElement.class).id(LoginScreenCustom.LOGIN_BUTTON).click();
        return (T) this;
    }

    public String resolve(String key) {
        return propertyService.resolve(key);
    }
}