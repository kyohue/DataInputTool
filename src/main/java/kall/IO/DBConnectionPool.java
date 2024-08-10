package kall.IO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

public class DBConnectionPool {
    private static final int INITIAL_POOL_SIZE = 10;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/DATA_ZKSQ";
    private static final String USER = "root";
    private static final String PASSWORD = "12138kk;";
    private final LinkedBlockingQueue<Connection> connections;

    private static class OneInstance{
        private static DBConnectionPool instance = new DBConnectionPool();
    }
    private DBConnectionPool() {
        this.connections = new LinkedBlockingQueue<>();
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                connections.add(createNewConnectionForPool());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static DBConnectionPool getInstance(){
        return OneInstance.instance;
    }
    private Connection createNewConnectionForPool() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public Connection getConnection() {
        if (connections.isEmpty()) {
            try {
                return createNewConnectionForPool();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connections.poll();
    }

    public void releaseConnection(Connection connection){
        if(!connections.offer(connection)){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
