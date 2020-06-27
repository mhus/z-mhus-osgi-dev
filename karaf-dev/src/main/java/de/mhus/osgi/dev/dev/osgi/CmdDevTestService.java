package de.mhus.osgi.dev.dev.osgi;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.osgi.api.MOsgi;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "mhus", name = "dev-testservice", description = "Dev Test Service Tool")
@Service
public class CmdDevTestService extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description = "sayso",
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

        if (cmd.equals("sayso")) {
            ITestService service = MOsgi.getService(ITestService.class);
            //TestService service = M.l(ITestService.class);
            String res = service.saySo();
            System.out.println("Answer: " + res);
        }
        
        return null;
    }

}
