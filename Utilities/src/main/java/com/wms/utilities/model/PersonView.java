package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonView {
    private int id;
    private String name;
    private String password;
    private String role;
    private String authorityString;
    private String post;

    @Basic
    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "Role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "AuthorityString")
    public String getAuthorityString() {
        return authorityString;
    }

    public void setAuthorityString(String authorityString) {
        this.authorityString = authorityString;
    }

    @Basic
    @Column(name = "Post")
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonView that = (PersonView) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (authorityString != null ? !authorityString.equals(that.authorityString) : that.authorityString != null)
            return false;
        if (post != null ? !post.equals(that.post) : that.post != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (authorityString != null ? authorityString.hashCode() : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        return result;
    }
}
