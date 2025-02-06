package com.app.weather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.weather.adapter.WeatherV7ListAdapter;
import com.app.weather.api.ApiConstants;
import com.app.weather.entity.AirDailyInfo;
import com.app.weather.entity.CityLocationInfo;
import com.app.weather.entity.NowWeatherInfo;
import com.app.weather.entity.WeatherV7Info;
import com.app.weather.utils.ProgressDialogUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private TextView tv_text;
    private TextView tv_city_name;
    private TextView tv_temp;
    private TextView tv_humidity;
    private TextView tv_pressure;
    private TextView tv_vis;
    private TextView tv_windDir;
    private TextView tv_windSpeed;
    private TextView tv_windScale;
    private TextView tv_category;
    private TextView tv_aqi;
    private TextView tv_pm10;
    private TextView tv_pm2p5;
    private TextView tv_so2;
    private TextView tv_o3;
    private TextView tv_co;
    private TextView tv_no2;
    private ImageView btn_speak;
    private RecyclerView recyclerView;

    private String city_id;
    private TextToSpeech textToSpeech;

    private WeatherV7ListAdapter mWeatherV7ListAdapter;
    private NowWeatherInfo nowWeatherInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initViews();
        //初始化适配器
        mWeatherV7ListAdapter = new WeatherV7ListAdapter();
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //设置适配器
        recyclerView.setAdapter(mWeatherV7ListAdapter);
        //根据城市获取城市ID
        getCityId("北京");
        //设置监听
        setListener();
        //语音播报
        initTextToSpeech();
    }

    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINESE);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d("TTS", "Language not supported");
                    } else {
                        btn_speak.setEnabled(true);
                        Log.d("TTS", "Language  success");
                    }
                } else {
                    Log.d("TTS", "Initialization failed");
                }
            }
        });

    }


    /**
     * 初始化控件
     */
    private void initViews() {
        tv_text = findViewById(R.id.tv_text);
        tv_city_name = findViewById(R.id.tv_city_name);
        tv_temp = findViewById(R.id.tv_temp);
        tv_humidity = findViewById(R.id.tv_humidity);
        tv_pressure = findViewById(R.id.tv_pressure);
        tv_vis = findViewById(R.id.tv_vis);
        tv_windDir = findViewById(R.id.tv_windDir);
        tv_windSpeed = findViewById(R.id.tv_windSpeed);
        tv_windScale = findViewById(R.id.tv_windScale);
        recyclerView = findViewById(R.id.recyclerView);
        tv_category = findViewById(R.id.tv_category);
        tv_aqi = findViewById(R.id.tv_aqi);
        tv_pm10 = findViewById(R.id.tv_pm10);
        tv_pm2p5 = findViewById(R.id.tv_pm2p5);
        tv_so2 = findViewById(R.id.tv_so2);
        tv_o3 = findViewById(R.id.tv_o3);
        tv_co = findViewById(R.id.tv_co);
        tv_no2 = findViewById(R.id.tv_no2);
        btn_speak = findViewById(R.id.btn_speak);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        //搜索
        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, SearchActivity.class), 1000);
            }
        });

        //更多
        findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建PopupMenu对象
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                //将R.menu.popup_menu菜单资源加载到popup菜单中
                getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                //为popup菜单的菜单项单击事件绑定事件监听器
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.about:
                                //关于app
                                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                                break;

                            case R.id.indices:

                                //天气指数
                                Intent intent = new Intent(MainActivity.this, IndicesActivity.class);
                                intent.putExtra("city_id", city_id);
                                startActivity(intent);

                                break;

                            default:

                        }
                        // TODO Auto-generated method stub
                        return false;
                    }

                });

                popup.show();

            }
        });


        //语音播报
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != textToSpeech) {
                    NowWeatherInfo.NowDTO now = nowWeatherInfo.getNow();
                    if (null != now) {
                        String text = "您好 , 当前天气" + now.getText() + "，温度" + now.getTemp() + "度 ，"+now.getWindDir()+  now.getWindScale() + "级，祝您生活愉快";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }else {
                    Toast.makeText(MainActivity.this, "抱歉~，该模拟器暂不支持语音播报", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 根据城市获取城市ID
     */
    private void getCityId(String cityName) {

        OkGo.<String>get("https://geoapi.qweather.com/v2/city/lookup").params("location", cityName).params("key", ApiConstants.APP_KEY).execute(new StringCallback() {
            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                ProgressDialogUtils.showProgressDialog(MainActivity.this);
            }

            @Override
            public void onSuccess(Response<String> response) {
                CityLocationInfo cityLocationInfo = new Gson().fromJson(response.body(), CityLocationInfo.class);
                if (null != cityLocationInfo) {
                    CityLocationInfo.LocationDTO locationDTO = cityLocationInfo.getLocation().get(0);
                    city_id = locationDTO.getId();
                    //通过城市ID获取实时天气
                    getWeatherNow(locationDTO.getId());

                    //通过城市ID 获取未来7天预报
                    getV7Weather(locationDTO.getId());

                    //空气质量每日预报
                    getWeatherAirDaily(locationDTO.getId());

                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });


    }


    /**
     * 根据城市ID获取实时天气
     */
    private void getWeatherNow(String id) {

        OkGo.<String>get("https://devapi.qweather.com/v7/weather/now").params("location", id).params("key", ApiConstants.APP_KEY).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                nowWeatherInfo = new Gson().fromJson(response.body(), NowWeatherInfo.class);
                if (null != nowWeatherInfo) {
                    //获取实时天气
                    NowWeatherInfo.NowDTO nowDTO = nowWeatherInfo.getNow();
                    String text = nowDTO.getText();
                    String windDir = nowDTO.getWindDir();
                    tv_text.setText(text + " | " + windDir);
                    tv_temp.setText(nowDTO.getTemp());
                    tv_humidity.setText(nowDTO.getHumidity() + " %");
                    tv_vis.setText(nowDTO.getVis() + "km");
                    tv_pressure.setText(nowDTO.getPressure() + " hPa");
                    tv_windDir.setText(nowDTO.getWindDir());
                    tv_windSpeed.setText(nowDTO.getWindSpeed() + " km/h");
                    tv_windScale.setText(nowDTO.getWindScale() + " 级");
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });


    }


    /**
     * 获取未来7天预报
     */
    private void getV7Weather(String id) {

        OkGo.<String>get("https://devapi.qweather.com/v7/weather/7d").params("location", id).params("key", ApiConstants.APP_KEY).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                WeatherV7Info weatherV7Info = new Gson().fromJson(response.body(), WeatherV7Info.class);
                if (null != weatherV7Info) {
                    if (null != mWeatherV7ListAdapter) {
                        mWeatherV7ListAdapter.setWeatherV7InfoList(weatherV7Info.getDaily());
                    }
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtils.hideProgressDialog();
            }
        });

    }

    /**
     * 空气质量每日预报
     */
    private void getWeatherAirDaily(String id) {
        OkGo.<String>get("https://devapi.qweather.com/v7/air/now").params("location", id).params("key", ApiConstants.APP_KEY).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                AirDailyInfo airDailyInfo = new Gson().fromJson(response.body(), AirDailyInfo.class);

                if (null != airDailyInfo) {
                    tv_category.setText(airDailyInfo.getNow().getCategory());
                    tv_aqi.setText(airDailyInfo.getNow().getAqi());
                    tv_pm10.setText(airDailyInfo.getNow().getPm10());
                    tv_pm2p5.setText(airDailyInfo.getNow().getPm2p5());
                    tv_o3.setText(airDailyInfo.getNow().getO3());
                    tv_so2.setText(airDailyInfo.getNow().getSo2());
                    tv_co.setText(airDailyInfo.getNow().getCo());
                    tv_no2.setText(airDailyInfo.getNow().getNo2());

                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1000) {
            String cityName = data.getStringExtra("cityName");
            city_id = data.getStringExtra("id");
            tv_city_name.setText(cityName);
            //根据城市ID获取实时天气
            getWeatherNow(city_id);
            //通过城市ID 获取未来7天预报
            getV7Weather(city_id);
            //空气质量每日预报
            getWeatherAirDaily(city_id);

        }
    }
}