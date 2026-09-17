package com.lostfound;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DB {
    private static final String URL="jdbc:mysql://localhost:3306/lost_found_db?useSSL=false&serverTimezone=Asia/Kolkata";
    private static final String USER="root";
    private static final String PASSWORD="Gayatri@123"; // CHANGE THIS
    static { try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch(Exception e){ throw new RuntimeException(e); } }
    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL,USER,PASSWORD); }
}