package vn.edu.usth.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApi {
    private final String apiUrl = "https://api.weatherapi.com/v1/";
    private String apiKey;

    public String getApiUrl() {
        return apiUrl;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @param location city name
     * @param aqi toggle air quality
     * @return json string
     */
    public String currentReq(String location, Boolean aqi){
        try {
            String link = apiUrl + "current.json?key=" + apiKey + "&q=" + location;
            if(aqi) link += "&aqi=yes";

            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();

            if(responseCode != 200) {
                System.out.println("Connection failed");
                return null;
            }

            System.out.println("Connection successful");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param location city name
     * @param days number of days to forecast
     * @param aqi toggle air quality
     * @return json string
     */
    public String forecastReq(String location, int days, Boolean aqi) {
        try {
            String link = apiUrl + "forecast.json?key=" + apiKey + "&q=" + location + "&days=" + days;
            if(aqi) link += "&aqi=yes";

            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();

            if(responseCode != 200) {
                System.out.println("Connection failed");
                return null;
            }

            System.out.println("Connection successful");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param location city name
     * @param date date in format yyyy-mm-dd
     * @return json string
     */
    public String historyReq(String location, String date) {
        try {
            String link = apiUrl + "history.json?key=" + apiKey + "&q=" + location + "&dt=" + date;

            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();

            if(responseCode != 200) {
                System.out.println("Connection failed");
                return null;
            }

            System.out.println("Connection successful");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
