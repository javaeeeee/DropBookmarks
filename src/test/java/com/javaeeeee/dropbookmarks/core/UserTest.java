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
package com.javaeeeee.dropbookmarks.core;

import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 *
 * http://hibernate.org/validator/documentation/getting-started/
 */
public class UserTest extends EntityTest {

    /**
     * Test of setId method, of class User.
     */
    @Test(expected = NullPointerException.class)
    public void idIsNull() {
        User user = new User("Coda", "1");
        user.setId(null);
    }

    /**
     * Test of setId method, of class User.
     */
    @Test
    public void idIsOK() {
        User user = new User("Coda", "1");
        user.setId(1);
        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);

        assertTrue(constraintViolations.isEmpty());
    }

    /**
     * Test of the constructor of the User class.
     */
    @Test
    public void usernameIsNull() {
        User user = new User(null, "1");

        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);

        assertFalse(constraintViolations.isEmpty());
        assertEquals(ERROR_NOT_NULL, constraintViolations
                .iterator()
                .next()
                .getMessage());
    }

    /**
     * Test of the constructor of the User class.
     */
    @Test
    public void passwordIsNull() {
        User user = new User("Coda", null);
        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(ERROR_NOT_NULL, constraintViolations
                .iterator()
                .next()
                .getMessage());
    }

    /**
     * Test of the constructor of the User class.
     */
    @Test
    public void constructorOK() {
        User user = new User("Coda", "1");

        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);

        assertTrue(constraintViolations.isEmpty());
    }

    /**
     * Test of setUsername method, of class User.
     */
    @Test
    public void testSetUsernameIsNull() {
        User user = new User("Coda", "1");
        user.setUsername(null);

        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(ERROR_NOT_NULL, constraintViolations
                .iterator()
                .next()
                .getMessage());
    }

    /**
     * Test of setUsername method, of class User.
     */
    @Test
    public void testSetUsernameIsEmpty() {
        User user = new User("Coda", "1");
        user.setUsername("");

        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(ERROR_LENGTH, constraintViolations
                .iterator()
                .next()
                .getMessage());
    }

    /**
     * Test of setUsername method, of class User.
     */
    @Test
    public void testSetUsernameIsOk() {
        User user = new User("Coda", "1");
        user.setUsername("Phil");

        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);

        assertTrue(constraintViolations.isEmpty());
    }

    /**
     * Test of setPassword method, of class User.
     */
    @Test
    public void testSetPasswordIsNull() {
        User user = new User("Coda", "1");
        user.setPassword(null);

        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);

        assertFalse(constraintViolations.isEmpty());
        assertEquals(ERROR_NOT_NULL, constraintViolations
                .iterator()
                .next()
                .getMessage());
    }

    /**
     * Test of setPassword method, of class User.
     */
    @Test
    public void testSetPasswordIsEmpty() {
        User user = new User("Coda", "1");
        user.setPassword("");

        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);

        assertFalse(constraintViolations.isEmpty());
        assertEquals(ERROR_LENGTH, constraintViolations
                .iterator()
                .next()
                .getMessage());
    }

    /**
     * Test of setPassword method, of class User.
     */
    @Test
    public void testSetPasswordIsOk() {
        User user = new User("Coda", "1");
        user.setPassword("2");

        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);

        assertTrue(constraintViolations.isEmpty());
    }

    /**
     * Test of addBookmark method, of class User.
     */
    @Test(expected = NullPointerException.class)
    public void testAddBookmark() {
        User user = new User("Coda", "1");
        user.addBookmark(null);
    }

    /**
     * Test of addBookmark method, of class User.
     */
    @Test
    public void testAddBookmarkIsNull() {
        User user = new User("Coda", "1");
        user.addBookmark(new Bookmark());
        int expectedId = 1;
        user.setId(expectedId);

        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);

        assertTrue(constraintViolations.isEmpty());

        Bookmark bookmark = user.getBookmarks().iterator().next();

        assertEquals(expectedId, bookmark.getUser().getId().intValue());
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEqualsOtherIsNull() {
        User user = new User("Coda", "1");
        assertFalse(user.equals(null));
        assertNotEquals(user.hashCode(), Objects.hashCode(null));
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEqualsOtherIsSame() {
        User user = new User("Coda", "1");
        assertTrue(user.equals(user));
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEqualsOtherIsBookmark() {
        User user = new User("Coda", "1");
        assertFalse(user.equals(new Bookmark()));
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEqualsAnotherUser() {
        User user = new User("Coda", "1");
        User other = new User();
        assertFalse(user.equals(other));
        assertNotEquals(user.hashCode(), other.hashCode());
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEqualsOk() {
        User user = new User("Coda", "1");
        int expectedId = 1;
        user.setId(expectedId);
        User other = new User("Coda", "1");
        other.setId(expectedId);
        assertTrue(user.equals(other));
        assertEquals(user.hashCode(), other.hashCode());
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEqualsIdIsNull() {
        User user = new User("Coda", "1");
        User other = new User("Coda", "1");
        assertTrue(user.equals(other));
        assertEquals(user.hashCode(), other.hashCode());
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEqualsOtherIdIsNull() {
        User user = new User("Coda", "1");
        int expectedId = 1;
        user.setId(expectedId);
        User other = new User("Coda", "1");
        assertFalse(user.equals(other));
        assertNotEquals(user.hashCode(), other.hashCode());
    }
}
