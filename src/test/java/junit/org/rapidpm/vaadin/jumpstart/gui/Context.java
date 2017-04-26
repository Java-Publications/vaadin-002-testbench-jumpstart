package junit.org.rapidpm.vaadin.jumpstart.gui;

import static org.rapidpm.frp.vaadin.addon.testbench.BrowserDriverFunctions.ipSupplierLocalIP;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.BrowserType;
import org.rapidpm.frp.model.Quad;

/**
 * Created by svenruppert on 24.04.17.
 */
public interface Context {

    // IP Vaadin Selenium Hub  http://tb3-hub.intra.itmill.com/

    Supplier<Platform> platformLinux = () -> Platform.LINUX;
    Supplier<Platform> platformWindows = () -> Platform.WINDOWS;
    Supplier<Platform> platformMAC = () -> Platform.MAC;

    Supplier<String> browserFireFox = () -> BrowserType.FIREFOX;
    Supplier<String> browserChrome = () -> BrowserType.CHROME;
    Supplier<String> browserPhantomJS = () -> BrowserType.PHANTOMJS;
    Supplier<String> browserSafarie = () -> BrowserType.SAFARI;

    Supplier<String> localHost = ipSupplierLocalIP;
    Supplier<String> seleniumHubLocal = ipSupplierLocalIP;
    Supplier<String> seleniumHubVaadin = () -> "tb3-hub.intra.itmill.com";

    Supplier<Boolean> runningLocalBrowser = () -> Boolean.TRUE; // on local installed Browser
    Supplier<Boolean> runningSeleniumHub = () -> Boolean.FALSE; // inside SeleniumHub

    static <T1, T2, T3, T4> Quad<T1, T2, T3, T4> nextQuad(T1 a, T2 b, T3 c, T4 d) {
        return new Quad<>(a, b, c, d);
    }

    //browserType, platform, runningLocal, seleniumHubIP
    Supplier<
        Stream<
            Quad<
                Supplier<String>,
                Supplier<Platform>,
                Supplier<Boolean>,
                Supplier<String>>>> streamOfConfig = () ->
        Stream.of(
            //            nextQuad(browserFireFox, platformMAC, runningLocalBrowser, localHost),
            //            nextQuad(browserSafarie, platformMAC, runningLocalBrowser, localHost),
            //            nextQuad(browserFireFox, platformLinux, runningLocalBrowser, localHost),
            //            nextQuad(browserSafarie, platformLinux, runningLocalBrowser, localHost),
            //            nextQuad(browserPhantomJS, platformLinux, runningSeleniumHub, seleniumHubLocal)

            //running - without Vaadin Selenium Hub
            nextQuad(browserChrome, platformMAC, runningLocalBrowser, localHost),
            nextQuad(browserPhantomJS, platformMAC, runningLocalBrowser, localHost),
//
//            nextQuad(browserChrome, platformLinux, runningLocalBrowser, localHost),
//            nextQuad(browserPhantomJS, platformLinux, runningLocalBrowser, localHost),
//
            nextQuad(browserChrome, platformLinux, runningSeleniumHub, seleniumHubLocal),
            nextQuad(browserFireFox, platformLinux, runningSeleniumHub, seleniumHubLocal)


            // running inside Vaadin Selenium Hub
//            nextQuad(browserChrome, platformLinux, runningSeleniumHub, seleniumHubVaadin),
//            nextQuad(browserFireFox, platformLinux, runningSeleniumHub, seleniumHubVaadin),
//            nextQuad(browserPhantomJS, platformLinux, runningSeleniumHub, seleniumHubVaadin),

//            nextQuad(browserChrome, platformWindows, runningSeleniumHub, seleniumHubVaadin), // exception
//            nextQuad(browserFireFox, platformWindows, runningSeleniumHub, seleniumHubVaadin), // exception
//            nextQuad(browserPhantomJS, platformWindows, runningSeleniumHub, seleniumHubVaadin)



            //            nextQuad(browserChrome, platformWindows, runningSeleniumHub, seleniumHubVaadin),// exception test002
            //            nextQuad(browserFireFox, platformWindows, runningSeleniumHub, seleniumHubVaadin), // exception
            //            nextQuad(browserPhantomJS, platformWindows, runningSeleniumHub, seleniumHubVaadin)// exception
        );
}

