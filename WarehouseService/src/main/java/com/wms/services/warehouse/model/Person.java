package com.wms.services.warehouse.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Person {
    private int id;
    private String name;
    private String password;
    private String role;
    private String authorityString;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Name", nullable = false, length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Password", nullable = false, length = 64)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "Role", nullable = false, length = 64)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "AuthorityString", nullable = false, length = 128)
    public String getAuthorityString() {
        return authorityString;
    }

    public void setAuthorityString(String authorityString) {
        this.authorityString = authorityString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id &&
                Objects.equals(name, person.name) &&
                Objects.equals(password, person.password) &&
                Objects.equals(role, person.role) &&
                Objects.equals(authorityString, person.authorityString);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, password, role, authorityString);
    }
}
