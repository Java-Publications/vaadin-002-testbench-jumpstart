package org.rapidpm.vaadin.testbench.addon;

import static org.rapidpm.frp.memoizer.Memoizer.memoize;
import static org.rapidpm.frp.vaadin.addon.testbench.BrowserDriverFunctions.ipSupplierLocalIP;
import static org.rapidpm.microservice.MainUndertow.DEFAULT_SERVLET_PORT;
import static org.rapidpm.microservice.MainUndertow.MYAPP;
import static org.rapidpm.microservice.MainUndertow.SERVLET_PORT_PROPERTY;

import java.util.function.Supplier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.rapidpm.dependencies.core.net.PortUtils;
import org.rapidpm.microservice.MainUndertow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by svenruppert on 23.04.17.
 */
public class MicroserviceTestUtils {

    public static void setUpMicroserviceProperties() {
        final PortUtils portUtils = new PortUtils();

        System.setProperty(MainUndertow.REST_HOST_PROPERTY, ipSupplierLocalIP.get());
        System.setProperty(MainUndertow.SERVLET_HOST_PROPERTY, ipSupplierLocalIP.get());

        System.setProperty(MainUndertow.REST_PORT_PROPERTY, portUtils.nextFreePortForTest() + "");
        System.setProperty(MainUndertow.SERVLET_PORT_PROPERTY, portUtils.nextFreePortForTest() + "");
    }


    public static  Supplier<String> memoizedLocalIpSupplier = memoize(ipSupplierLocalIP);

    public static Supplier<String> baseURL =
        ()-> "http://"
             + memoizedLocalIpSupplier.get() + ":"
//             + memoizedServletPortSupplier.get() + "/"
             + System.getProperty(SERVLET_PORT_PROPERTY) + "/"
             + MYAPP;

}
