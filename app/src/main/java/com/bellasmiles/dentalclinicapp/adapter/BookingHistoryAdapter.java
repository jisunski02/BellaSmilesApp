package com.bellasmiles.dentalclinicapp.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bellasmiles.dentalclinicapp.BookingHistoryActivity;
import com.bellasmiles.dentalclinicapp.R;
import com.bellasmiles.dentalclinicapp.databinding.DialogAppointmentdetailsBinding;
import com.bellasmiles.dentalclinicapp.interfaces.OnItemClickListener;
import com.bellasmiles.dentalclinicapp.model.AppointmentModel;
import com.bellasmiles.dentalclinicapp.model.ScheduleModel;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    Context context;
    private List<AppointmentModel> appointmentModelList;

    public BookingHistoryAdapter(Context context, List<AppointmentModel> appointmentModelList){
        this.context = context;
        this.appointmentModelList = appointmentModelList;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppointmentModel appointmentModel = appointmentModelList.get(position);

        holder.tv_datetimeschedule.setText(appointmentModel.getSchedDate()+" - "+appointmentModel.getSchedStartTime()+" to "+appointmentModel.getschedEndTime());
        holder.tv_service.setText(appointmentModel.getServDesc());

        holder.iv_seemore.setOnClickListener(view -> {
            Dialog dialogAppointmentDetails = new Dialog(context);
            dialogAppointmentDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
            dialogAppointmentDetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DialogAppointmentdetailsBinding binding = DialogAppointmentdetailsBinding.inflate(dialogAppointmentDetails.getLayoutInflater());
            dialogAppointmentDetails.setContentView(binding.getRoot());

            binding.tvSelectedDoctor.setText(appointmentModel.getDocFirstName()+" "+appointmentModel.getDocMiddleName()+" "+appointmentModel.getDocLastName());
            binding.tvSelectedService.setText(appointmentModel.getServDesc());
            binding.tvSelectedTimeschedule.setText(appointmentModel.getSchedDate()+"  "+appointmentModel.getSchedStartTime()+ " - "+appointmentModel.getschedEndTime());
            binding.tvCost.setText("Php "+appointmentModel.getServCost()+".00");

            binding.btnClose.setOnClickListener(v->{
                dialogAppointmentDetails.dismiss();
            });

            dialogAppointmentDetails.setCancelable(false);
            dialogAppointmentDetails.show();
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bookinghistory,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public int getItemCount() {
        return appointmentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_seemore;
        TextView tv_service, tv_datetimeschedule;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            iv_seemore = itemView.findViewById(R.id.iv_seemore);
            tv_service = itemView.findViewById(R.id.tv_service);
            tv_datetimeschedule = itemView.findViewById(R.id.tv_datetimeschedule);

        }
    }

}
