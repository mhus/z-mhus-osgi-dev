package de.mhus.osgi.dev.micro;

import de.mhus.lib.annotations.service.ServiceComponent;
import de.mhus.lib.annotations.strategy.OperationService;
import de.mhus.lib.core.operation.AbstractOperation;
import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.OperationResult;
import de.mhus.lib.core.operation.Successful;
import de.mhus.lib.core.operation.TaskContext;

@ServiceComponent(service = Operation.class)
@OperationService(title = "Another")
public class AnotherOperation extends AbstractOperation {

    @Override
    protected OperationResult doExecute2(TaskContext context) throws Exception {
        return new Successful(this);
    }

}
