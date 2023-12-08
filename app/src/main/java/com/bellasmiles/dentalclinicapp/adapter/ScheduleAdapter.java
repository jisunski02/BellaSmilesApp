package com.bellasmiles.dentalclinicapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bellasmiles.dentalclinicapp.R;
import com.bellasmiles.dentalclinicapp.interfaces.OnItemClickListener;
import com.bellasmiles.dentalclinicapp.model.ScheduleModel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    Context context;
    private List<ScheduleModel> timeScheduleModelList;
    private OnItemClickListener onItemClickListener;
    int selected_position = -1;

    public ScheduleAdapter(Context context, List<ScheduleModel> timeScheduleModelList){
        this.context = context;
        this.timeScheduleModelList = timeScheduleModelList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ScheduleModel scheduleModel = timeScheduleModelList.get(position);

        holder.linearTimeSchedule.setBackgroundResource(selected_position == position ? R.drawable.selected_schedule_bg: R.drawable.unselected_schedule_bg);
        holder.tvTimeSchedule.setTextColor(selected_position == position ? Color.WHITE : Color.BLACK);

        String startTime = LocalTime.parse(scheduleModel.getStartTime()).format(DateTimeFormatter.ofPattern("h:mma"));
        String endTime = LocalTime.parse(scheduleModel.getEndTime()).format(DateTimeFormatter.ofPattern("h:mma"));
        holder.tvTimeSchedule.setText(startTime+" - "+endTime);

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_timeschedule,parent,false);
        ViewHolder viewHolder = new ViewHolder(v, onItemClickListener);
        return viewHolder;

    }

    @Override
    public int getItemCount() {
        return timeScheduleModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearTimeSchedule;
        TextView tvTimeSchedule;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            linearTimeSchedule = itemView.findViewById(R.id.linearTimeSchedule);
            tvTimeSchedule = itemView.findViewById(R.id.tvTimeSchedule);

            itemView.setOnClickListener(view -> {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                            notifyItemChanged(selected_position);
                            selected_position = getAdapterPosition();
                            notifyItemChanged(selected_position);
                        }
                    }
            });
        }
    }

}
