package junit.org.rapidpm.vaadin.jumpstart.gui;

import org.junit.Assert;
import org.rapidpm.dependencies.core.net.PortUtils;
import org.rapidpm.microservice.MainUndertow;
import static org.rapidpm.frp.vaadin.addon.testbench.BrowserDriverFunctions.ipSupplierLocalIP;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonType;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonObject;

/**
 * Created by svenruppert on 23.04.17.
 */
public class MicroserviceTestUtils {


    // TODO moce to MicroService client module
    public void setUpMicroserviceProperties() {
        final PortUtils portUtils = new PortUtils();

        System.setProperty(MainUndertow.REST_HOST_PROPERTY, ipSupplierLocalIP.get());
        System.setProperty(MainUndertow.SERVLET_HOST_PROPERTY, ipSupplierLocalIP.get());

        System.setProperty(MainUndertow.REST_PORT_PROPERTY, portUtils.nextFreePortForTest() + "");
        System.setProperty(MainUndertow.SERVLET_PORT_PROPERTY, portUtils.nextFreePortForTest() + "");
    }



    public String requestInfos(String seleniumHubRequestURL){

        //http://tb3-hub.intra.itmill.com:4444/grid/api/proxy?id=http://172.16.11.50:5555
        Client client = ClientBuilder.newClient();
        String val = client
            .target(seleniumHubRequestURL)
            .request()
            .get(String.class);
        System.out.println("val = " + val);
        client.close();
        return val;
    }

    public static void main(String[] args) {
        String vaadin = "http://tb3-hub.intra.itmill.com:4444/grid/api/proxy?id=http://172.16.11.50:5555";
        String uglyJSONString = new MicroserviceTestUtils().requestInfos(vaadin);


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);


        System.out.println("requestInfos = " + prettyJsonString);
    }

}
