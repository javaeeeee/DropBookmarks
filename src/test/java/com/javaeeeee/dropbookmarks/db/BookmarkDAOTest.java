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

import com.javaeeeee.dropbookmarks.core.Bookmark;
import com.javaeeeee.dropbookmarks.core.User;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.javaeeeee.dropbookmarks.db.DAOTest.SESSION_FACTORY;

/**
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
public class BookmarkDAOTest extends DAOTest {

    /**
     * System under test.
     */
    private BookmarkDAO sut;

    /**
     * Initializations before each test method.
     *
     * @throws LiquibaseException if something is wrong with Liquibase.
     */
    @Override
    public void setUp() throws LiquibaseException {
        liquibase.update("TEST");
        session = SESSION_FACTORY.openSession();
        sut = new BookmarkDAO(SESSION_FACTORY);
        tx = null;
    }

    /**
     * Cleanup after each test method.
     *
     * @throws DatabaseException if there is an error with database access.
     * @throws LockException if two clients try to apply migrations
     * simultaneously.
     */
    @Override
    public void tearDown() throws DatabaseException, LockException {
        liquibase.dropAll();
    }

    /**
     * Test of findByUserId method, of class BookmarkDAO.
     */
    @Test
    public void testFindByUserId() {
        List<Bookmark> bookmarks = null;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Do something here with UserDAO
            bookmarks = sut.findByUserId(1);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }
        assertNotNull(bookmarks);
        assertFalse(bookmarks.isEmpty());
    }

    /**
     * Test of findById method, of class BookmarkDAO.
     */
    @Test
    public void testFindById() {
        String expectedUrl = "https://github.com/javaeeeee/DropBookmarks";
        String expectedDescription = "Repo for this project";
        // An id of a user added by a migration
        int userId = 1;
        // A generated id of a bookmark
        Integer bmId;
        Optional<Bookmark> optional;
        Bookmark bookmark;

        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Add a bookmark
            session
                    .createSQLQuery(
                            "insert into bookmarks "
                            + "values(null, :url, :description, :userId)"
                    )
                    .setString("url", expectedUrl)
                    .setString("description", expectedDescription)
                    .setInteger("userId", userId)
                    .executeUpdate();

            BigInteger result = (BigInteger) session
                    .createSQLQuery(
                            "select id from bookmarks "
                            + "where url = :url "
                            + "and description = :description "
                            + "and user_id = :userId"
                    )
                    .setString("url", expectedUrl)
                    .setString("description", expectedDescription)
                    .setInteger("userId", userId)
                    .uniqueResult();

            bmId = result.intValue();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        assertNotNull(bmId);
        //Reopen session
        session = SESSION_FACTORY.openSession();
        tx = null;

        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Look for a bookmark
            optional = sut.findById(bmId);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        assertNotNull(optional);
        assertTrue(optional.isPresent());
        bookmark = optional.get();
        assertEquals(expectedUrl, bookmark.getUrl());
    }

    /**
     * Test of save method, of class BookmarkDAO.
     */
    @Test
    public void testSave() {
        String expectedUrl = "https://github.com/javaeeeee/DropBookmarks";
        String actualUrl;
        String expectedDescription = "Repo for this project";
        // An id of a user added by a migration
        int userId = 1;
        Integer bmID;
        Bookmark addedBookmark = new Bookmark(expectedUrl, expectedDescription);
        UserDAO userDAO = new UserDAO(SESSION_FACTORY);

        // Add a bookmark
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //obtain a user
            User user = userDAO.findById(userId).get();
            addedBookmark.setUser(user);
            //Save Bookmark
            bmID = sut.save(addedBookmark).getId();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        assertNotNull(bmID);
        //Reopen session
        session = SESSION_FACTORY.openSession();
        tx = null;

        // Extract the bookmark;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            actualUrl = (String) session
                    .createSQLQuery(
                            "select url from bookmarks "
                            + "where id = :id"
                    )
                    .setInteger("id", bmID)
                    .uniqueResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        assertNotNull(actualUrl);
        assertFalse(actualUrl.isEmpty());
        assertEquals(expectedUrl, actualUrl);
    }

    /**
     * Test of delete method, of class BookmarkDAO.
     */
    @Test
    public void testDelete() {
        String expectedUrl = "https://github.com/javaeeeee/DropBookmarks";
        String actualUrl;
        String expectedDescription = "Repo for this project";
        // An id of a user added by a migration
        int userId = 1;
        // A generated id of a bookmark
        Integer bmId;

        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Add a bookmark
            session
                    .createSQLQuery(
                            "insert into bookmarks "
                            + "values(null, :url, :description, :userId)"
                    )
                    .setString("url", expectedUrl)
                    .setString("description", expectedDescription)
                    .setInteger("userId", userId)
                    .executeUpdate();

            BigInteger result = (BigInteger) session
                    .createSQLQuery(
                            "select id from bookmarks "
                            + "where url = :url "
                            + "and description = :description "
                            + "and user_id = :userId"
                    )
                    .setString("url", expectedUrl)
                    .setString("description", expectedDescription)
                    .setInteger("userId", userId)
                    .uniqueResult();

            bmId = result.intValue();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        assertNotNull(bmId);
        //Reopen session
        session = SESSION_FACTORY.openSession();
        tx = null;

        //delete a bookmark
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Delete a bookmark
            sut.delete(bmId);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        //Reopen session
        session = SESSION_FACTORY.openSession();
        tx = null;

        //look for a bookmark
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            actualUrl = (String) session
                    .createSQLQuery(
                            "select url from bookmarks "
                            + "where id = :id"
                    )
                    .setInteger("id", bmId)
                    .uniqueResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        assertNull(actualUrl);
    }

}
