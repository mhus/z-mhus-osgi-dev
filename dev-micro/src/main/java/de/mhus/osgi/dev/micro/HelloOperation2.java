package de.mhus.osgi.dev.micro;

import org.osgi.service.component.annotations.Component;

import de.mhus.lib.annotations.strategy.OperationService;
import de.mhus.lib.core.base.service.IdentUtil;
import de.mhus.lib.core.strategy.AbstractOperation;
import de.mhus.lib.core.strategy.NotSuccessful;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.Successful;
import de.mhus.lib.core.strategy.TaskContext;

@Component(service = Operation.class, property = "tags=acl=*")
@OperationService(title = "Hello", path="de.mhus.osgi.dev.critical.micro.Hello",version="2.0.0")
public class HelloOperation2 extends AbstractOperation {

    private String nextMsg = "Moin";

    @Override
    protected OperationResult doExecute2(TaskContext context) throws Exception {
        if (context.getParameters().getBoolean("error", false)) {
            return new NotSuccessful(this, "error", -1);
        }
        String msg = nextMsg;
        nextMsg = context.getParameters().getString("next", nextMsg);
        return new Successful(this, "ok", "msg", msg, "version","2", "ident", IdentUtil.getServerIdent());
    }

}
