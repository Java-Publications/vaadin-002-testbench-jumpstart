package junit.org.rapidpm.vaadin.ui.components;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

/**
 *
 */
@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = TestUI.class, productionMode = false)
public class TestServlet extends VaadinServlet {
}
