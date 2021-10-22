package com.ex.ers.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="ers_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    protected int user_id;

    @Column(columnDefinition = "VARCHAR(20)", unique = true)
    protected String username;

    @Column(columnDefinition = "VARCHAR(20)")
    protected String password;

    @Column(columnDefinition = "VARCHAR(40)", unique = true)
    protected String email;

    @Column(columnDefinition = "BOOLEAN",name="isemployee")
    protected boolean employee;

    @OneToMany(mappedBy="requestor_id", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Request> requests;

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmployee() {
        return employee;
    }

    public void setIsEmployee(boolean isEmployee) {
        this.employee = isEmployee;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isEmployee=" + employee +
                '}';
    }
}
