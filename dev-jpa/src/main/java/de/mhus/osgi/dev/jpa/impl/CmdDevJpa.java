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
    	
    	PersistenceProvider persistenceProvider = null;
    	for (PersistenceProvider pp : MOsgi.getServices(PersistenceProvider.class, null)) {
    		System.out.println(">>> " + pp);
    		if (pp.toString().matches(provMatch))
    			persistenceProvider = pp;
    	}
    	if (persistenceProvider == null) {
    		System.out.println("PersistenceProvider not selected");
    		return null;
    	}
    	
    	HashMap<String, Object> createMap = new HashMap<String, Object>();
    	PersistenceUnitInfo info = archiverPersistenceUnitInfo();
//    	PersistenceUnitInfo info = new org.apache.aries.jpa.container.parser.impl.PersistenceUnit(
//    			
//    			);
    	
    	EntityManagerFactory entityManagerFactory = persistenceProvider
    			 .createContainerEntityManagerFactory(info, createMap);
    	 
//                ImmutableMap.<String, Object>builder()
//                        .put(JPA_JDBC_DRIVER, JDBC_DRIVER)
//                        .put(JPA_JDBC_URL, JDBC_URL)
//                        .put(DIALECT, Oracle12cDialect.class)
//                        .put(HBM2DDL_AUTO, CREATE)
//                        .put(SHOW_SQL, false)
//                        .put(QUERY_STARTUP_CHECKING, false)
//                        .put(GENERATE_STATISTICS, false)
//                        .put(USE_REFLECTION_OPTIMIZER, false)
//                        .put(USE_SECOND_LEVEL_CACHE, false)
//                        .put(USE_QUERY_CACHE, false)
//                        .put(USE_STRUCTURED_CACHE, false)
//                        .put(STATEMENT_BATCH_SIZE, 20)
//                        .build()

    	 System.out.println("entityManagerFactory: " + entityManagerFactory);
    	 EntityManager entityManager = entityManagerFactory.createEntityManager();
    
    	 System.out.println("entityManager: " + entityManager);
    	 
    	 //entityManager.flush();
    	 try {
	    	 
	    	 PageEntry pe = new PageEntry();
	    	 pe.setLinkName( "Google " + new Date() );
	    	 pe.setLinkDestination( new URL( "http://www.google.com" ) );
	
	    	 System.out.println("PageEntry: " + pe);
	    	 
	    	 EntityTransaction entTrans = entityManager.getTransaction();
	    	 entTrans.begin();
	    	 entityManager.persist( pe );
	    	 entTrans.commit();
	    	 
	    	 System.out.println("--- Saved");
	    	 
	    	 CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    	 CriteriaQuery<PageEntry> query = cb.createQuery(PageEntry.class);
	    	 Root<PageEntry> root = query.from(PageEntry.class);
	    	 query.select(root);
	    	 TypedQuery<PageEntry> tq = entityManager.createQuery(query);
	    	 List<PageEntry> res = tq.getResultList();
	    	 for (PageEntry item : res) {
	    		 System.out.println(": " + item.getId() + " " + item.getLinkName());
	    	 }
	    	 
    	 } finally {
    		 entityManager.close();
		 }
    	return null;
    }
    
    
    private static PersistenceUnitInfo archiverPersistenceUnitInfo() {
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return "ApplicationPersistenceUnit";
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
            	DataSource ds = MOsgi.getDataSource("booking");
            	System.out.println("JtaDataSource: " + ds);
                return ds;
            }

            @Override
            public DataSource getNonJtaDataSource() {
            	DataSource ds = MOsgi.getDataSource("booking");
            	System.out.println("NonJtaDataSource: " + ds);
                return ds;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<URL> getJarFileUrls() {
                try {
                    return Collections.list(this.getClass()
                                                .getClassLoader()
                                                .getResources(""));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                // return ((URLClassLoader)CmdDevJpa.class.getClassLoader()).getURLs()[0];
            	return null;
            }

            @Override
            public List<String> getManagedClassNames() {
            	LinkedList<String> list = new LinkedList<>();
            	list.add(PageEntry.class.getCanonicalName());
                return list;
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                Properties prop = new Properties();
                // https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#configurations-hbmddl
                prop.put("hibernate.hbm2ddl.auto","create");
                return prop;
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return CmdDevJpa.class.getClassLoader();
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return getClassLoader();
            }
        };
    }
    
}
