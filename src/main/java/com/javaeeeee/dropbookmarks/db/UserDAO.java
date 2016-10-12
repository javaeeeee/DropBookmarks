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

import com.javaeeeee.dropbookmarks.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;

/**
 * Data Access Object to manipulate users.
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
public class UserDAO extends AbstractDAO<User> {

    /**
     * The constructor of user DAO which initializes Hibernate session factory
     * defined by the superclass.
     *
     * @param sessionFactory Hibernate session factory
     */
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Method finds all users.
     *
     * @return List of all registered users.
     */
    public List<User> findAll() {
        return list(namedQuery("User.findAll"));
    }

    /**
     * Method looks for a user with given credentials for authentication
     * purposes.
     *
     * @param username username used for login.
     * @param password password of a user.
     * @return An Optional containing the user if found or empty otherwise.
     */
    public Optional<User> findByUsernameAndPassword(
            String username,
            String password
    ) {
        return Optional.ofNullable(
                uniqueResult(
                        namedQuery("User.findByUsernameAndPassword")
                        .setParameter("username", username)
                        .setParameter("password", password)
                ));
    }

    /**
     * A method that finds a user by id. Used for testing purposes.
     *
     * @param id the id of a user.
     * @return The user characterized by the id passed to the method.
     */
    Optional<User> findById(Integer id) {
        return Optional.ofNullable(get(id));
    }
}
