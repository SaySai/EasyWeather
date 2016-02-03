package saysai.app.easyweather.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import saysai.app.easyweather.R;
import saysai.app.easyweather.Utils.HttpCallbackListener;
import saysai.app.easyweather.Utils.HttpUtil;
import saysai.app.easyweather.Utils.Utility;

/**
 * Created by isay on 2/3/2016.
 */
public class WeatherActivity extends Activity  {
    private TextView cityNameText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private TextView publishText;
    private TextView windDirection;
    private TextView windLevel;
    private TextView fsDetails;
    private TextView source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.tempMin);
        temp2Text = (TextView) findViewById(R.id.tempMax);
        currentDateText = (TextView) findViewById(R.id.current_date);
        publishText = (TextView) findViewById(R.id.publish_text);
        windDirection = (TextView) findViewById(R.id.windDirection);
        windLevel = (TextView) findViewById(R.id.windLevel);
        fsDetails = (TextView) findViewById(R.id.fs_details);
        source = (TextView) findViewById(R.id.source);
        String weatherID = getIntent().getStringExtra("weatherID");
        String address = "http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=" + weatherID;
        QueryWeatherfromServer(address);

    }

    private void QueryWeatherfromServer(final String address){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response){
                Utility.handleWeatherResponse(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }

        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
     */
    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText( prefs.getString("city_name", ""));
        temp1Text.setText(prefs.getString("tempMin", ""));
        temp2Text.setText(prefs.getString("tempMax", ""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText( prefs.getString("publish_time", "") + "发布");
        currentDateText.setText(prefs.getString("current_date", ""));
        windLevel.setText(prefs.getString("wind_level",""));
        windDirection.setText(prefs.getString("wind_direction",""));
        fsDetails.setText(prefs.getString("fs_details", ""));
        source.setText(prefs.getString("source",""));
        cityNameText.setVisibility(View.VISIBLE);
        //Intent intent = new Intent(this, AutoUpdateService.class);
        //startService(intent);
    }

}
