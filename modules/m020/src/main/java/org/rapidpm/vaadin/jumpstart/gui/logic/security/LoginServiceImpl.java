package org.rapidpm.vaadin.jumpstart.gui.logic.security;

import static java.time.LocalDate.of;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import org.rapidpm.vaadin.jumpstart.gui.model.security.User;

/**
 * Created by svenruppert on 06.04.17.
 */
public class LoginServiceImpl implements LoginService {

    private final BiFunction<String, String, User> mapToUser = (username, password) ->
        ("admin".equals(username) && "admin".equals(password)) ?
            new User(username, "Master", "Chief", of(2020, 1, 31)) :
            ("Sven".equals(username) && "123".equals(password)) ?
                new User(username, "Sven", "Ruppert", of(2020, 1, 31)) :
                User.NO_USER;

    private final BiFunction<String, String, Optional<User>> validate = (username, password) ->
        (Objects.isNull(username)) ?
            Optional.empty() :
            (Objects.isNull(password)) ?
                Optional.empty() :
                Optional.of(mapToUser.apply(username, password));

    @Override
    public boolean isLoginAllowed(String username, String password) {
        return !mapToUser.apply(username, password).equals(User.NO_USER);
    }

    @Override
    public Optional<User> loadUser(String username, String password) {
        return validate.apply(username, password);
    }
}
