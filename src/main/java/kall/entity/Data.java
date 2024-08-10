package kall.entity;

public class Data {
    private String stationName;
    private String stationCode;
    private double latitude;
    private double longitude;
    private double eoStation;
    private int year;
    private int month;
    private int day;
    private int hour;
    private double airPressure;
    private double temperature;
    private double relHumidity;
    private double pastOneHour;
    private double tMinDirection;
    private double tMinSpeed;
    private double horizontalVisibility;

    @Override
    public String toString() {
        return "Data{" +
                "stationName='" + stationName + '\'' +
                ", stationCode='" + stationCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", eoStation=" + eoStation +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", airPressure=" + airPressure +
                ", temperature=" + temperature +
                ", relHumidity=" + relHumidity +
                ", pastOneHour=" + pastOneHour +
                ", tMinDirection=" + tMinDirection +
                ", tMinSpeed=" + tMinSpeed +
                ", horizontalVisibility=" + horizontalVisibility +
                '}';
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getEoStation() {
        return eoStation;
    }

    public void setEoStation(double eoStation) {
        this.eoStation = eoStation;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(double airPressure) {
        this.airPressure = airPressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getRelHumidity() {
        return relHumidity;
    }

    public void setRelHumidity(double relHumidity) {
        this.relHumidity = relHumidity;
    }

    public double getPastOneHour() {
        return pastOneHour;
    }

    public void setPastOneHour(double pastOneHour) {
        this.pastOneHour = pastOneHour;
    }

    public double gettMinDirection() {
        return tMinDirection;
    }

    public void settMinDirection(double tMinDirection) {
        this.tMinDirection = tMinDirection;
    }

    public double gettMinSpeed() {
        return tMinSpeed;
    }

    public void settMinSpeed(double tMinSpeed) {
        this.tMinSpeed = tMinSpeed;
    }

    public double getHorizontalVisibility() {
        return horizontalVisibility;
    }

    public void setHorizontalVisibility(double horizontalVisibility) {
        this.horizontalVisibility = horizontalVisibility;
    }
}
