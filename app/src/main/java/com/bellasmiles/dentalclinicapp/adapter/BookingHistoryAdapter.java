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
import com.bellasmiles.dentalclinicapp.databinding.LayoutBookinghistoryBinding;
import com.bellasmiles.dentalclinicapp.databinding.LayoutDoctorsBinding;
import com.bellasmiles.dentalclinicapp.interfaces.OnItemClickListener;
import com.bellasmiles.dentalclinicapp.model.AppointmentModel;
import com.bellasmiles.dentalclinicapp.model.ScheduleModel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    LayoutBookinghistoryBinding binding;
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

        String startTime = LocalTime.parse(appointmentModel.getSchedStartTime()).format(DateTimeFormatter.ofPattern("h:mma"));
        String endTime = LocalTime.parse(appointmentModel.getschedEndTime()).format(DateTimeFormatter.ofPattern("h:mma"));

        holder.binding.tvDatetimeschedule.setText(appointmentModel.getSchedDate());
        holder.binding.tvService.setText(appointmentModel.getServDesc());
        holder.binding.tvCost.setText("Php "+appointmentModel.getServCost()+".00");
        holder.binding.tvTimeSchedule.setText(startTime+" to "+endTime);

        holder.binding.ivSeemore.setOnClickListener(view -> {
            Dialog dialogAppointmentDetails = new Dialog(context);
            dialogAppointmentDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
            dialogAppointmentDetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DialogAppointmentdetailsBinding binding = DialogAppointmentdetailsBinding.inflate(dialogAppointmentDetails.getLayoutInflater());
            dialogAppointmentDetails.setContentView(binding.getRoot());

            binding.tvSelectedDoctor.setText(appointmentModel.getDocFirstName()+" "+appointmentModel.getDocMiddleName()+" "+appointmentModel.getDocLastName());
            binding.tvSelectedService.setText(appointmentModel.getServDesc());
            binding.tvSelectedTimeschedule.setText(appointmentModel.getSchedDate()+"   "+startTime+ " - "+endTime);
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
        binding = LayoutBookinghistoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);

    }

    @Override
    public int getItemCount() {
        return appointmentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutBookinghistoryBinding binding;

        public ViewHolder(@NonNull LayoutBookinghistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }

}
