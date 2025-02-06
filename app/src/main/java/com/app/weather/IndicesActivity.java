package com.app.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.app.weather.adapter.IndicesListAdapter;
import com.app.weather.api.ApiConstants;
import com.app.weather.api.IndicesInfo;
import com.app.weather.entity.WeatherV7Info;
import com.app.weather.utils.ProgressDialogUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

public class IndicesActivity extends AppCompatActivity {
    private String city_id;

    private RecyclerView recyclerView;
    private IndicesListAdapter mIndicesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indices);
        //获取跳转传值
        city_id = getIntent().getStringExtra("city_id");
        //获取生活指数
        getWeatherIndices(city_id);
        //初始化控件
        initViews();
        //初始化适配器
        mIndicesListAdapter = new IndicesListAdapter();
        //设置适配器
        recyclerView.setAdapter(mIndicesListAdapter);
        //设置监听器
        setListener();
    }


    /**
     * 初始化控件
     */
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);

    }

    /**
     * 设置监听器
     */
    private void setListener() {

        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    /**
     * 获取生活指数
     */
    private void getWeatherIndices(String city_id) {
        OkGo.<String>get("https://devapi.qweather.com/v7/indices/1d")
                .params("location", city_id)
                .params("key", ApiConstants.APP_KEY)
                .params("type", "0")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showProgressDialog(IndicesActivity.this);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        IndicesInfo indicesInfo = new Gson().fromJson(response.body(), IndicesInfo.class);
                        if (null != indicesInfo && indicesInfo.getCode().equals("200")) {
                            mIndicesListAdapter.setIndicesInfoList(indicesInfo.getDaily());
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtils.hideProgressDialog();
                    }
                });
    }
}