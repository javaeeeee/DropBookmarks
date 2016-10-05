/*
 * The MIT License
 *
 * Copyright 2016 Dmitry Noranovich javaeeeee (at) gmail (dot) com.
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

import java.sql.Connection;
import java.sql.SQLException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.internal.SessionImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * An abstract class which contains all the necessary initializations for
 * testing Dropwizard DAO classes. Intended to be a superclass for DAOs tests.
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
public abstract class DAOTest {

    /**
     * Hibernate session factory.
     */
    protected static final SessionFactory SESSION_FACTORY
            = HibernateUtil.getSessionFactory();
    /**
     * A handle to apply Liquibase DB refactorings programmatically.
     */
    protected static Liquibase liquibase = null;

    /**
     * Initializations before all test methods.
     * http://myjourneyonjava.blogspot.ca/2014/12/different-ways-to-get-connection-object.html
     *
     * @throws LiquibaseException if something is wrong with Liquibase.
     * @throws SQLException if there is an error with database access.
     */
    @BeforeClass
    public static void setUpClass() throws LiquibaseException, SQLException {
        final Session session = SESSION_FACTORY.openSession();
        final SessionImpl sessionImpl = (SessionImpl) session;
        final Connection connection = sessionImpl.connection();
        final Database database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(
                        new JdbcConnection(connection)
                );
        liquibase = new Liquibase(
                "migrations.xml",
                new ClassLoaderResourceAccessor(),
                database
        );
        session.close();
    }

    /**
     * Clean up after all test methods.
     */
    @AfterClass
    public static void tearDownClass() {
        //SESSION_FACTORY.close();
    }
    /**
     * Hibernate session.
     */
    protected Session session;
    /**
     * Hibernate transaction.
     */
    protected Transaction tx;

    /**
     * Initializations before each test method.
     *
     * @throws LiquibaseException if something is wrong with Liquibase.
     */
    @Before
    public abstract void setUp() throws LiquibaseException;

    /**
     * Cleanup after each test method.
     *
     * @throws DatabaseException if there is an error with database access.
     * @throws LockException if two clients try to apply migrations
     * simultaneously.
     */
    @After
    public abstract void tearDown() throws DatabaseException, LockException;

}
