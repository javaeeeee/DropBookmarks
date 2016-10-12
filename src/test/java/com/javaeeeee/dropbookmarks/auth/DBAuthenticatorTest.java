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
package com.javaeeeee.dropbookmarks.auth;

import com.javaeeeee.dropbookmarks.core.User;
import com.javaeeeee.dropbookmarks.db.UserDAO;
import io.dropwizard.auth.basic.BasicCredentials;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class used to test DB-based basic authentication methods.
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
@RunWith(MockitoJUnitRunner.class)
public class DBAuthenticatorTest {

    /**
     * Username for a test user.
     */
    private static final String USERNAME = "Coda";
    /**
     * Password for a test user.
     */
    private static final String PASSWORD = "HALE";
    /**
     * A test user.
     */
    private static final User USER = new User(USERNAME, PASSWORD);
    /**
     * User DAO mock.
     */
    @Mock
    private UserDAO USER_DAO;

    @Mock
    SessionFactory sf;
    @Mock
    Session session;
    /**
     * System under test, an authenticator class in this case.
     */
    private DBAuthenticator sut;

    /**
     * A method to initialize SUT before each test.
     */
    @Before
    public void setUp() {
        sut = new DBAuthenticator(USER_DAO, sf);
    }

    /**
     * Test of authenticate method, of class DBAuthenticator.
     */
    @Test
    public void testAuthenticateOk() throws Exception {
        // given
        when(USER_DAO.findByUsernameAndPassword(USERNAME, PASSWORD))
                .thenReturn(Optional.of(USER));
        when(sf.openSession()).thenReturn(session);

        // when
        Optional<User> optional = 
                sut.authenticate(new BasicCredentials(USERNAME, PASSWORD));

        // then
        verify(USER_DAO).findByUsernameAndPassword(USERNAME, PASSWORD);
        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(USERNAME, optional.get().getUsername());
    }

    /**
     * Test of authenticate method, of class DBAuthenticator.
     */
    @Test
    public void testAuthenticateFailure() throws Exception {
        // given
        when(USER_DAO.findByUsernameAndPassword(USERNAME, PASSWORD))
                .thenReturn(Optional.empty());
        when(sf.openSession()).thenReturn(session);

        // when
        Optional<User> optional
                = sut.authenticate(new BasicCredentials(USERNAME, PASSWORD));

        // then
        verify(USER_DAO).findByUsernameAndPassword(USERNAME, PASSWORD);
        assertNotNull(optional);
        assertFalse(optional.isPresent());
    }
}
