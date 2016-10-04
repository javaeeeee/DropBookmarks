/*
 * The MIT License
 *
 * Copyright 2016 Dmitry Noranovich javaeeeee at gmail dot com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.javaeeeee.dropbookmarks.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 * http://stackoverflow.com/questions/36964923/cant-get-hibernateutil-work
 * http://stackoverflow.com/questions/33005348/hibernate-5-org-hibernate-mappingexception-unknown-entity
 *
 * @author Dmitry Noranovich javaeeeee at gmail dot com
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            StandardServiceRegistry standardRegistry
                    = new StandardServiceRegistryBuilder().
                    configure("hibernate.cfg.xml")
                    .build();
            Metadata metadata
                    = new MetadataSources(standardRegistry)
                    .getMetadataBuilder().
                    build();
            sessionFactory = metadata
                    .getSessionFactoryBuilder()
                    .build();
        } catch (Throwable ex) {
            // Log the exception.
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Produces session factory from the hibernate.cfg.xml configuration file.
     *
     * @return Hibernate session factory.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
