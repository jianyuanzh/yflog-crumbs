import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by vincent on 4/26/16.
 */
public class TestMysql {
    public static void main(String[] args)  {
        try {
            Class.forName("com.mysql.jdbc.Driver");
//            Connection conn = DriverManager.getConnection("jdbc:mysql://172.16.133.16:::::3306?connectTimeout=10000&socketTimeout=10000", "root", "");
            Connection conn = DriverManager.getConnection("JDBC:MYSQL://10.130.161.1/santaba?connectTimeout=10000&socketTimeout=10000", "root", "root");
            Statement statement = conn.createStatement();
//            statement.executeUpdate("insert into colors(name, rgb) value('test23','blck')");
            statement.executeQuery(" select name,value,id as newid from configurations");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
