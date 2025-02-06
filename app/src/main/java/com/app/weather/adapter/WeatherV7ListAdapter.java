package com.app.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weather.R;
import com.app.weather.entity.WeatherV7Info;
import com.app.weather.utils.IconUtils;

import java.util.ArrayList;
import java.util.List;

public class WeatherV7ListAdapter extends RecyclerView.Adapter<WeatherV7ListAdapter.MyHolder> {
    private List<WeatherV7Info.DailyDTO> weatherV7InfoList = new ArrayList<>();


    public void setWeatherV7InfoList(List<WeatherV7Info.DailyDTO> weatherV7InfoList) {
        this.weatherV7InfoList = weatherV7InfoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_v7_list_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        WeatherV7Info.DailyDTO weatherV7Info = weatherV7InfoList.get(position);
        String[] split = weatherV7Info.getFxDate().split("-");
        holder.fxDate.setText(split[1] + "/" + split[2]);
        holder.textDay.setText(weatherV7Info.getTextDay());
        holder.tempMax.setText(weatherV7Info.getTempMax()+"°C");
        holder.tempMin.setText(weatherV7Info.getTempMin()+"°C");

        holder.iconDay.setImageResource(IconUtils.getDayIconDark(weatherV7Info.getIconDay()));

    }

    @Override
    public int getItemCount() {
        return weatherV7InfoList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        TextView fxDate;
        TextView textDay;
        TextView tempMax;
        TextView tempMin;
        ImageView iconDay;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            fxDate = itemView.findViewById(R.id.fxDate);
            textDay = itemView.findViewById(R.id.textDay);
            tempMax = itemView.findViewById(R.id.tempMax);
            tempMin = itemView.findViewById(R.id.tempMin);
            iconDay = itemView.findViewById(R.id.iconDay);
        }
    }
}
