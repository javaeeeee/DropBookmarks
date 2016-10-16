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
package com.javaeeeee.dropbookmarks.resources;

import com.javaeeeee.dropbookmarks.core.Bookmark;
import com.javaeeeee.dropbookmarks.core.User;
import com.javaeeeee.dropbookmarks.db.BookmarkDAO;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
public class BookmarksResourceTest {

    /**
     * Test user id.
     */
    private static final int USER_ID = 1;
    /**
     * Test user name.
     */
    private static final String USERNAME = "Coda";
    /**
     * Test user password.
     */
    private static final String PASSWORD = "Hale";
    /**
     * The id of the expected bookmark.
     */
    private static final int BOOKMARK_ID = 1;
    /**
     * The URL of the expected bookmark.
     */
    private static final String URL
            = "https://github.com/javaeeeee/DropBookmarks";
    /**
     * Test user.
     */
    private static final User USER = new User(USERNAME, PASSWORD);

    /**
     * Mocks bookmark DAO for resource testing purposes.
     */
    private static final BookmarkDAO BOOKMARK_DAO = mock(BookmarkDAO.class);

    /**
     * Special class that automatically provides user credentials when a
     * resource method is accessed.
     */
    private static final HttpAuthenticationFeature FEATURE
            = HttpAuthenticationFeature.basic(USERNAME, PASSWORD);

    /**
     * Fake authenticator.
     */
    public static final Authenticator<BasicCredentials, User> AUTHENTICATOR
            = new Authenticator<BasicCredentials, User>() {
        @Override
        public Optional<User> authenticate(BasicCredentials credentials)
                throws AuthenticationException {
            return Optional.of(USER);
        }

    };

    /**
     * Basic authentication filter based on fake authenticator.
     */
    private static final BasicCredentialAuthFilter FILTER
            = new BasicCredentialAuthFilter.Builder<User>()
            .setAuthenticator(credentials -> Optional.of(USER))
            .setAuthorizer((principal, role) -> true)
            .setRealm("SECURITY REALM")
            .buildAuthFilter();

    /**
     * Instruction to spin up in-memory server to test resource classes.
     *
     */
    @ClassRule
    public static final ResourceTestRule RULE
            = ResourceTestRule
            .builder()
            .addProvider(new AuthDynamicFeature(FILTER))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addResource(new BookmarksResource(BOOKMARK_DAO))
            .build();

    /**
     * A bookmark for testing purposes.
     */
    private Bookmark expectedBookmark;
    /**
     * A list of bookmarks for testing purposes.
     */
    private List<Bookmark> bookmarks;

    /**
     * Methods that provides class-level initialization.
     */
    @BeforeClass
    public static void beforeClass() {
        USER.setId(USER_ID);
        // Enable authomatic authentication.
        RULE.getJerseyTest().client().register(FEATURE);
    }

    /**
     * Initialization before each method.
     */
    @Before
    public void setUp() {
        bookmarks = new ArrayList<>();
        expectedBookmark = new Bookmark(
                "https://bitbucket.org/dnoranovich/dropbookmarks",
                "Old project version");
        expectedBookmark.setId(2);
        bookmarks.add(expectedBookmark);

        expectedBookmark = new Bookmark(URL, "The repository of this project");
        expectedBookmark.setId(BOOKMARK_ID);
        bookmarks.add(expectedBookmark);
    }

    /**
     * Clean up after each method.
     */
    @After
    public void dearDown() {
        reset(BOOKMARK_DAO);
        bookmarks.clear();
    }

    /**
     * Test of getBookmarks method, of class BookmarksResource.
     */
    @Test
    public void testGetBookmarks() {
        // given
        when(BOOKMARK_DAO.findByUserId(USER_ID))
                .thenReturn(Collections.unmodifiableList(bookmarks));

        // when
        final List<Bookmark> response = RULE
                .getJerseyTest()
                .target("/bookmarks")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Bookmark>>() {
                });

