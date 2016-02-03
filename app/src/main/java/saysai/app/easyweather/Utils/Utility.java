package saysai.app.easyweather.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by isay on 2/3/2016.
 */
public class Utility {
    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject today = jsonObject.getJSONObject("today");
            JSONObject forecast = jsonObject.getJSONObject("forecast");
            JSONObject realtime = jsonObject.getJSONObject("realtime");
            JSONArray index = jsonObject.getJSONArray("index");
            JSONObject aqi = jsonObject.getJSONObject("aqi");
            JSONObject index0 = index.getJSONObject(0);
            //JSONObject index_fs = indexArray.getJSONObject(1);
            String cityName = forecast.getString("city");
            String weatherCode = forecast.getString("cityid");
            String tempMin = today.getString("tempMin");
            String tempMax = today.getString("tempMax");
            String weatherDesp = realtime.getString("weather");
            String publishTime = realtime.getString("time");
            String windDirection = realtime.getString("WD");
            String windLevel = realtime.getString("WS");
            String source = aqi.getString("src");
            String fs_details = index0.getString("details");
            String currentDate = forecast.getString("date_y");

            saveWeatherInfo(context, cityName, weatherCode, tempMin, tempMax,
                    weatherDesp, publishTime,windDirection,windLevel,source,fs_details,currentDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     */
    public static void saveWeatherInfo(Context context, String cityName,
                                       String weatherCode, String tempMin, String tempMax, String weatherDesp,
                                       String publishTime, String windDirection, String windLevel, String source,
                                       String fs_details, String currentDate) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("tempMin", tempMin);
        editor.putString("tempMax", tempMax);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", currentDate);
        editor.putString("wind_direction", windDirection);
        editor.putString("wind_level", windLevel);
        editor.putString("source", source);
        editor.putString("fs_details",fs_details);
        editor.commit();
    }
}
