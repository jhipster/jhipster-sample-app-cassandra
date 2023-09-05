package io.github.jhipster.sample.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.DefaultBatchType;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import io.github.jhipster.sample.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * Spring Data Cassandra repository for the {@link User} entity.
 */
@Repository
public class UserRepository {

    private final CqlSession session;

    private final Validator validator;

    private UserDao userDao;

    private PreparedStatement findOneByActivationKeyStmt;

    private PreparedStatement findOneByResetKeyStmt;

    private PreparedStatement insertByActivationKeyStmt;

    private PreparedStatement insertByResetKeyStmt;

    private PreparedStatement deleteByActivationKeyStmt;

    private PreparedStatement deleteByResetKeyStmt;

    private PreparedStatement findOneByLoginStmt;

    private PreparedStatement insertByLoginStmt;

    private PreparedStatement deleteByLoginStmt;

    private PreparedStatement findOneByEmailStmt;

    private PreparedStatement insertByEmailStmt;

    private PreparedStatement deleteByEmailStmt;

    private PreparedStatement truncateStmt;

    private PreparedStatement truncateByResetKeyStmt;

    private PreparedStatement truncateByLoginStmt;

    private PreparedStatement truncateByEmailStmt;

    public UserRepository(CqlSession session, Validator validator, CassandraProperties cassandraProperties) {
        this.session = session;
        this.validator = validator;
        UserTokenMapper userTokenMapper = new UserTokenMapperBuilder(session).build();
        userDao = userTokenMapper.userTokenDao(CqlIdentifier.fromCql(cassandraProperties.getKeyspaceName()));

        findOneByActivationKeyStmt =
            session.prepare("SELECT id " + "FROM user_by_activation_key " + "WHERE activation_key = :activation_key");

        findOneByResetKeyStmt = session.prepare("SELECT id " + "FROM user_by_reset_key " + "WHERE reset_key = :reset_key");

        insertByActivationKeyStmt =
            session.prepare("INSERT INTO user_by_activation_key (activation_key, id) " + "VALUES (:activation_key, :id)");

        insertByResetKeyStmt = session.prepare("INSERT INTO user_by_reset_key (reset_key, id) " + "VALUES (:reset_key, :id)");

        deleteByActivationKeyStmt = session.prepare("DELETE FROM user_by_activation_key " + "WHERE activation_key = :activation_key");

        deleteByResetKeyStmt = session.prepare("DELETE FROM user_by_reset_key " + "WHERE reset_key = :reset_key");

        findOneByLoginStmt = session.prepare("SELECT id " + "FROM user_by_login " + "WHERE login = :login");

        insertByLoginStmt = session.prepare("INSERT INTO user_by_login (login, id) " + "VALUES (:login, :id)");

        deleteByLoginStmt = session.prepare("DELETE FROM user_by_login " + "WHERE login = :login");

        findOneByEmailStmt = session.prepare("SELECT id " + "FROM user_by_email " + "WHERE email     = :email");

        insertByEmailStmt = session.prepare("INSERT INTO user_by_email (email, id) " + "VALUES (:email, :id)");

        deleteByEmailStmt = session.prepare("DELETE FROM user_by_email " + "WHERE email = :email");

        truncateStmt = session.prepare("TRUNCATE user");

        truncateByResetKeyStmt = session.prepare("TRUNCATE user_by_reset_key");

        truncateByLoginStmt = session.prepare("TRUNCATE user_by_login");

        truncateByEmailStmt = session.prepare("TRUNCATE user_by_email");
    }

    public Optional<User> findById(String id) {
        return userDao.get(id);
    }

    public Optional<User> findOneByActivationKey(String activationKey) {
        BoundStatement stmt = findOneByActivationKeyStmt.bind().setString("activation_key", activationKey);
        return findOneFromIndex(stmt);
    }

    public Optional<User> findOneByResetKey(String resetKey) {
        BoundStatement stmt = findOneByResetKeyStmt.bind().setString("reset_key", resetKey);
        return findOneFromIndex(stmt);
    }

    public Optional<User> findOneByEmailIgnoreCase(String email) {
        BoundStatement stmt = findOneByEmailStmt.bind().setString("email", email.toLowerCase());
        return findOneFromIndex(stmt);
    }

