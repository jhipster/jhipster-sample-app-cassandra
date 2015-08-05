package com.mycompany.myapp.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.mycompany.myapp.domain.PersistentToken;
import com.mycompany.myapp.domain.User;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Cassandra repository for the PersistentToken entity.
 */
@Repository
public class PersistentTokenRepository {

    @Inject
    private Session session;

    Mapper<PersistentToken> mapper;

    private PreparedStatement findPersistentTokenSeriesByUserIdStmt;

    private PreparedStatement insertPersistentTokenSeriesByUserIdStmt;

    private PreparedStatement insertPersistentTokenStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(PersistentToken.class);

        findPersistentTokenSeriesByUserIdStmt = session.prepare(
            "SELECT persistent_token_series " +
            "FROM persistent_token_by_user " +
            "WHERE user_id = :user_id");

        insertPersistentTokenSeriesByUserIdStmt = session.prepare(
            "INSERT INTO persistent_token_by_user (user_id, persistent_token_series) " +
                "VALUES (:user_id, :persistent_token_series) " +
                "USING TTL 2592000"); // 30 days

        insertPersistentTokenStmt = session.prepare(
            "INSERT INTO persistent_token (series, token_date, user_agent, token_value, login, user_id, ip_address) " +
                "VALUES (:series, :token_date, :user_agent, :token_value, :login, :user_id, :ip_address) " +
                "USING TTL 2592000"); // 30 days
    }

    public PersistentToken findOne(String presentedSeries) {
        return mapper.get(presentedSeries);
    }

    public List<PersistentToken> findByUser(User user) {
        BoundStatement stmt = findPersistentTokenSeriesByUserIdStmt.bind();
        stmt.setString("user_id", user.getId());
        ResultSet rs = session.execute(stmt);
        List<PersistentToken> persistentTokens = new ArrayList<>();
        rs.all().stream()
            .map(row -> row.getString("persistent_token_series"))
            .map(token -> mapper.get(token))
            .forEach(persistentTokens::add);

        return persistentTokens;
    }

    public void save(PersistentToken token) {
        BatchStatement batch = new BatchStatement();
        batch.add(insertPersistentTokenStmt.bind()
            .setString("series", token.getSeries())
            .setDate("token_date", token.getTokenDate())
            .setString("user_agent", token.getUserAgent())
            .setString("token_value", token.getTokenValue())
            .setString("login", token.getLogin())
            .setString("user_id", token.getUserId())
            .setString("ip_address", token.getIpAddress()));
        batch.add(insertPersistentTokenSeriesByUserIdStmt.bind()
            .setString("user_id", token.getUserId())
            .setString("persistent_token_series", token.getSeries()));
        session.execute(batch);
    }

    public void delete(PersistentToken token) {
        mapper.delete(token);
    }

    public void delete(String decodedSeries) {
        mapper.delete(decodedSeries);
    }
}
