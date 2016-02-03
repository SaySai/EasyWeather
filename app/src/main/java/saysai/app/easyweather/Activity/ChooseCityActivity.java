package saysai.app.easyweather.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import saysai.app.easyweather.Database.CityQuery;
import saysai.app.easyweather.R;


/**
 * Created by isay on 2/2/2016.
 */
public class ChooseCityActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> datalist = new ArrayList<String>();

    private int currentLevel;
    private String currentProvince;
    private String currentCity;
    private String currentCounty;
    private String  weatherID;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_city);
        listView = (ListView) findViewById(R.id.data_list);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(ChooseCityActivity.this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
                if (currentLevel == LEVEL_PROVINCE) {
                    currentProvince = datalist.get(index);
                    if (CityQuery.getCityListByProvince(currentProvince).size() > 0) {
                        datalist.clear();
                        for (String city : CityQuery.getCityListByProvince(currentProvince)) {
                            datalist.add(city);
                        }
                    }
                    titleText.setText(currentProvince);
                    currentLevel = LEVEL_CITY;
                } else if (currentLevel == LEVEL_CITY) {
                    currentCity = datalist.get(index);
                    if (CityQuery.getAreaListByCity(currentCity).size() > 0) {
                        datalist.clear();
                        for (String area : CityQuery.getAreaListByCity(currentCity)) {
                            datalist.add(area);
                        }
                    }
                    titleText.setText(currentCity);
                    currentLevel = LEVEL_COUNTY;
                } else if (currentLevel == LEVEL_COUNTY) {
                    currentCounty = datalist.get(index);
                    weatherID = CityQuery.getWeatherIdByAreaName(currentCounty);
                    Intent intent = new Intent(ChooseCityActivity.this,WeatherActivity.class);
                    intent.putExtra("weatherID",weatherID);
                    startActivity(intent);
                    finish();
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
            }
        });
        if (CityQuery.getProvinceList().size() > 0) {
            datalist.clear();
            for (String province : CityQuery.getProvinceList()) {
                datalist.add(province);
            }
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        titleText.setText("中国");
        currentLevel = LEVEL_PROVINCE;

    }





    //读取location.db来初始化数据库
    private void initDB() {
        final File file = new File(getFilesDir(), "location.db");
        if (file.exists() && file.length() > 0) {
            datalist = CityQuery.getProvinceList();
        } else {
            // 开启一个线程去检查省市数据库是否存在
            new Thread(new Runnable() {
                @Override
                public void run() {
                    copyLocationDB(file);
                }
            }).start();
            if(CityQuery.getProvinceList().size()>0){
            }
            datalist = CityQuery.getProvinceList();
        }
    }

    /**若本地没有全国省市县的数据库，
     * 拷贝省市数据库
     */
    private void copyLocationDB(File file) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = getAssets().open("location.db");
            fos = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    
    /**
     * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            if (CityQuery.getCityListByProvince(currentProvince).size() > 0) {
                datalist.clear();
                for (String area : CityQuery.getCityListByProvince(currentProvince)) {
                    datalist.add(area);
                }
            }
            titleText.setText(currentProvince);
            currentLevel = LEVEL_CITY;
        } else if (currentLevel == LEVEL_CITY) {
            if (CityQuery.getProvinceList().size() > 0) {
                datalist.clear();
                for (String province : CityQuery.getProvinceList()) {
                    datalist.add(province);
                }
            }
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            finish();
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);

    }







}
