package com.code.foodapp.connection;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {

    String classes = "net.sourceforge.jtds.jdbc.Driver";

    protected static String ip = "192.168.1.2"; //IP wifi
    protected static String port = "1433";
    protected static String db = "FoodApp";
    protected static String un = "sa";
    protected static String password = "sa";

    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;
        try {
            Class.forName(classes);
            String conUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databaseName=" + db + ";user=" + un + ";password=" + password + ";";
            conn = DriverManager.getConnection(conUrl,un,password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
