package de.mhus.osgi.dev.cxf.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "mhus", name = "dev-cxf", description = "Cxf tests")
@Service
public class CmdDevCxf extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description =
                    "Command to execute\n"
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
    String restLocation = "http://localhost:8181/cxf/booking/";
    
    @Override
    public Object execute2() throws Exception {
        
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
