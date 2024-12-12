package com.ruuuuuuuby.domain;

import java.util.Objects;

public class User {
    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get the username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "User{username = " + username + ", password = " + password + "}";
    }
}