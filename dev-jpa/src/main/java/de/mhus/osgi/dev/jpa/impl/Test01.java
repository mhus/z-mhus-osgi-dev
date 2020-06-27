package de.mhus.osgi.dev.jpa.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;

import de.mhus.osgi.api.MOsgi;

public class Test01 {

    public static void test(String provMatch, String[] args) throws MalformedURLException {
        
         
         EntityManager entityManager = null;
         try {
             
             entityManager  = createEntityManager(provMatch);
             
             PageEntry pe = new PageEntry();
             pe.setLinkName( "Entry " + new Date() );
             pe.setLinkDestination( new URL( "http://www.google.com" ) );
    
             System.out.println("PageEntry: " + pe.getLinkName());
             
             if (TestPersistenceUnitInfo.AUTOCOMMIT) {
                 entityManager.persist( pe );
             } else {
                 EntityTransaction entTrans = entityManager.getTransaction();
                 entTrans.begin();
                 entityManager.persist( pe );
                 entTrans.commit();
             }
             
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
     }
    
    public static EntityManager createEntityManager(String provMatch) {
        
        if (provMatch == null)
            provMatch = ".*hiber.*";
        
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
        PersistenceUnitInfo info = new TestPersistenceUnitInfo();
//      PersistenceUnitInfo info = new org.apache.aries.jpa.container.parser.impl.PersistenceUnit(
//              
//              );
        
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

         return entityManager;
    }
    
}
