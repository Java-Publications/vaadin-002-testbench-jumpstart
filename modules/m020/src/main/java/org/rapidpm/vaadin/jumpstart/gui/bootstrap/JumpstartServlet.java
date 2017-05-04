package org.rapidpm.vaadin.jumpstart.gui.bootstrap;

import static java.util.Collections.singletonList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.rapidpm.vaadin.jumpstart.gui.bootstrap.core.DDIVaadinServlet;
import org.rapidpm.vaadin.jumpstart.gui.bootstrap.core.JumpstartUI;

import com.vaadin.annotations.VaadinServletConfiguration;

/**
 * Created by svenruppert on 06.04.17.
 */
@WebServlet(urlPatterns = "/*", name = "JumpstartServlet", asyncSupported = true, displayName = "JumpstartServlet")
@VaadinServletConfiguration(ui = JumpstartUI.class, productionMode = false)
public class JumpstartServlet extends DDIVaadinServlet {

    @Override
    public List<String> topLevelPackagesToActivate() {
        return singletonList("org.rapidpm");
    }
}