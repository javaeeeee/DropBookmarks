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
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.internal.SessionImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * A class to test UserDAO class.
 *
 * @author Dmitry Noranovich javaeeeee at gmail dot com
 */
public class UserDAOTest {

    /**
     * Hibernate session factory.
     */
    private static final SessionFactory SESSION_FACTORY
            = HibernateUtil.getSessionFactory();
    /**
     * A handle to apply Liquibase DB refactorings programmatically.
     */
    private static Liquibase liquibase = null;
    /**
     * Hibernate session.
     */
    private Session session;
    /**
     * Hibernate transaction.
     */
    private Transaction tx;
    /**
     * Class to perform CRUD database operations with users. System Under Test.
     */
    private UserDAO sut;

    /**
     * Initializations before all test methods.
     * http://myjourneyonjava.blogspot.ca/2014/12/different-ways-to-get-connection-object.html
     *
     * @throws LiquibaseException if something is wrong with Liquibase.
     * @throws SQLException if there is an error with database access.
     */
    @BeforeClass
    public static void setUpClass() throws LiquibaseException, SQLException {

        Session session = SESSION_FACTORY.openSession();
        SessionImpl sessionImpl = (SessionImpl) session;
        Connection connection = sessionImpl.connection();
        Database database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(
                        new JdbcConnection(connection));

        liquibase
                = new Liquibase(
                        "migrations.xml",
                        new ClassLoaderResourceAccessor(),
                        database);

    }

    /**
     * Clean up after all test methods.
     */
    @AfterClass
    public static void tearDownClass() {
        SESSION_FACTORY.close();
    }

    /**
     * Initializations before each test method.
     *
     * @throws LiquibaseException if something is wrong with Liquibase.
     */
    @Before
    public void setUp() throws LiquibaseException {
        liquibase.update("DEV");
        session = SESSION_FACTORY.openSession();
        sut = new UserDAO(SESSION_FACTORY);
        tx = null;
    }

    /**
     * Cleanup after each test method.
     *
     * @throws DatabaseException if there is an error with database access.
     * @throws LockException if two clients try to apply migrations
     * simultaneously.
     */
    @After
    public void tearDown() throws DatabaseException, LockException {
        liquibase.dropAll();
    }

    /**
     * Test of findAll method, of class UserDAO.
     */
    @Test
    public void testFindAll() {
    }

    /**
     * Test of findByUsernameAndPassword method, of class UserDAO.
     */
    @Test
    public void testFindByUsernameAndPassword() {
    }

}
