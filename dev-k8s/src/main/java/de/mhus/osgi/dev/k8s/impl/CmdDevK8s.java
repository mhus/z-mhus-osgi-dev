package de.mhus.osgi.dev.k8s.impl;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.osgi.api.karaf.AbstractCmd;

// https://github.com/kubernetes-client/java/tree/master/examples/src/main/java/io/kubernetes/client/examples
// https://medium.com/programming-kubernetes/building-stuff-with-the-kubernetes-api-part-2-using-java-ceb8a5ff7920

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

    private WithFabric8 withFabric8;
        
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
    	} else
        if (cmd.equals("fabric8")) {
            if (withFabric8 == null) {
                System.out.println("Start");
                withFabric8 = new WithFabric8();
            } else {
                System.out.println("Stop");
                withFabric8.close();
                withFabric8 = null;
            }
        } else
        if (cmd.equals("my")) {
            MyController.main(parameters);
        } else
        if (cmd.equals("watch")) {
            WatchExample.main(parameters);
            System.out.println("Done");
        }

        return null;
    }

}
