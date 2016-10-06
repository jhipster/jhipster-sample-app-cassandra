package io.github.jhipster.sample.web.rest.vm;


import java.util.Set;

import io.github.jhipster.sample.domain.User;
import io.github.jhipster.sample.service.dto.UserDTO;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private String id;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public ManagedUserVM() {
    }

    public ManagedUserVM(User user) {
        super(user);
        this.id = user.getId();
        this.password = null;
    }

    public ManagedUserVM(String id, String login, String password, String firstName, String lastName,
                         String email, boolean activated, String langKey, Set<String> authorities) {
        super(login, firstName, lastName, email, activated, langKey, authorities);
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" +
            "id=" + id +
            "} " + super.toString();
    }
}
