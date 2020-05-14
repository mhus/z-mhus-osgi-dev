package de.mhus.osgi.dev.k8s.impl;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.osgi.api.karaf.AbstractCmd;

// https://github.com/kubernetes-client/java/tree/master/examples/src/main/java/io/kubernetes/client/examples


@Command(scope = "mhus", name = "dev-k8s", description = "Kubernetes tests")
@Service
public class CmdDevK8s extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description =
                    "Command to execute\n"
                    + "  list"
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
        
    @Override
    public Object execute2() throws Exception {
        
        if (cmd.equals("proto")) {
            ProtoExample.main(parameters);
            System.out.println("Done");
        } else
        if (cmd.equals("simple")) {
            SimpleExample.main(parameters);
            System.out.println("Done");
        } else
    	if (cmd.equals("controller")) {
    		ControllerExample.main(parameters);
    		System.out.println("Done");
    	}

        return null;
    }

}
