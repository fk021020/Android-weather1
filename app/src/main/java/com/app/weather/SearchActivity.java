package com.app.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.weather.adapter.SearchListAdapter;
import com.app.weather.api.ApiConstants;
import com.app.weather.entity.CityLocationInfo;
import com.app.weather.utils.ProgressDialogUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

public class SearchActivity extends AppCompatActivity {
    private EditText et_search_city;
    private RecyclerView recyclerView;
    private LinearLayoutCompat ll_empty;

    private SearchListAdapter mSearchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 1. 初始化控件
        initViews();
        //创建适配器
        mSearchListAdapter = new SearchListAdapter();
        //设置适配器
        recyclerView.setAdapter(mSearchListAdapter);
        // 2. 点击事件
        setListener();


    }

    /**
     * 初始化控件
     */
    private void initViews() {
        et_search_city = findViewById(R.id.et_search_city);
        recyclerView = findViewById(R.id.recyclerView);
        ll_empty = findViewById(R.id.ll_empty);
    }

    /**
     * 点击事件
     */
    private void setListener() {
        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 获取输入框的值
                String cityName = et_search_city.getText().toString().trim();
                // 2. 判断是否为空
                if (cityName.isEmpty()) {
                    // 提示用户
                    Toast.makeText(SearchActivity.this, "城市名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    searchCity(cityName);
                }
            }
        });


        //返回
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //recyclerView点击事件
        mSearchListAdapter.setOnItemClickListener(new SearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CityLocationInfo.LocationDTO locationDTO) {
                // 1. 获取城市名
                String cityName = locationDTO.getName();
                Intent intent = new Intent();
                intent.putExtra("cityName", cityName);
                intent.putExtra("id", locationDTO.getId());
                //设置跳转回传的值
                setResult(1000, intent);
                // 3. 关闭当前界面
                finish();
            }
        });
    }


    /**
     * 城市搜索
     */
    private void searchCity(String cityName) {

        OkGo.<String>get("https://geoapi.qweather.com/v2/city/lookup").params("location", cityName).params("key", ApiConstants.APP_KEY).execute(new StringCallback() {
            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                ProgressDialogUtils.showProgressDialog(SearchActivity.this);
            }

            @Override
            public void onSuccess(Response<String> response) {
                CityLocationInfo cityLocationInfo = new Gson().fromJson(response.body(), CityLocationInfo.class);
                if (null != cityLocationInfo && cityLocationInfo.getCode().equals("200")) {
                    if (null != mSearchListAdapter) {
                        mSearchListAdapter.setCityLocationInfoList(cityLocationInfo.getLocation());
                    }
                    //判断是否显示空布局
                    if (mSearchListAdapter.getItemCount() > 0) {
                        ll_empty.setVisibility(View.GONE);
                    } else {
                        ll_empty.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "未查询到该城市", Toast.LENGTH_SHORT).show();
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
}