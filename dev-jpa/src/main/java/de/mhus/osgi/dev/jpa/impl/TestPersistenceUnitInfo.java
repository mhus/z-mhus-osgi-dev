package de.mhus.osgi.dev.jpa.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import de.mhus.osgi.api.util.DataSourceUtil;

public class TestPersistenceUnitInfo implements PersistenceUnitInfo {

    public static boolean AUTOCOMMIT = false;
    public static String HIBERNATE_DIALECT = null;

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
        DataSource ds = DataSourceUtil.getDataSource(CmdDevJpa.DS_NAME);
        System.out.println("JtaDataSource: " + ds);
        return ds;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        DataSource ds = DataSourceUtil.getDataSource(CmdDevJpa.DS_NAME);
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
            return Collections.list(this.getClass().getClassLoader().getResources(""));
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
        prop.put("hibernate.hbm2ddl.auto", "create-only");
        if (HIBERNATE_DIALECT != null) prop.put("hibernate.dialect", HIBERNATE_DIALECT);
        if (AUTOCOMMIT) prop.put("hibernate.connection.autocommit", "true");
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
    public void addTransformer(ClassTransformer transformer) {}

    @Override
    public ClassLoader getNewTempClassLoader() {
        return getClassLoader();
    }
}
