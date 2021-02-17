package world.ucode;

import java.sql.*;

@SuppressWarnings("SqlResolve")
public class RequestDB {
    private static String url = "jdbc:mysql://localhost/productdb?serverTimezone=Europe/Moscow&useSSL=false";
    private static String username = "root";
    private static String password = "161090";

    public static int insert(String[] info) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)){

                String sql = "INSERT INTO pixelizator (fileName, pixSize, algorithm, filter, time) " +
                            "VALUES (?, ?, ?, ?, now())";
                try(PreparedStatement preparedStatement = conn.prepareStatement(sql)){
                    for (int i = 0; i < 4; ++i) {
                        preparedStatement.setString(i + 1, info[i]);
                    }
                    return preparedStatement.executeUpdate();
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return 0;
    }
}
