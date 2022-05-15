package com.company;
import com.mysql.cj.util.DnsSrv;

import java.io.File;
import java.sql.*;

public class DB {

    private static final String url = "jdbc:mysql://localhost:3306/mydb";
    private static final String user = "root";
    private static final String password = "кщще";
    private static final String db_driver= "C:\\Program Files (x86)\\MySQL\\Connector J 8.0\\mysql-connector-java-8.0.28.jar";
    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;


    public DB(){


    }
    static Connection dbConnection;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://localhost:3306/mydb";

        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, user, password);

        return dbConnection;
    }

    public static void addDir(String Dirn,String patch) throws SQLException, ClassNotFoundException {

        String query = "insert into dir (DirName,patch)" + " values (?, ?)";
        var conn =getConnection();
        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString (1, Dirn);
        preparedStmt.setString (2, patch);

        // execute the preparedstatement
        preparedStmt.execute();
        conn.close();
    }
    public static void addFile(String Fn,String patch) throws SQLException, ClassNotFoundException {

        var dirid=GetDirId(patch);
        var conn =getConnection();
        String query2 = "insert into file (Fname,Patch,Dir_ID)" + " values (?, ?, ?)";
        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt2 = conn.prepareStatement(query2);
        preparedStmt2.setString (1, Fn);
        preparedStmt2.setString (2, patch);
        preparedStmt2.setInt (3, dirid);

        // execute the preparedstatement
        preparedStmt2.execute();
        conn.close();
    }
    public static void addWord(String Word,String Fpatch,Integer count) throws SQLException, ClassNotFoundException {
        //
        var dirid=GetFId(Fpatch);
        var conn =getConnection();
        String query2 = "insert into word (Word,Count,File_ID)" + " values (?, ?, ?)";
        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt2 = conn.prepareStatement(query2);
        preparedStmt2.setString (1, Word);
        preparedStmt2.setInt (2, count);
        preparedStmt2.setInt (3, dirid);

        // execute the preparedstatement
        preparedStmt2.execute();
        conn.close();
    }

    private static Integer GetDirId(String patch) throws SQLException, ClassNotFoundException {
        var conn =getConnection();
        String dirPatch=patch.substring(0,patch.lastIndexOf(File.separator));
        String query = "SELECT ID FROM dir WHERE patch = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, dirPatch);
        ResultSet resultSet = preparedStmt.executeQuery();
        //preparedStmt.clearParameters();
        int id = 0;
        while (resultSet.next())
        {
            id = resultSet.getInt("ID");
        }
        return id;
    }

    private static Integer GetFId(String patch) throws SQLException, ClassNotFoundException {
        var conn =getConnection();

        String query = "SELECT ID FROM file WHERE patch = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, patch);
        ResultSet resultSet = preparedStmt.executeQuery();

        int id = 0;
        while (resultSet.next())
        {
            id = resultSet.getInt("ID");
        }
        return id;
    }
}
