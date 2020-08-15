package de.mhus.osgi.dev.dev.testit;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Map.Entry;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi;
import de.mhus.osgi.api.util.OsgiBundleClassLoader;

public class SystemShit implements ShitIfc {

    @Override
    public void printUsage() {
        System.out.println("lookup <ifc> [<def>]");
        System.out.println("myip - Print my Ip and hostname");
        System.out.println("env");
        System.out.println("properties");
    }

    @Override
    public Object doExecute(CmdShitYo base, String cmd, String[] parameters) throws Exception {
        if ("myip".equals(cmd)) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("IP Address: " + inetAddress.getHostAddress());
            System.out.println("Host Name : " + inetAddress.getHostName());
        } else if (cmd.equals("lookup")) {
            OsgiBundleClassLoader loader = new OsgiBundleClassLoader();
            Class<?> ifc = loader.loadClass(parameters[0]);
            Object obj = null;
            if (parameters.length > 1) {
                Class<?> def = loader.loadClass(parameters[1]);
                Method method = MApi.class.getMethod("lookup", Class.class, Class.class);
                obj = method.invoke(null, ifc, def);
            } else {
                obj = M.l(ifc);
            }

            if (obj != null) {
                System.out.println(obj.getClass());
            }
            return obj;
        } else if (cmd.equals("env")) {
            for (Entry<String, String> entry : System.getenv().entrySet())
                if (parameters == null
                        || parameters.length < 1
                        || entry.getKey().contains(parameters[0]))
                    System.out.println(entry.getKey() + "=" + entry.getValue());
        } else if (cmd.equals("properties")) {
            for (Entry<Object, Object> entry : System.getProperties().entrySet())
                if (parameters == null
                        || parameters.length < 1
                        || String.valueOf(entry.getKey()).contains(parameters[0]))
                    System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        return null;
    }
}
