package question7.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DatabaseConnection {

    private final Queue<Connection> connections = new ConcurrentLinkedQueue<>();
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    public DatabaseConnection(int connectionNumber) {
        try {
            Class.forName("org.postgresql.Driver");
            addNewConnection(Math.max(connectionNumber, 1));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection acquire(){
        while (true) {
            Connection connection = connections.poll();
            if (connection != null)
                return connection;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void release(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connections.add(connection);
            } else {
                addNewConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addNewConnection(int connectionNumber) {
        for (int i = 0; i < connectionNumber; i++) {
            addNewConnection();
        }
    }

    private void addNewConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connections.add(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
