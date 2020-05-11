package de.mhus.osgi.dev.cxf.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.WriterInterceptor;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jaxrs.runtime.dto.ResourceDTO;
import org.osgi.service.jaxrs.runtime.dto.ResourceMethodInfoDTO;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import de.mhus.osgi.api.karaf.AbstractCmd;
import de.mhus.osgi.api.services.MOsgi;

@Command(scope = "mhus", name = "dev-cxf", description = "Cxf tests")
@Service
public class CmdDevCxf extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description =
                    "Command to execute\n"
                    + " BookingService\n"
                    + " Extension\n"
                    + " cxf.list\n"
                    + " http.list\n"
                    + " http.add <id> <flight> <customer>"
                    + ""
            ,
            multiValued = false)
    String cmd;

    @Argument(
            index = 1,
            name = "paramteters",
            required = false,
            description = "Parameters",
            multiValued = true)
    String[] parameters;
    
    @Option(name = "--url", description = "Location of the REST service", required = false, multiValued = false)
    String restLocation = "http://localhost:8181/booking/";

    private static ServiceRegistration<?> extensionRegistration;
    private static ServiceRegistration<BookingService> bookingServiceRegistration;
    
    // https://access.redhat.com/documentation/en-us/red_hat_fuse/7.5/html/apache_cxf_development_guide/jaxrs20filters
    // https://osgi.org/specification/osgi.cmpn/7.0.0/service.jaxrs.html#service.jaxrs.common.properties
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object execute2() throws Exception {
        

        if (cmd.equals("runtime")) {
            for (org.osgi.service.jaxrs.runtime.JaxrsServiceRuntime runtime : MOsgi.getServices(org.osgi.service.jaxrs.runtime.JaxrsServiceRuntime.class, null)) {
                System.out.println(runtime.getClass() +": ");
                for (ResourceDTO res : runtime.getRuntimeDTO().defaultApplication.resourceDTOs) {
                    System.out.println("  " + res.name);
                    for (ResourceMethodInfoDTO meth : res.resourceMethods)
                        System.out.println("    " + meth.method + " " + meth.path);
                }
                
            }
                
        }
        if (cmd.equals("Extension")) {
            if (extensionRegistration == null) {
                BundleContext context = FrameworkUtil.getBundle(CmdDevCxf.class).getBundleContext();
                Dictionary<String, Object> properties = new Hashtable<>();
                properties.put("osgi.jaxrs.extension", true);
                properties.put(MOsgi.COMPONENT_NAME, RsExtension.class.getCanonicalName());
                System.out.println("Register RsExtension");
                extensionRegistration = context.registerService(
                        new String[] {
                        WriterInterceptor.class.getCanonicalName(),
                        ReaderInterceptor.class.getCanonicalName(),
                        ContainerRequestFilter.class.getCanonicalName(),
                        ContainerResponseFilter.class.getCanonicalName()
                        }, new RsExtension(), properties);
            } else {
                System.out.println("Unregister RsExtension");
                extensionRegistration.unregister();
                extensionRegistration = null;
            }
        } else
        if (cmd.equals("BookingService")) {
            if (bookingServiceRegistration == null) {
                BundleContext context = FrameworkUtil.getBundle(CmdDevCxf.class).getBundleContext();
                Dictionary<String, Object> properties = new Hashtable<>();
                properties.put(MOsgi.COMPONENT_NAME, BookingService.class.getCanonicalName());
                properties.put("osgi.jaxrs.resource", true);
                System.out.println("Register SecondBookingServiceRest");
                bookingServiceRegistration = context.registerService(BookingService.class, new SecondBookingServiceRest(), properties);
            } else {
                System.out.println("Unregister SecondBookingServiceRest");
                bookingServiceRegistration.unregister();
                bookingServiceRegistration = null;
            }
        } else
        if (cmd.equals("cxf.list")) {
            
            List providers = new ArrayList();
            providers.add(new JacksonJsonProvider());
            WebClient webClient = WebClient.create(restLocation, providers);

            List<Booking> bookings = (List<Booking>) webClient.accept(MediaType.APPLICATION_JSON).getCollection(Booking.class);
            for (Booking booking : bookings) {
                System.out.println(booking.getId() + " " + booking.getCustomer() + " " + booking.getFlight());
            }
            
        } else
        if (cmd.equals("http.list")) {

            URL url = new URL(restLocation);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = buffer.readLine())!= null) {
                    sb.append(line);
                }
                System.out.println(sb.toString());
            } else {
                System.err.println("Error when sending GET method : HTTP_CODE = " + connection.getResponseCode());
            }
        } else
        if (cmd.equals("http.add")) {
            
            URL url = new URL(restLocation);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            String json = "{"
                    + "\"id\": " + parameters[0] + ","
                    + "\"flight\": \"" + parameters[1] + "\","
                    + "\"customer\": \"" + parameters[2] + "\""
                    + "}";

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(json);
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

        }

        return null;
    }
    

    
}
