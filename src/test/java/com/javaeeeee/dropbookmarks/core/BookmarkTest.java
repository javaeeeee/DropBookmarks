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

import java.util.Set;
import javax.validation.ConstraintViolation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
public class BookmarkTest extends EntityTest {

    public BookmarkTest() {
    }

    /**
     * Test of setId method, of class Bookmark.
     */
    @Test(expected = NullPointerException.class)
    public void testSetIdIsNull() {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(null);
    }

    /**
     * Test of setUrl method, of class Bookmark.
     */
    @Test
    public void testSetUrlIsNull() {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(null);

        Set<ConstraintViolation<Bookmark>> constraintViolations
                = validator.validate(bookmark);

        assertFalse(constraintViolations.isEmpty());
        assertEquals(ERROR_NOT_NULL, constraintViolations
                .iterator()
                .next()
                .getMessage());
    }

    /**
     * Test of setUrl method, of class Bookmark.
     */
    @Test
    public void testSetUrlIsEmpty() {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl("");

        Set<ConstraintViolation<Bookmark>> constraintViolations
                = validator.validate(bookmark);

        assertFalse(constraintViolations.isEmpty());
        assertEquals(ERROR_LENGTH, constraintViolations
                .iterator()
                .next()
                .getMessage());
    }

    /**
     * Test of equals method, of class Bookmark.
     */
    @Test
    public void testEqualsNull() {
        String expectedURL = "https://github.com/javaeeeee/DropBookmarks";
        Bookmark bookmark = new Bookmark(
                expectedURL,
                "Project Repository URL");
        Bookmark other = null;

        assertFalse(bookmark.equals(other));
    }

    /**
     * Test of equals method, of class Bookmark.
     */
    @Test
    public void testEqualsSame() {
        String expectedURL = "https://github.com/javaeeeee/DropBookmarks";
        Bookmark bookmark = new Bookmark(
                expectedURL,
                "Project Repository URL");
        Bookmark other = bookmark;

        assertTrue(bookmark.equals(other));

    }

    /**
     * Test of equals method, of class Bookmark.
     */
    @Test
    public void testEqualsUser() {
        String expectedURL = "https://github.com/javaeeeee/DropBookmarks";
        Bookmark bookmark = new Bookmark(
                expectedURL,
                "Project Repository URL");

        assertFalse(bookmark.equals(new User()));
    }

    /**
     * Test of equals method, of class Bookmark.
     */
    @Test
    public void testEqualsOk() {
        String expectedURL = "https://github.com/javaeeeee/DropBookmarks";
        Bookmark bookmark = new Bookmark(
                expectedURL,
                "Project Repository URL");
        Bookmark other = new Bookmark(
                expectedURL,
                "Project Repository URL");;

        assertTrue(bookmark.equals(other));

    }

    /**
     * Test of equals method, of class Bookmark.
     */
    @Test
    public void testEqualsUsersNotEqual() {
        String expectedURL = "https://github.com/javaeeeee/DropBookmarks";
        Bookmark bookmark = new Bookmark(
                expectedURL,
                "Project Repository URL");
        User u1 = new User();
        u1.setId(1);
        bookmark.setUser(u1);
        Bookmark other = new Bookmark(
                expectedURL,
                "Project Repository URL");
        User u2 = new User();
        u2.setId(2);
        other.setUser(u2);

        assertFalse(bookmark.equals(other));
    }

}