    public Optional<User> findOneByLogin(String login) {
        BoundStatement stmt = findOneByLoginStmt.bind().setString("login", login);
        return findOneFromIndex(stmt);
    }

    public List<User> findAll() {
        return userDao.findAll().all();
    }

    public User save(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        User oldUser = userDao.get(user.getId()).orElse(null);
        if (oldUser != null) {
            if (!StringUtils.isEmpty(oldUser.getActivationKey()) && !oldUser.getActivationKey().equals(user.getActivationKey())) {
                session.execute(deleteByActivationKeyStmt.bind().setString("activation_key", oldUser.getActivationKey()));
            }
            if (!StringUtils.isEmpty(oldUser.getResetKey()) && !oldUser.getResetKey().equals(user.getResetKey())) {
                session.execute(deleteByResetKeyStmt.bind().setString("reset_key", oldUser.getResetKey()));
            }
            if (!StringUtils.isEmpty(oldUser.getLogin()) && !oldUser.getLogin().equals(user.getLogin())) {
                session.execute(deleteByLoginStmt.bind().setString("login", oldUser.getLogin()));
            }
            if (!StringUtils.isEmpty(oldUser.getEmail()) && !oldUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                session.execute(deleteByEmailStmt.bind().setString("email", oldUser.getEmail().toLowerCase()));
            }
        }
        BatchStatementBuilder batch = BatchStatement.builder(DefaultBatchType.LOGGED);
        batch.addStatement(userDao.saveQuery(user));
        if (!StringUtils.isEmpty(user.getActivationKey())) {
            batch.addStatement(
                insertByActivationKeyStmt.bind().setString("activation_key", user.getActivationKey()).setString("id", user.getId())
            );
        }
        if (!StringUtils.isEmpty(user.getResetKey())) {
            batch.addStatement(insertByResetKeyStmt.bind().setString("reset_key", user.getResetKey()).setString("id", user.getId()));
        }
        batch.addStatement(insertByLoginStmt.bind().setString("login", user.getLogin()).setString("id", user.getId()));
        batch.addStatement(insertByEmailStmt.bind().setString("email", user.getEmail().toLowerCase()).setString("id", user.getId()));
        session.execute(batch.build());
        return user;
    }

    public void delete(User user) {
        BatchStatementBuilder batch = BatchStatement.builder(DefaultBatchType.LOGGED);
        batch.addStatement(userDao.deleteQuery(user));
        if (!StringUtils.isEmpty(user.getActivationKey())) {
            batch.addStatement(deleteByActivationKeyStmt.bind().setString("activation_key", user.getActivationKey()));
        }
        if (!StringUtils.isEmpty(user.getResetKey())) {
            batch.addStatement(deleteByResetKeyStmt.bind().setString("reset_key", user.getResetKey()));
        }
        batch.addStatement(deleteByLoginStmt.bind().setString("login", user.getLogin()));
        batch.addStatement(deleteByEmailStmt.bind().setString("email", user.getEmail().toLowerCase()));
        session.execute(batch.build());
    }

    private Optional<User> findOneFromIndex(BoundStatement stmt) {
        ResultSet rs = session.execute(stmt);
        return Optional.ofNullable(rs.one()).map(row -> row.getString("id")).flatMap(id -> userDao.get(id));
    }

    public void deleteAll() {
        BoundStatement truncate = truncateStmt.bind();
        session.execute(truncate);

        BoundStatement truncateByEmail = truncateByEmailStmt.bind();
        session.execute(truncateByEmail);

        BoundStatement truncateByLogin = truncateByLoginStmt.bind();
        session.execute(truncateByLogin);

        BoundStatement truncateByResetKey = truncateByResetKeyStmt.bind();
        session.execute(truncateByResetKey);
    }
}

@Dao
interface UserDao {
    @Select
    Optional<User> get(String id);

    @Select
    PagingIterable<User> findAll();

    @Insert
    BoundStatement saveQuery(User user);

    @Delete
    BoundStatement deleteQuery(User user);
}

@Mapper
interface UserTokenMapper {
    @DaoFactory
    UserDao userTokenDao(@DaoKeyspace CqlIdentifier keyspace);
}
