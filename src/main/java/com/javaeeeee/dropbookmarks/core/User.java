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
package com.javaeeeee.dropbookmarks.core;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dmitry Noranovich javaeeeee (at) gmail (dot) com
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById",
            query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByUsernameAndPassword",
            query = "SELECT u FROM User u WHERE u.username = :username "
            + "and u.password = :password")})
public class User implements Principal, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    /**
     * Username for the login operation.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    private String username;
    /**
     * User's password.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    /**
     * List of user's bookmarks.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<Bookmark> bookmarks = new HashSet<>();

    /**
     * A no-argument constructor.
     */
    public User() {
    }

    /**
     * Constructor to create users.
     * @param username the username.
     * @param password the password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method adds a bookmark to the user's collection.
     *
     * @param bookmark a bookmark to add.
     */
    public void addBookmark(final Bookmark bookmark) {
        Objects.requireNonNull(bookmark);
        bookmark.setUser(this);
        bookmarks.add(bookmark);
    }

    /**
     * Getter for bookmark list.
     *
     * @return the list by bookmarks stored by a user.
     */
    public Set<Bookmark> getBookmarks() {
        return bookmarks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id,
                this.username,
                this.password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.username, other.username)
                && Objects.equals(this.password, other.password)
                && Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username
                + ", password=" + password
                + ", bookmarks=" + bookmarks.size()
                + '}';
    }

    /**
     * Method implementation from Principal interface.
     *
     * @return The name of the Principal.
     */
    @Override
    public String getName() {
        return username;
    }

}
