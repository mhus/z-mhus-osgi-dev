package de.mhus.osgi.dev.micro;

import org.osgi.service.component.annotations.Component;

import de.mhus.lib.annotations.strategy.OperationService;
import de.mhus.lib.core.operation.AbstractOperation;
import de.mhus.lib.core.operation.NotSuccessful;
import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.OperationResult;
import de.mhus.lib.core.operation.Successful;
import de.mhus.lib.core.operation.TaskContext;
import de.mhus.lib.core.service.IdentUtil;

@Component(service = Operation.class, property = "tags=acl=*")
@OperationService(title = "Hello", path="de.mhus.osgi.dev.critical.micro.Hello",version="1.0.0")
public class HelloOperation1 extends AbstractOperation {

    private String nextMsg = "Moin";

    @Override
    protected OperationResult doExecute2(TaskContext context) throws Exception {
        if (context.getParameters().getBoolean("error", false)) {
            return new NotSuccessful(this, "error", -1);
        }
        String msg = nextMsg;
        nextMsg = context.getParameters().getString("next", nextMsg);
        return new Successful(this, "ok", "msg", msg, "version","1", "ident", IdentUtil.getServerIdent());
    }

}
