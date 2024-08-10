import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OutTest {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/DATA_ZKSQ";
    private static final String USER = "root";
    private static final String PASSWORD = "12138kk;";

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. 加载MySQL驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 建立数据库连接
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

            // 3. 定义 SQL 插入语句
            String sql = "INSERT INTO data (stationName, stationCode) VALUES ( ?, ?)";

            // 4. 创建 PreparedStatement 对象
            preparedStatement = connection.prepareStatement(sql);

            // 5. 设置参数值
            int id = 1;
            String name = "John Doe";
            int age = 30;

                // 第一个 ? 对应 id
            preparedStatement.setString(1, name);   // 第二个 ? 对应 name
            preparedStatement.setInt(2, 121);       // 第三个 ? 对应 age

            // 6. 执行插入操作
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭资源
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
