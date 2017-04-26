package org.rapidpm.vaadin.jumpstart.gui.model.security;

import java.time.LocalDate;

/**
 * Created by svenruppert on 06.04.17.
 */
public class User {

    public static final User NO_USER = new User("", "", "", LocalDate.MIN);

    private final String username;
    private final String foreName;
    private final String familyName;

    private final LocalDate validUntil;

    public User(String username, String foreName, String familyName,
        LocalDate validUntil) {
        this.username = username;
        this.foreName = foreName;
        this.familyName = familyName;
        this.validUntil = validUntil;
    }

    public String getUsername() {
        return username;
    }

    public String getForeName() {
        return foreName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public User changeUsername(String username) {
        return new User(username, foreName, familyName, validUntil);
    }

    public User changeForeName(String foreName) {
        return new User(username, foreName, familyName, validUntil);
    }

    public User changeFamilyName(String familyName) {
        return new User(username, foreName, familyName, validUntil);
    }

    public User changeValidUntil(LocalDate validUntil) {
        return new User(username, foreName, familyName, validUntil);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;

        if (username != null ?
            !username.equals(user.username) :
            user.username != null)
            return false;
        if (foreName != null ?
            !foreName.equals(user.foreName) :
            user.foreName != null)
            return false;
        if (familyName != null ?
            !familyName.equals(user.familyName) :
            user.familyName != null)
            return false;
        return validUntil != null ?
            validUntil.equals(user.validUntil) :
            user.validUntil == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (foreName != null ? foreName.hashCode() : 0);
        result = 31 * result + (familyName != null ? familyName.hashCode() : 0);
        result = 31 * result + (validUntil != null ? validUntil.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", foreName='"
            + foreName + '\'' + ", familyName='" + familyName + '\''
            + ", validUntil=" + validUntil + '}';
    }
}
