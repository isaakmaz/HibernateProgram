package com.example.userservice.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static StandardServiceRegistry standardServiceRegistry;
    private static SessionFactory sessionFactory;

    public static void shutdown() {
        if (standardServiceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(standardServiceRegistry);
        }
    }

    static {
        if (sessionFactory == null) {
            try {
                standardServiceRegistry = new StandardServiceRegistryBuilder()
                        .configure()
                        .build();
                MetadataSources metadataSources = new MetadataSources(standardServiceRegistry);
                Metadata metadata = metadataSources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                logger.error("Ошибка при создании SessionFactory", e);
                if (standardServiceRegistry != null) {
                    StandardServiceRegistryBuilder.destroy(standardServiceRegistry);
                }
            }
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}