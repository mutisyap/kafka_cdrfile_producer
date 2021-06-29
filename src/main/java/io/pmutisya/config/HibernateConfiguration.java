package io.pmutisya.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class HibernateConfiguration {
    private static final String configFileName = "hibernate.cfg.xml";
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            URL fileUrl = classLoader.getResource(configFileName);

            if (fileUrl == null) {
                throw new FileNotFoundException("File " + configFileName + " Does not exist");
            }
            return new Configuration().configure(fileUrl).buildSessionFactory();

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
