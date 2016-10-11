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
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import java.security.Principal;
import java.util.Optional;

/**
 * Class for authenticating users using backing database.
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
public class DBAuthenticator implements Authenticator<BasicCredentials, User> {

    /**
     * Reference to User DAO to check whether the user with credentials
     * specified exists in the application's backing database.
     */
    private final UserDAO userDAO;

    /**
     * A constructor to initialize DAO.
     *
     * @param userDAO
     */
    public DBAuthenticator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Implementation of the authenticate method.
     *
     * @param credentials An instance of the BasicCredentials class containing
     * username and password.
     * @return An Optional containing the user characterized by credentials or
     * an empty optional otherwise.
     * @throws AuthenticationException
     */
    @Override
    public Optional<User> authenticate(BasicCredentials credentials)
            throws AuthenticationException {
        return userDAO
                .findByUsernameAndPassword(
                        credentials.getUsername(),
                        credentials.getPassword());
    }

}
