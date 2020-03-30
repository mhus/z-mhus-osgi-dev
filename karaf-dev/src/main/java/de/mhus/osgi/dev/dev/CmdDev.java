package de.mhus.osgi.dev.dev;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.Bundle;

import de.mhus.osgi.api.karaf.AbstractCmd;
import de.mhus.osgi.api.services.MOsgi;

@Command(scope = "mhus", name = "dev", description = "Dev tools")
@Service
public class CmdDev extends AbstractCmd {

    @Argument(
            index = 0,
            name = "file",
            required = true,
            description = "updateall, stopstartall",
            multiValued = false)
    String cmd;
    
    @Argument(
            index = 1,
            name = "file",
            required = false,
            description = "file name to copy or 'list'",
            multiValued = false)
    String file;

    @Argument(
            index = 2,
            name = "paramteters",
            required = false,
            description = "Parameters",
            multiValued = true)
    String[] parameters;

    @Option(
            name = "-o",
            aliases = "--output",
            required = false,
            description = "Change target",
            multiValued = false)
    String target;

    @Option(
            name = "-t",
            aliases = "--try",
            required = false,
            description = "try run",
            multiValued = false)
    boolean test;
    
    @Option(
            name = "-y",
            aliases = "--yes-overwrite",
            required = false,
            description = "Overwrite files",
            multiValued = false)
    boolean overwrite;
    
    @Override
    public Object execute2() throws Exception {

        if (cmd.equals("updateall")) {
            for (Bundle bundle : MOsgi.getBundleContext().getBundles()) {
                if (bundle.getVersion().toString().endsWith(".SNAPSHOT")) {
                    System.out.println(">>> " + bundle.getSymbolicName() + ":" + bundle.getVersion());
                    try {
                        bundle.update();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        } else
        if (cmd.equals("stopstartall")) {
            for (Bundle bundle : MOsgi.getBundleContext().getBundles()) {
                if (bundle.getVersion().toString().endsWith(".SNAPSHOT")) {
                    System.out.println(">>> " + bundle.getSymbolicName() + ":" + bundle.getVersion());
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
