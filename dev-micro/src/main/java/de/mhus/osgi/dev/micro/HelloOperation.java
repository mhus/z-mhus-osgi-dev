package de.mhus.osgi.dev.micro;

import org.osgi.service.component.annotations.Component;

import de.mhus.lib.annotations.strategy.OperationService;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.operation.AbstractOperation;
import de.mhus.lib.core.operation.NotSuccessful;
import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.OperationResult;
import de.mhus.lib.core.operation.Successful;
import de.mhus.lib.core.operation.TaskContext;
import de.mhus.lib.core.service.IdentUtil;
import de.mhus.lib.form.IFormProvider;
import de.mhus.lib.form.definition.FaMandatory;
import de.mhus.lib.form.definition.FmCheckbox;
import de.mhus.lib.form.definition.FmText;

@Component(service = Operation.class, property = "tags=acl=*")
@OperationService(title = "Hello", path="de.mhus.osgi.dev.critical.micro.Hello",strictParameterCheck=true)
public class HelloOperation extends AbstractOperation implements IFormProvider {

    private String nextMsg = "Moin";

    @Override
    protected OperationResult doExecute2(TaskContext context) throws Exception {
        if (context.getParameters().getBoolean("error", false)) {
            return new NotSuccessful(this, "error", -1);
        }
        String msg = nextMsg;
        nextMsg = context.getParameters().getString("next", nextMsg);
        return new Successful(this, "ok", "msg", msg, "version","0", "ident", IdentUtil.getServerIdent());
    }

    @Override
    public DefRoot getForm() {
        return new DefRoot(
                new FmCheckbox("error", "Error" ,"Set to true fi you want a not successful result")
                ,
                new FmText("next", "Next Message", "Set the following result message", new FaMandatory() )
                );
    }

}
