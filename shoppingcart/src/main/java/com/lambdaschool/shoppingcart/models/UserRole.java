package com.lambdaschool.shoppingcart.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "userroles")
@IdClass(UserRoleId.class)
public class UserRole extends Auditable implements Serializable
{
    @Id
    @ManyToOne
    @JoinColumn(name="userid")
    @JsonIgnoreProperties(value = "roles", allowSetters = true)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name="roleid")
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Role role;

    public UserRole() {
    }

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return ((user == null) ? 0 : user.getUserid()) == ((userRole.user == null) ? 0 : userRole.user.getUserid()) &&
                ((role == null) ? 0 : role.getRoleid()) == ((userRole.role == null) ? 0 : userRole.role.getRoleid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser().getUserid(), getRole().getRoleid());
    }
}
