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
package com.javaeeeee.dropbookmarks.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dmitry Noranovich javaeeeee at gmail dot com
 */
@Entity
@Table(name = "bookmarks", catalog = "bookmarks", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bookmark.findAll",
            query = "SELECT b FROM Bookmark b"),
    @NamedQuery(name = "Bookmark.findById",
            query = "SELECT b FROM Bookmark b WHERE b.id = :id"),
    @NamedQuery(name = "Bookmark.findByUrl",
            query = "SELECT b FROM Bookmark b WHERE b.url = :url"),
    @NamedQuery(name = "Bookmark.findByDescription",
            query = "SELECT b FROM Bookmark b "
            + "WHERE b.description = :description"),
    @NamedQuery(name = "Bookmark.findByUserId",
            query = "SELECT b FROM Bookmark b WHERE b.user.userId = :userId"),
@NamedQuery(name = "Bookmark.remove", query = "REMOVE FROM Bookmark b "
        + "where b.id = :id")})
public class Bookmark implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    /**
     * Bookmark URL.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "url")
    private String url;
    /**
     * Bookmark description.
     */
    @Size(max = 2048)
    @Column(name = "description")
    private String description;
    /**
     * The owner of the bookmark.
     */
    @Basic(optional = false)
    @JsonIgnore
    @ManyToOne
    private User user;

    public Bookmark() {
    }

    public Bookmark(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id,
                this.url,
                this.description,
                this.user);
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
        final Bookmark other = (Bookmark) obj;
        return Objects.equals(this.user, other.user)
                && Objects.equals(this.url, other.url)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Bookmark{" + "id=" + id + ", url=" + url
                + ", description=" + description
                + ", user=" + Objects.toString(user) + '}';
    }

}
