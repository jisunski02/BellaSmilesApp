package com.bellasmiles.dentalclinicapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bellasmiles.dentalclinicapp.databinding.LayoutDoctorsBinding;
import com.bellasmiles.dentalclinicapp.databinding.LayoutServicesBinding;
import com.bellasmiles.dentalclinicapp.model.DoctorModel;
import com.bellasmiles.dentalclinicapp.model.ServiceModel;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    LayoutServicesBinding binding;
    Context context;
    private List<ServiceModel> serviceModelList;

    public ServicesAdapter(Context context, List<ServiceModel> serviceModelList){
        this.context = context;
        this.serviceModelList = serviceModelList;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServiceModel serviceModel = serviceModelList.get(position);

        holder.binding.tvServicedesc.setText(serviceModel.getServiceDesc());
        holder.binding.tvServicecost.setText("Php "+serviceModel.getServiceCost()+".00");

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = LayoutServicesBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LayoutServicesBinding binding;
        public ViewHolder(@NonNull LayoutServicesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
