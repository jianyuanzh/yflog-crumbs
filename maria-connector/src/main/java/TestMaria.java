import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by vincent on 4/26/16.
 */
public class TestMaria {
    public static void main(String[] args) throws ClassNotFoundException {
                Class.forName("org.mariadb.jdbc.Driver");
        try {
//            Connection conn = DriverManager.getConnection("jdbc:mysql://172.16.133.16::::3306/testmaria", "root", "");
            Connection conn = DriverManager.getConnection("JDBC:MYSQL://10.130.161.1:3306/?connectTimeout=10000&socketTimeout=10000", "root", "root");
            Statement statement = conn.createStatement();
//            statement.executeUpdate("insert into colors(name, rgb) value('test23','blck')");
            statement.executeQuery(" select name,value,id as newid from santaba.configurations");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
