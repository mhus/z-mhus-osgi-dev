/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.osgi.dev.micro;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.osgi.service.component.annotations.Component;

import de.mhus.lib.annotations.strategy.OperationDescription;
import de.mhus.lib.core.operation.AbstractOperation;
import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.OperationResult;
import de.mhus.lib.core.operation.Successful;
import de.mhus.lib.core.operation.TaskContext;

@Component(service = Operation.class)
@OperationDescription(title = "Authenticated Only",version="1.0.0")
@RequiresAuthentication
public class AuthenticationOnlyOperation extends AbstractOperation {

    @Override
    protected OperationResult doExecute2(TaskContext context) throws Exception {
        return new Successful(this);
    }

}
