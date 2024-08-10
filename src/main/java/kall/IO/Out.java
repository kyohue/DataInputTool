package kall.IO;

import kall.entity.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Out {
    public boolean intoDB(List<Data> dataList, Connection connection) {
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO data (stationName, stationCode,latitude,longitude,eoStation,year,month,day,hour,airPressure,temperature,relHumidity,pastOneHour,tMinDirection,tMinSpeed,horizontalVisibility) VALUES ( ?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            int rowsAffected = 0;
            for (Data data : dataList) {
                preparedStatement.setString(1, data.getStationName());
                preparedStatement.setString(2, data.getStationCode());
                preparedStatement.setDouble(3, data.getLatitude());
                preparedStatement.setDouble(4, data.getLongitude());
                preparedStatement.setDouble(5, data.getEoStation());
                preparedStatement.setInt(6, data.getYear());
                preparedStatement.setInt(7, data.getMonth());
                preparedStatement.setInt(8, data.getDay());
                preparedStatement.setInt(9, data.getHour());
                preparedStatement.setDouble(10, data.getAirPressure());
                preparedStatement.setDouble(11, data.getTemperature());
                preparedStatement.setDouble(12, data.getRelHumidity());
                preparedStatement.setDouble(13, data.getPastOneHour());
                preparedStatement.setDouble(14, data.gettMinDirection());
                preparedStatement.setDouble(15, data.gettMinSpeed());
                preparedStatement.setDouble(16, data.getHorizontalVisibility());
                rowsAffected += preparedStatement.executeUpdate();
            }
            if (dataList.isEmpty())
                System.out.println("dataList.size() == 0");
            System.out.println("Rows affected: " + rowsAffected);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return true;
    }
}
