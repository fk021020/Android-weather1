package com.app.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weather.R;
import com.app.weather.entity.CityLocationInfo;
import com.app.weather.entity.WeatherV7Info;
import com.app.weather.utils.IconUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyHolder> {
    private List<CityLocationInfo.LocationDTO> mCityLocationInfoList = new ArrayList<>();


    public void setCityLocationInfoList(List<CityLocationInfo.LocationDTO> cityLocationInfoList) {
        this.mCityLocationInfoList = cityLocationInfoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        CityLocationInfo.LocationDTO locationDTO = mCityLocationInfoList.get(position);
        holder.tv_city_name.setText(locationDTO.getName() + "," + locationDTO.getAdm1() + "," + locationDTO.getCountry());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(locationDTO);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCityLocationInfoList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_city_name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_city_name = itemView.findViewById(R.id.tv_city_name);
        }
    }


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;

    }

    public interface OnItemClickListener {
        void onItemClick(CityLocationInfo.LocationDTO locationDTO);

    }
}
