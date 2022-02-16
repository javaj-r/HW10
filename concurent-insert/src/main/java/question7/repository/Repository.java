package question7.repository;

import question7.database.DatabaseConnection;
import question7.model.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Repository {

    private final DatabaseConnection connection;
    private static final String insertQuery = "INSERT INTO thread_test(content) values (?);";

    public Repository(DatabaseConnection connection) {
        this.connection = connection;
    }

    public void save(Entity entity) {
        Connection connection = this.connection.acquire();
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, entity.getContent());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.release(connection);
        }
    }
}
