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
package com.javaeeeee.dropbookmarks;

import com.javaeeeee.dropbookmarks.core.Bookmark;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.ClassRule;

/**
 * A class to test entire application.
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
public class IntegrationTest {

    /**
     * HTTP Status Code 422 Unprocessable Entity. Used to test validation.
     */
    private static final int UNPROCESSABLE_ENTITY_HTTP_RESPONSE_CODE = 422;
    /**
     * A path to test configuration file.
     */
    private static final String CONFIG_PATH
            = ResourceHelpers.resourceFilePath("test-config.yml");

    /**
     * Start the application before all test methods.
     */
    @ClassRule
    public static final DropwizardAppRule<DropBookmarksConfiguration> RULE
            = new DropwizardAppRule<>(
                    DropBookmarksApplication.class,
                    CONFIG_PATH);

    /**
     * Special class that automatically provides user credentials when a
     * resource method is accessed.
     */
    private static final HttpAuthenticationFeature FEATURE
            = HttpAuthenticationFeature.basic("javaeeeee", "p@ssw0rd");

    /**
     * Base path to resources.
     */
    private static String target;

    /**
     * Path to bookmark resources.
     */
    private static final String BOOKMARK_PATH
            = "/bookmarks";
    /**
     * Jersey client to access resources.
     */
    private Client client;

    /**
     * Initialization method run before all test methods.
     */
    @BeforeClass
    public static void setUpClass() {
        target = String.format("http://localhost:%d",
                RULE.getLocalPort());

    }

    /**
     * Configuration method run before each test method.
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        RULE.getApplication()
                .run("db", "migrate", "-i TEST", CONFIG_PATH);
        client = ClientBuilder.newClient();
    }

    /**
     * Do cleanup after each method.
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        client.close();
        RULE.getApplication()
                .run("db",
                        "drop-all",
                        "--confirm-delete-everything",
                        CONFIG_PATH);
    }

    /**
     * Test getBookmarks() method.
     */
    @Test
    public void getBookmarksUnauthorized() {
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),
                response.getStatus());
    }

    /**
     * Test getBookmarks() method.
     */
    @Test
    public void getBookmarksOK() {
        client.register(FEATURE);
        List<Bookmark> response = client.target(target)
                .path(BOOKMARK_PATH)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Bookmark>>() {
                });

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(2, response.size());
    }

    /**
     * Test getBookmark() method.
     */
    @Test
    public void getBookmarkUnathorized() {
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),
                response.getStatus());
    }

    /**
     * Test getBookmark() method.
     */
    @Test
    public void getBookmarkNotFound() {
        client.register(FEATURE);
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .path("109678")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus());
    }

    /**
     * Test getBookmark() method.
     */
    @Test
    public void getBookmarkOK() {
        client.register(FEATURE);
        String expectedDescription = "Dropwizard Getting Started";
        Bookmark response = client.target(target)
                .path(BOOKMARK_PATH)
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .get(Bookmark.class);

        assertNotNull(response);
        assertEquals(expectedDescription, response.getDescription());
    }

    /**
     * Test add bookmark method.
     */
    @Test
    public void addBookmarkUnauthorised() {
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        new Bookmark("http://localhost:8080", "localhost"),
                        MediaType.APPLICATION_JSON));

        assertNotNull(response);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),
                response.getStatus());
    }

    /**
     * Test add bookmark method.
     */
    @Test
    public void addBookmarkInvalid() {
        client.register(FEATURE);
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        new Bookmark(null, ""),
                        MediaType.APPLICATION_JSON));

        assertNotNull(response);
        assertEquals(UNPROCESSABLE_ENTITY_HTTP_RESPONSE_CODE,
                response.getStatus()
        );
    }

    /**
     * Test add bookmark method.
     */
    @Test
    public void addBookmarkOK() {
        client.register(FEATURE);
        String expectedURL = "http://localhost:8080";
        Bookmark response = client.target(target)
                .path(BOOKMARK_PATH)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(new Bookmark(expectedURL, "localhost"),
                        MediaType.APPLICATION_JSON))
                .readEntity(Bookmark.class);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(expectedURL, response.getUrl());
    }

    /**
     * Test delete bookmark method.
     */
    @Test
    public void deleteBookmarkUnauthorized() {
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertNotNull(response);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),
                response.getStatus());
    }

    /**
     * Test delete bookmark method.
     */
    @Test
    public void deleteBookmarkNotFound() {
        client.register(FEATURE);
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .path("109678")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus());
    }

    /**
     * Test delete bookmark method.
     */
    @Test
    public void deleteBookmarkOk() {
        int expestedId = 1;
        client.register(FEATURE);
        Bookmark response = client.target(target)
                .path(BOOKMARK_PATH)
                .path(String.valueOf(expestedId))
                .request(MediaType.APPLICATION_JSON)
                .delete(Bookmark.class);

        assertNotNull(response);
        assertEquals(expestedId, response.getId().intValue());
    }

    /**
     * Test modify bookmark method.
     */
    @Test
    public void modifyBookmarkUnauthorized() {
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(
                        new Bookmark("http://localhost:8080", "localhost"),
                        MediaType.APPLICATION_JSON));

        assertNotNull(response);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),
                response.getStatus());
    }

    /**
     * Test modify bookmark method.
     */
    @Test
    public void modifyBookmarkNotFound() {
        client.register(FEATURE);
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .path("109678")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(
                        new Bookmark("http://localhost:8080", "localhost"),
                        MediaType.APPLICATION_JSON));

        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus());
    }

    /**
     * Test modify bookmark method.
     */
    @Test
    public void modifyBookmarkInvalid() {
        String data = "UNPROCESSABLE_ENTITY";
        client.register(FEATURE);
        Response response = client.target(target)
                .path(BOOKMARK_PATH)
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(data,
                        MediaType.APPLICATION_JSON));

        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatus());
    }

    /**
     * Test modify bookmark method.
     */
    @Test
    public void modifyBookmarkOK() {
        String expectedURL
                = "https://github.com/javaeeeee/SpringBootBookmarks";
        Map<String, String> data = new HashMap<>();
        data.put("url", "https://github.com/javaeeeee/SpringBootBookmarks");
        client.register(FEATURE);
        Bookmark response = client.target(target)
                .path(BOOKMARK_PATH)
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(data))
                .readEntity(Bookmark.class);

        assertNotNull(response);
        assertEquals(expectedURL, response.getUrl());
    }
}
