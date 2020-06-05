package students;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.*;

import static students.logic.ManagementSystem.printString;

public class TestJDBC
{
    public static void main(String[] args) {
        try{
            System.setOut(new PrintStream("out.txt"));
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            return;
        }

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/students";
            con = DriverManager.getConnection(url, "root", "root");
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()){
                String str = rs.getString(1) + " : " + rs.getString(2) + ":" + rs.getString(3)
                        + ":" + rs.getString(4) + ":" + rs.getString(5) + ":"
                        + ":" + rs.getString(7) + ":" + rs.getString(8);
                printString(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null){
                    rs.close();
                }
                if (stmt != null){
                    stmt.close();
                }
                if (con != null){
                    con.close();
                }
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }

    }
}