        //then
        verify(BOOKMARK_DAO).findByUserId(USER_ID);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(bookmarks.size(), response.size());
        assertTrue(response.containsAll(bookmarks));
    }

    /**
     * Test of getBookmark method, of class BookmarksResource.
     */
    @Test
    public void testGetBookmarkFound() {
        // given
        when(BOOKMARK_DAO.findByIdAndUserId(BOOKMARK_ID, USER_ID))
                .thenReturn(Optional.of(expectedBookmark));

        // when
        final Optional<Bookmark> response
                = RULE
                .getJerseyTest()
                .target("/bookmarks/" + BOOKMARK_ID)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<Optional<Bookmark>>() {
                });

        // then
        verify(BOOKMARK_DAO).findByIdAndUserId(BOOKMARK_ID, USER_ID);
        assertNotNull(response);
        assertTrue(response.isPresent());
        assertEquals(expectedBookmark, response.get());
    }

    /**
     * Test of getBookmark method, of class BookmarksResource.
     */
    @Test(expected = NotFoundException.class)
    public void testGetBookmarkNotFound() {
        // given
        when(BOOKMARK_DAO.findByIdAndUserId(BOOKMARK_ID, USER_ID))
                .thenReturn(Optional.empty());

        // when
        final Optional<Bookmark> response
                = RULE
                .getJerseyTest()
                .target("/bookmarks/" + BOOKMARK_ID)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<Optional<Bookmark>>() {
                });

        // then
        verify(BOOKMARK_DAO).findByIdAndUserId(BOOKMARK_ID, USER_ID);
        assertNotNull(response);
        assertFalse(response.isPresent());
    }

    /**
     * Test of addBookmark method, of class BookmarksResource.
     */
    @Test
    public void testAddBookmarkOK() {
        ArgumentCaptor<Bookmark> argumentCaptor
                = ArgumentCaptor.forClass(Bookmark.class);

        // given
        when(BOOKMARK_DAO.save(any(Bookmark.class
        )))
                .thenReturn(expectedBookmark);

        // when
        final Bookmark response
                = RULE
                .getJerseyTest()
                .target("/bookmarks")
                .request(MediaType.APPLICATION_JSON)
                .post(
                        Entity.entity(
                                expectedBookmark,
                                MediaType.APPLICATION_JSON),
                        Bookmark.class);
        // then
        assertNotNull(response);
        verify(BOOKMARK_DAO)
                .save(argumentCaptor.capture());

        Bookmark value = argumentCaptor.getValue();
        assertNotNull(value);
        assertNotNull(value.getUser());
        assertEquals(value.getUser(), USER);

        assertEquals(expectedBookmark, response);
    }

    /**
     * Test of addBookmark method, of class BookmarksResource.
     */
    @Test
    public void testAddBookmarkInvalid() {
        final Response response
                = RULE
                .getJerseyTest()
                .target("/bookmarks")
                .request(MediaType.APPLICATION_JSON)
                .post(
                        Entity.json(
                                new Bookmark(null, null)));

        assertEquals(422, response.getStatus());
    }

    /**
     * Test of modifyBookmark method, of class BookmarksResource.
     */
    @Test
    public void testModifyBookmarkOK() {
        String expectedURL
                = "https://github.com/javaeeeee/SpringBootBookmarks";
        ArgumentCaptor<Bookmark> argumentCaptor
                = ArgumentCaptor.forClass(Bookmark.class);

        // given
        when(BOOKMARK_DAO.findByIdAndUserId(BOOKMARK_ID, USER_ID))
                .thenReturn(Optional.of(expectedBookmark));
        when(BOOKMARK_DAO.save(expectedBookmark))
                .thenReturn(expectedBookmark);

        // when
        Bookmark response = RULE
                .getJerseyTest()
                .target("/bookmarks/" + BOOKMARK_ID)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(
                        new Bookmark(expectedURL, null),
                        MediaType.APPLICATION_JSON),
                        Bookmark.class);

        // then
        assertNotNull(response);
        assertEquals(expectedURL, response.getUrl());
        assertEquals(expectedBookmark.getDescription(),
                response.getDescription());
        assertEquals(expectedBookmark.getUser(),
                response.getUser());

        verify(BOOKMARK_DAO).save(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(expectedURL, argumentCaptor.getValue().getUrl());
        assertEquals(expectedBookmark.getDescription(),
                argumentCaptor.getValue().getDescription());
        assertEquals(expectedBookmark.getUser(),
                argumentCaptor.getValue().getUser());
    }

    /**
     * Test of modifyBookmark method, of class BookmarksResource.
     */
    @Test(expected = NotFoundException.class)
    public void testModifyBookmarkNotFound() {
        // given
        when(BOOKMARK_DAO.findByIdAndUserId(BOOKMARK_ID, USER_ID))
                .thenReturn(Optional.empty());

        // when
        RULE
                .getJerseyTest()
                .target("/bookmarks/" + BOOKMARK_ID)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(
                        expectedBookmark,
                        MediaType.APPLICATION_JSON),
                        Bookmark.class);

        // then
    }

    /**
     * Test of modifyBookmark method, of class BookmarksResource.
     */
    @Test
    public void testModifyBookmarkInvalid() {
        String expectedKey = "wrongKey";

        // given
        when(BOOKMARK_DAO.findByIdAndUserId(BOOKMARK_ID, USER_ID))
                .thenReturn(Optional.of(expectedBookmark));

        // when
        Response response = RULE
                .getJerseyTest()
                .target("/bookmarks/" + BOOKMARK_ID)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(expectedKey,
                        MediaType.APPLICATION_JSON));

        // then
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatus());

        verify(BOOKMARK_DAO, times(0)).save(any(Bookmark.class));
    }

    /**
     * Test of modifyBookmark method, of class BookmarksResource.
     */
    @Test(expected = ProcessingException.class)
    public void testModifyBookmarkInvalidException() {
        String expectedKey = "wrongKey";

        // given
        when(BOOKMARK_DAO.findByIdAndUserId(BOOKMARK_ID, USER_ID))
                .thenReturn(Optional.of(expectedBookmark));

        // when
        Bookmark response = RULE
                .getJerseyTest()
                .target("/bookmarks/" + BOOKMARK_ID)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(expectedKey,
                        MediaType.APPLICATION_JSON))
                .readEntity(Bookmark.class);

        // then
    }

    /**
     * Test of deleteBookmark method, of class BookmarksResource.
     */
    @Test
    public void testDeleteBookmarkOK() {
        // given
        when(BOOKMARK_DAO.findByIdAndUserId(BOOKMARK_ID, USER_ID))
                .thenReturn(Optional.of(expectedBookmark));

        //when
        Bookmark response = RULE
                .getJerseyTest()
                .target("/bookmarks/" + BOOKMARK_ID)
                .request(MediaType.APPLICATION_JSON)
                .delete(Bookmark.class);

        //then
        assertNotNull(response);
        assertEquals(expectedBookmark, response);

        verify(BOOKMARK_DAO).delete(BOOKMARK_ID);
    }

    /**
     * Test of deleteBookmark method, of class BookmarksResource.
     */
    @Test(expected = NotFoundException.class)
    public void testDeleteBookmarkNotFound() {
        // given
        when(BOOKMARK_DAO.findByIdAndUserId(BOOKMARK_ID, USER_ID))
                .thenReturn(Optional.empty());

        // when
        RULE
                .getJerseyTest()
                .target("/bookmarks/" + BOOKMARK_ID)
                .request(MediaType.APPLICATION_JSON)
                .delete(Bookmark.class);

        // then
    }

    /**
     * Test of purgeMap() method
     */
    @Test
    public void testPurgeMap() {
        String expectedKey = "url";
        BookmarksResource sut = new BookmarksResource(BOOKMARK_DAO);
        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put(expectedKey, "http://www.dropwizard.io/1.0.2/docs/");
        map.put("description", null);

        sut.purgeMap(map);

        assertNotNull(map);
        assertFalse(map.isEmpty());
        assertEquals(1, map.size());
        assertTrue(map.containsKey(expectedKey));
    }
}
