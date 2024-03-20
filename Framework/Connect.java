package etu1748.framework;

import java.sql.*;

public class Connect {
    public static Connection postgres() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test_framework", "test",
                "test");
        return connect;
    }
}