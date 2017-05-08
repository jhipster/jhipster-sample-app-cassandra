package io.github.jhipster.sample.service;

import io.github.jhipster.sample.AbstractCassandraTest;
import io.github.jhipster.sample.JhipsterCassandraSampleApplicationApp;
import io.github.jhipster.sample.domain.User;
import io.github.jhipster.sample.config.Constants;
import io.github.jhipster.sample.repository.UserRepository;
import io.github.jhipster.sample.service.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterCassandraSampleApplicationApp.class)
public class UserServiceIntTest extends AbstractCassandraTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void assertThatAnonymousUserIsNotGet() {
        final List<UserDTO> allManagedUsers = userService.getAllManagedUsers();
        assertThat(allManagedUsers.stream()
            .noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin())))
            .isTrue();
    }
}
