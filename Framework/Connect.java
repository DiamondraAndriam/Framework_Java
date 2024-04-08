package etu1748.framework;

import java.io.FileReader;
import java.sql.*;

import com.google.gson.*;

public class Connect {
    private String user;
    private String password;
    private String url;
    private String driver;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public static Connection connect() throws Exception {
        JsonParser jsonParser = new JsonParser();
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println("Path:" + path);
        JsonElement jsonData = jsonParser.parse(new FileReader(path+"/config.json"));
        Gson gson = new Gson();
        Connect con = gson.fromJson(jsonData, Connect.class);
        System.out.println(con.getUrl());
        Class.forName(con.driver);
        Connection connect = DriverManager.getConnection(con.url, con.user, con.password);
        return connect;
    }
}