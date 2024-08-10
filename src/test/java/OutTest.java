import kall.entity.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OutTest {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/DATA_ZKSQ";
    private static final String USER = "root";
    private static final String PASSWORD = "12138kk;";

    public static void main(String[] args) {

    }
    public void insertTest(Data data,Connection connection){
        PreparedStatement preparedStatement = null;

        try {
            // 1. 加载MySQL驱动程序
//            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 建立数据库连接
//            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

            // 3. 定义 SQL 插入语句
            String sql = "INSERT INTO data (stationName, stationCode,year) VALUES ( ?, ?,?)";

            // 4. 创建 PreparedStatement 对象
            preparedStatement = connection.prepareStatement(sql);

            // 5. 设置参数值


            // 第一个 ? 对应 id
            preparedStatement.setString(1, data.getStationName());   // 第二个 ? 对应 name
            preparedStatement.setString(2, data.getStationCode());       // 第三个 ? 对应 age
            preparedStatement.setInt(3,data.getYear());
            // 6. 执行插入操作
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭资源
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
