package com.app.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weather.R;
import com.app.weather.api.IndicesInfo;
import com.app.weather.entity.CityLocationInfo;

import java.util.ArrayList;
import java.util.List;

public class IndicesListAdapter extends RecyclerView.Adapter<IndicesListAdapter.MyHolder> {
    private List<IndicesInfo.DailyDTO>   mIndicesInfoList = new ArrayList<>();


    public void setIndicesInfoList(List<IndicesInfo.DailyDTO>  indicesInfoList) {
        this.mIndicesInfoList = indicesInfoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.indices_list_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        IndicesInfo.DailyDTO dailyDTO = mIndicesInfoList.get(position);
        //绑定数据
        holder.tv_text.setText(dailyDTO.getText());
        holder.tv_category.setText(dailyDTO.getCategory());
        holder.tv_name.setText(dailyDTO.getName());

    }

    @Override
    public int getItemCount() {
        return mIndicesInfoList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_text;
        TextView tv_category;
        TextView tv_name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_text = itemView.findViewById(R.id.tv_text);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_name = itemView.findViewById(R.id.tv_name);
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
