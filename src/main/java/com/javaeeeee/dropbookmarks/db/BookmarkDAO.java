/*
 * The MIT License
 *
 * Copyright 2016 Dmitry Noranovich <javaeeeee at gmail dot com>.
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
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;

/**
 * Data Access Object to manipulate bookmarks.
 *
 * @author Dmitry Noranovich javaeeeee at gmail dot com
 */
public class BookmarkDAO extends AbstractDAO<Bookmark> {

    public BookmarkDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Find bookmarks for a particular user.
     *
     * @param id the id of the user.
     * @return List of all bookmarks stored by the user identified by id.
     */
    public List<Bookmark> findByUserId(long id) {
        return list(namedQuery("Bookmark.findAll"));
    }

    /**
     * Method to find the bookmark with a particular id.
     *
     * @param id the id of a bookmark.
     * @return An Optional with a bookmark if found and an empty Optional
     * otherwise.
     */
    public Optional<Bookmark> findById(long id) {
        return Optional.ofNullable(get(id));
    }

    /**
     * Method saves a bookmark; either creates new or modifies an existent one.
     *
     * @param bookmark a bookmark to be saved.
     * @return the saved bookmark updated with data generated by the database.
     */
    public Bookmark save(Bookmark bookmark) {
        return persist(bookmark);
    }

    /**
     * Method removes the bookmark from the database.
     *
     * @param bookmark a bookmark to remove.
     * @return the removed bookmark.
     */
    public Bookmark delete(Bookmark bookmark) {
        namedQuery("Bookmark.remove")
                .setParameter("id", bookmark.getId())
                .executeUpdate();
        return bookmark;
    }
}
