/*
 * The MIT License
 *
 * Copyright 2016 Dmitry Noranovich <javaeeeee (at) gmail (dot) com>.
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
package com.javaeeeee.dropbookmarks;

import com.javaeeeee.dropbookmarks.auth.DBAuthenticator;
import com.javaeeeee.dropbookmarks.core.Bookmark;
import com.javaeeeee.dropbookmarks.core.User;
import com.javaeeeee.dropbookmarks.db.BookmarkDAO;
import com.javaeeeee.dropbookmarks.db.UserDAO;
import com.javaeeeee.dropbookmarks.resources.BookmarksResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;

/**
 * Dropwizard Application class.
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
public class DropBookmarksApplication
        extends Application<DropBookmarksConfiguration> {

    /**
     * Create Hibernate bundle.
     */
    private final HibernateBundle<DropBookmarksConfiguration> hibernateBundle
            = new HibernateBundle<DropBookmarksConfiguration>(
                    User.class,
                    Bookmark.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(
                DropBookmarksConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    /**
     * The application main method.
     * @param args
     * @throws Exception 
     */
    public static void main(final String[] args) throws Exception {
        new DropBookmarksApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropBookmarks";
    }

    @Override
    public void initialize(
            final Bootstrap<DropBookmarksConfiguration> bootstrap) {
        /**
         * Adding Hibernate bundle.
         */
        bootstrap.addBundle(hibernateBundle);
        /**
         * Adding migrations bundle.
         */
        bootstrap.addBundle(new MigrationsBundle<DropBookmarksConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(
                    DropBookmarksConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final DropBookmarksConfiguration configuration,
            final Environment environment) {
        // Create DAOs.
        final UserDAO userDAO
                = new UserDAO(hibernateBundle.getSessionFactory());
        final BookmarkDAO bookmarkDAO
                = new BookmarkDAO(hibernateBundle.getSessionFactory());

        // Create an authenticator which is using the backing database
        // to check credentials.
        final DBAuthenticator authenticator
                = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(DBAuthenticator.class,
                        new Class<?>[]{UserDAO.class, SessionFactory.class},
                        new Object[]{userDAO,
                            hibernateBundle.getSessionFactory()});

        // Register authenticator.
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(authenticator)
                .setAuthorizer((User principal, String role)-> true
                        /*
                        new Authorizer<User>() {
                    @Override
                    public boolean authorize(User principal, String role) {
                        return true;
                    }
                }*/)
                .setRealm("SECURITY REALM")
                .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        //Necessary if @Auth is used to inject a custom Principal
        // type into your resource
        environment.jersey().register(
                new AuthValueFactoryProvider.Binder<>(User.class));

        // Register the Bookmark Resource.
        environment.jersey().register(new BookmarksResource(bookmarkDAO));
    }

}
