package org.example.config;

import lombok.Getter;
import org.example.model.Client;
import org.example.model.Planet;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.Properties;

public class HibernateConfig {

    private static final HibernateConfig INSTANCE;

    @Getter
    private final SessionFactory sessionFactory;

    static {
        INSTANCE = new HibernateConfig();
    }

    private HibernateConfig() {
        Properties properties = new Properties();
        properties.put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
        properties.put("hibernate.hikari.dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.put("hibernate.hikari.dataSource.url", System.getenv("GOIT_DB_URL"));
        properties.put("hibernate.hikari.dataSource.user", System.getenv("GOIT_DB_USER"));
        properties.put("hibernate.hikari.dataSource.password", System.getenv("GOIT_DB_PASS"));
        properties.put("hibernate.hikari.maximumPoolSize", "10");
        properties.put("hibernate.hikari.minimumIdle", "2");
        properties.put("hibernate.hikari.idleTimeout", "30000");
        properties.put("hibernate.hikari.connectionTimeout", "20000");
        properties.put("hibernate.hikari.poolName", "MyHikariCPPool");
        properties.put("hibernate.hikari.maxLifetime", "1800000");
        properties.put("hibernate.hikari.initializationFailTimeout", "0");
        properties.put("hibernate.hikari.leakDetectionThreshold", "2000");

        sessionFactory = new Configuration()
                .addProperties(properties)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(Planet.class)
                .buildSessionFactory();
    }

    public static HibernateConfig getInstance() {
        return INSTANCE;
    }

    public void close() {
        sessionFactory.close();
    }
}