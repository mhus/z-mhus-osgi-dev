package de.mhus.osgi.dev.dev;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.Bundle;

import de.mhus.osgi.api.MOsgi;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "mhus", name = "dev", description = "Dev tools")
@Service
public class CmdDev extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description = "updateall\n" + "stopstartall\n",
            multiValued = false)
    String cmd;

    @Argument(
            index = 1,
            name = "paramteters",
            required = false,
            description = "Parameters",
            multiValued = true)
    String[] parameters;

    @Override
    public Object execute2() throws Exception {

        if (cmd.equals("updateall")) {
            for (Bundle bundle : MOsgi.getBundleContext().getBundles()) {
                if (bundle.getVersion().toString().endsWith(".SNAPSHOT")) {
                    System.out.println(
                            ">>> " + bundle.getSymbolicName() + ":" + bundle.getVersion());
                    try {
                        bundle.update();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        } else if (cmd.equals("stopstartall")) {
            for (Bundle bundle : MOsgi.getBundleContext().getBundles()) {
                if (bundle.getVersion().toString().endsWith(".SNAPSHOT")) {
                    System.out.println(
                            ">>> " + bundle.getSymbolicName() + ":" + bundle.getVersion());
                    try {
                        bundle.stop();
                        bundle.start();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }

        return null;
    }
}
