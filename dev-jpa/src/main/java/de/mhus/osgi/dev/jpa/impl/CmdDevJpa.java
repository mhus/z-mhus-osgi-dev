package de.mhus.osgi.dev.jpa.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.SharedCacheMode;
import javax.persistence.TypedQuery;
import javax.persistence.ValidationMode;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.osgi.api.karaf.AbstractCmd;
import de.mhus.osgi.api.services.MOsgi;

@Command(scope = "mhus", name = "dev-jpa", description = "JPA tests")
@Service
public class CmdDevJpa extends AbstractCmd {

    protected static String DS_NAME = "h2";

    @Argument(
            index = 0,
            name = "provider",
            required = true,
            description =
                    "Provider regex"
            ,
            multiValued = false)
    String provMatch;

    @Argument(
            index = 1,
            name = "cmd",
            required = true,
            description =
                    "Command to execute"
            ,
            multiValued = false)
    String cmd;

    @Argument(
            index = 2,
            name = "paramteters",
            required = false,
            description = "Parameters",
            multiValued = true)
    String[] parameters;
    
    @Override
    public Object execute2() throws Exception {
    	
        if (cmd.equals("ds")) {
            DS_NAME = parameters[0];
            return null;
        }
        
        
        if (cmd.equals("test01")) {
            Test01.test(provMatch, parameters);
        }
    	return null;
    }

    
}
