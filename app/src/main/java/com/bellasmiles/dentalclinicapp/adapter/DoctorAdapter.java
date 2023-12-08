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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bellasmiles.dentalclinicapp.HomeActivity;
import com.bellasmiles.dentalclinicapp.R;
import com.bellasmiles.dentalclinicapp.databinding.DialogProfileBinding;
import com.bellasmiles.dentalclinicapp.databinding.LayoutDoctorsBinding;
import com.bellasmiles.dentalclinicapp.model.DoctorModel;
import com.bellasmiles.dentalclinicapp.model.ScheduleModel;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    LayoutDoctorsBinding binding;
    Context context;
    private List<DoctorModel> doctorModelList;

    public DoctorAdapter(Context context, List<DoctorModel> doctorModelList){
        this.context = context;
        this.doctorModelList = doctorModelList;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DoctorModel doctorModel = doctorModelList.get(position);

        holder.binding.tvDoctoraddress.setText(doctorModel.getAddress());
        holder.binding.tvDoctorcontact.setText(doctorModel.getContactNo());
        holder.binding.tvDoctorname.setText(doctorModel.getFirstName()+" "+doctorModel.getMiddleName()+" "+doctorModel.getLastName());

        if(doctorModel.getContactNo().isEmpty()){
            holder.binding.tvDoctorcontact.setText("N/A");
        }

        if(doctorModel.getAddress().isEmpty()){
            holder.binding.tvDoctoraddress.setText("N/A");
        }

        holder.binding.btnViewdetails.setOnClickListener(v->{
            Dialog profileDialog = new Dialog(context);
            profileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
            profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DialogProfileBinding binding1 = DialogProfileBinding.inflate(profileDialog.getLayoutInflater());
            profileDialog.setContentView(binding1.getRoot());

            binding1.btnLogout.setVisibility(View.GONE);
            binding1.tvFullname.setText(doctorModel.getFirstName()+" "+doctorModel.getMiddleName()+" "+doctorModel.getLastName());
            binding1.tvBirthdateLabel.setVisibility(View.GONE);
            binding1.tvBirthdate.setVisibility(View.GONE);
            binding1.tvGender.setText(doctorModel.getGender());
            binding1.tvContactnumberr.setText(doctorModel.getContactNo());
            binding1.tvEmailaddress.setText(doctorModel.getEmailAddress());
            binding1.tvAddress.setText(doctorModel.getAddress());
            binding1.tvPofileTitle.setText("Doctor Details");

            if(doctorModel.getGender().isEmpty()){
                binding1.tvGender.setText("N/A");
            }

            if(doctorModel.getContactNo().isEmpty()){
                binding1.tvContactnumberr.setText("N/A");
            }

            if(doctorModel.getEmailAddress().isEmpty()){
                binding1.tvEmailaddress.setText("N/A");
            }

            if(doctorModel.getAddress().isEmpty()){
                binding1.tvAddress.setText("N/A");
            }

            profileDialog.setCancelable(true);
            profileDialog.show();
        });
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = LayoutDoctorsBinding.inflate(LayoutInflater.from(context),parent,false);
        ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;

    }

    @Override
    public int getItemCount() {
        return doctorModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      LayoutDoctorsBinding binding;
        public ViewHolder(@NonNull LayoutDoctorsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
