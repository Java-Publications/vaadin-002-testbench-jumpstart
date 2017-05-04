package org.rapidpm.vaadin.jumpstart.gui.logic.security;

/**
 * Created by svenruppert on 06.04.17.
 */

import java.util.Optional;

import org.rapidpm.vaadin.jumpstart.gui.model.security.User;

public interface LoginService {

    boolean isLoginAllowed(String username, String password);

    Optional<User> loadUser(String username, String password);

}
