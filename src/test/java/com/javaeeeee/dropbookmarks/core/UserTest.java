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
package com.javaeeeee.dropbookmarks.core;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Dmitry Noranovich javaeeeee at gmail dot com
 *
 * http://hibernate.org/validator/documentation/getting-started/
 */
public class UserTest {

    /**
     * Validation error message if value is null.
     */
    private static final String ERROR_NOT_NULL = "may not be null";
    /**
     * Validation error message if string argument length is incorrect.
     */
    private static final String ERROR_LENGTH = "size must be between 1 and 255";

    /**
     * Validator used for testing purposes.
     */
    private static Validator validator;

    /**
     * Do initialization.
     */
    @BeforeClass
    public static void setUpClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Test of setId method, of class User.
     */
    @Test(expected = NullPointerException.class)
    public void idIsNull() {
        User user = new User("Coda", "1");
        user.setId(null);
    }

    @Test
    public void idIsOK() {
        User user = new User("Coda", "1");
        user.setId(1);
        Set<ConstraintViolation<User>> constraintViolations
                = validator.validate(user);

        assertTrue(constraintViolations.isEmpty());
    }

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

    @Test
    public void constructorOK() {
        User user = new User("Coda", "1");
    }

    /**
     * Test of getUsername method, of class User.
     */
    @Test
    public void testGetUsername() {
    }

    /**
     * Test of setUsername method, of class User.
     */
    @Test
    public void testSetUsername() {
    }

    /**
     * Test of getPassword method, of class User.
     */
    @Test
    public void testGetPassword() {
    }

    /**
     * Test of setPassword method, of class User.
     */
    @Test
    public void testSetPassword() {
    }

    /**
     * Test of addBookmark method, of class User.
     */
    @Test
    public void testAddBookmark() {
    }

    /**
     * Test of hashCode method, of class User.
     */
    @Test
    public void testHashCode() {
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEquals() {
    }

    /**
     * Test of toString method, of class User.
     */
    @Test
    public void testToString() {
    }

}
