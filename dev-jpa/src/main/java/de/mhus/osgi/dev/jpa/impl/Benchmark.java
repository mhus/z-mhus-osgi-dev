package de.mhus.osgi.dev.jpa.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import de.mhus.db.osgi.api.adb.AdbOsgiUtil;
import de.mhus.db.osgi.api.adb.AdbService;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MStopWatch;
import de.mhus.lib.core.MThread;
import de.mhus.lib.errors.MException;

public class Benchmark {

    

    public static void benchmark(String provMatch, String[] parameters) throws InterruptedException {
        
        EntityManager entityManager = Test01.createEntityManager(provMatch);
        try {
            AdbService service = AdbOsgiUtil.getCommonAdbService();
            DbManager adb = service.getManager();

            System.out.println("Cleanup ADB");
            // cleanup
            while (!service.isConnected()) {
                System.out.println("ADB Wait for connection");
                MThread.sleepInLoop(10000);
            }
            
            for ( AdbPageEntry entry : adb.getAll(AdbPageEntry.class))
                try {
                    entry.delete();
                } catch (MException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            System.out.println("Count: " + adb.count(Db.query(AdbPageEntry.class)));
            
            System.out.println("Cleanup JPA");
            if (TestPersistenceUnitInfo.AUTOCOMMIT) {
                entityManager.createQuery("delete from PageEntry").executeUpdate();
            } else {
                EntityTransaction entTrans = entityManager.getTransaction();
                entTrans.begin();
                entityManager.createQuery("delete from PageEntry").executeUpdate();
                entTrans.commit();
            }
            {
                CriteriaBuilder qb = entityManager.getCriteriaBuilder();
                CriteriaQuery<Long> cq = qb.createQuery(Long.class);
                cq.select(qb.count(cq.from(PageEntry.class)));
                //ParameterExpression<Integer> p = qb.parameter(Integer.class);
                Long cnt = entityManager.createQuery(cq).getSingleResult();
                System.out.println("Count: " + cnt);
            }
            
            System.out.println(">>> Start");
            URL url = new URL( "http://www.google.com" );
            int CNT = MCast.toint(parameters[0], 1000000);
            int READ_LOOPS = MCast.toint(parameters[1], 10);
            
            // WriteLoad Test
            
            {
                MStopWatch watch = new MStopWatch("ADB SaveLoad").start();
                for (int i = 0; i < CNT; i++) {
                    System.out.print(".");
                    AdbPageEntry entry = adb.inject(new AdbPageEntry());
                    entry.setLinkDestination(url);
                    entry.setLinkName( "Entry " + new Date());
                    entry.save();
                    
                    AdbPageEntry loaded = adb.getObject(AdbPageEntry.class, entry.getId());
                    if (!entry.getLinkName().equals(loaded.getLinkName()))
                        throw new MException("ADB Not the same");
                }
                watch.stop();
                System.out.println();
                System.out.println(watch);
            }
            {
                MStopWatch watch = new MStopWatch("JPA SaveLoad").start();
                for (int i = 0; i < CNT; i++) {
                    System.out.print(".");
                    PageEntry entry = new PageEntry();
                    entry.setLinkName( "Entry " + new Date() );
                    entry.setLinkDestination( new URL( "http://www.google.com" ) );
                
                    if (TestPersistenceUnitInfo.AUTOCOMMIT) {
                        entityManager.persist( entry );
                    } else {
                        EntityTransaction entTrans = entityManager.getTransaction();
                        entTrans.begin();
                        entityManager.persist( entry );
                        entTrans.commit();
                    }
                    
                    PageEntry loaded = entityManager.find(PageEntry.class, entry.getId());
                    if (!entry.getLinkName().equals(loaded.getLinkName()))
                        throw new MException("ADB Not the same");
                }
                watch.stop();
                System.out.println();
                System.out.println(watch);
            }
            
            // Read
            {
                MStopWatch watch = new MStopWatch("ADB Read").start();
                for (int i = 0; i < READ_LOOPS; i++) {
                    System.out.print(".");
                    for (AdbPageEntry entry : adb.getAll(AdbPageEntry.class))
                        entry.getLinkName();
                }
                watch.stop();
                System.out.println();
                System.out.println(watch);
            }
            {
                MStopWatch watch = new MStopWatch("JPA Read").start();
                for (int i = 0; i < READ_LOOPS; i++) {
                    System.out.print(".");

                    CriteriaQuery<PageEntry> criteria = entityManager.getCriteriaBuilder().createQuery(PageEntry.class);
                    criteria.select(criteria.from(PageEntry.class));

                    for (PageEntry entry : entityManager.createQuery(criteria).getResultList())
                        entry.getLinkName();
                }
                watch.stop();
                System.out.println();
                System.out.println(watch);
            }
            
            // Delete
            {
                int cnt = 0;
                MStopWatch watch = new MStopWatch("ADB Delete").start();
                for (AdbPageEntry entry : adb.getAll(AdbPageEntry.class)) {
                    System.out.print(".");
                    entry.delete();
                    cnt++;
                }
                watch.stop();
                System.out.println();
                System.out.println(cnt + " " + watch);
            }
            {
                int cnt = 0;
                // entityManager.flush();
                MStopWatch watch = new MStopWatch("JPA Delete").start();
                CriteriaQuery<PageEntry> criteria = entityManager.getCriteriaBuilder().createQuery(PageEntry.class);
                criteria.select(criteria.from(PageEntry.class));
                for (PageEntry entry : entityManager.createQuery(criteria).getResultList()) {
                    System.out.print(".");
                    if (TestPersistenceUnitInfo.AUTOCOMMIT) {
                        entityManager.remove( entry );
                    } else {
                        EntityTransaction entTrans = entityManager.getTransaction();
                        entTrans.begin();
                        entityManager.remove( entry );
                        entTrans.commit();
                    }
                    cnt++;
                }
                watch.stop();
                System.out.println();
                System.out.println(cnt + " " + watch);
            }
            
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        
        
        
    }
}
