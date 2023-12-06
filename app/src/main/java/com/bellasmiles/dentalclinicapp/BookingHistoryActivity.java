package com.bellasmiles.dentalclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bellasmiles.dentalclinicapp.adapter.BookingHistoryAdapter;
import com.bellasmiles.dentalclinicapp.adapter.ScheduleAdapter;
import com.bellasmiles.dentalclinicapp.api.ApiClientRx;
import com.bellasmiles.dentalclinicapp.api.ApiService;
import com.bellasmiles.dentalclinicapp.constant.Constants;
import com.bellasmiles.dentalclinicapp.databinding.ActivityBookingHistoryBinding;
import com.bellasmiles.dentalclinicapp.model.AppointmentModel;
import com.bellasmiles.dentalclinicapp.model.ScheduleModel;
import com.bellasmiles.dentalclinicapp.sharedpreferences.LoginSharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class BookingHistoryActivity extends AppCompatActivity {

    ActivityBookingHistoryBinding binding;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    List<AppointmentModel> appointmentModelList = new ArrayList<>();

    LoginSharedPrefManager sharedPrefManager;

    BookingHistoryAdapter adapter;

    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new LoginSharedPrefManager(this);

        apiService = ApiClientRx.getClient(Constants.BASE_URL).create(ApiService.class);

        binding.rvBookinghistory.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvBookinghistory.setLayoutManager(linearLayoutManager);

        binding.toolbar.activityTitle.setText("Appointment History");
        binding.toolbar.ivProfile.setVisibility(View.GONE);
        binding.toolbar.ivHistory.setVisibility(View.GONE);

        appointmentHistory(sharedPrefManager.getClientId(), "0", "No Pending Appointment");

        binding.btnPending.setEnabled(false);

        binding.btnPending.setOnClickListener(v->{
            binding.btnPending.setEnabled(false);
            binding.btnApproved.setEnabled(true);
            binding.btnCompleted.setEnabled(true);
            binding.btnCancelled.setEnabled(true);
            binding.btnPending.setBackgroundResource(R.drawable.clickedchangecolorbutton);
            binding.btnApproved.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnCompleted.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnCancelled.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnPending.setTextColor(Color.WHITE);
            binding.btnApproved.setTextColor(Color.BLACK);
            binding.btnCompleted.setTextColor(Color.BLACK);
            binding.btnCancelled.setTextColor(Color.BLACK);
            appointmentHistory(sharedPrefManager.getClientId(), "0", "No Pending Appointment");
        });

        binding.btnApproved.setOnClickListener(v->{
            binding.btnPending.setEnabled(true);
            binding.btnApproved.setEnabled(false);
            binding.btnCompleted.setEnabled(true);
            binding.btnCancelled.setEnabled(true);
            binding.btnPending.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnApproved.setBackgroundResource(R.drawable.clickedchangecolorbutton);
            binding.btnCompleted.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnCancelled.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnPending.setTextColor(Color.BLACK);
            binding.btnApproved.setTextColor(Color.WHITE);
            binding.btnCompleted.setTextColor(Color.BLACK);
            binding.btnCancelled.setTextColor(Color.BLACK);
            appointmentHistory(sharedPrefManager.getClientId(), "1", "No Approved Appointment");
        });

        binding.btnCompleted.setOnClickListener(v->{
            binding.btnPending.setEnabled(true);
            binding.btnApproved.setEnabled(true);
            binding.btnCompleted.setEnabled(false);
            binding.btnCancelled.setEnabled(true);
            binding.btnPending.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnApproved.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnCompleted.setBackgroundResource(R.drawable.clickedchangecolorbutton);
            binding.btnCancelled.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnPending.setTextColor(Color.BLACK);
            binding.btnApproved.setTextColor(Color.BLACK);
            binding.btnCompleted.setTextColor(Color.WHITE);
            binding.btnCancelled.setTextColor(Color.BLACK);
            appointmentHistory(sharedPrefManager.getClientId(), "2", "No Completed Appointment");
        });


        binding.btnCancelled.setOnClickListener(v->{
            binding.btnPending.setEnabled(true);
            binding.btnApproved.setEnabled(true);
            binding.btnCompleted.setEnabled(true);
            binding.btnCancelled.setEnabled(false);
            binding.btnPending.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnApproved.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnCompleted.setBackgroundResource(R.drawable.btn_cancel_bg);
            binding.btnCancelled.setBackgroundResource(R.drawable.clickedchangecolorbutton);
            binding.btnPending.setTextColor(Color.BLACK);
            binding.btnApproved.setTextColor(Color.BLACK);
            binding.btnCompleted.setTextColor(Color.BLACK);
            binding.btnCancelled.setTextColor(Color.WHITE);
            appointmentHistory(sharedPrefManager.getClientId(), "3", "No Canceled Appointment");
        });
    }


    private void appointmentHistory(String clientId, String status, String noAppointmentTitle){
        appointmentModelList.clear();
        showDialog();
        disposable.add(
                apiService.appointmentHistory(clientId, status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<AppointmentModel>>() {
                            @Override
                            public void onSuccess(List<AppointmentModel> response) {
                                loadingDialog.dismiss();

                                if(response.size() == 0){
                                   binding.linearNoappointment.setVisibility(View.VISIBLE);
                                   binding.tvNoappointment.setText(noAppointmentTitle);
                                }

                                else{
                                    binding.linearNoappointment.setVisibility(View.GONE);

                                    for(int a = 0; a<response.size(); a++){

                                        AppointmentModel appointmentModel = response.get(a);

                                        String transactionNo = appointmentModel.getTransactionNo();
                                        String docFirstName = appointmentModel.getDocFirstName();
                                        String docMiddleName = appointmentModel.getDocMiddleName();
                                        String docLastName = appointmentModel.getDocLastName();
                                        String servDesc = appointmentModel.getServDesc();
                                        String servCost = appointmentModel.getServCost();
                                        String schedDate = appointmentModel.getSchedDate();
                                        String schedStartTime = appointmentModel.getSchedStartTime();
                                        String schedEndtime = appointmentModel.getschedEndTime();
                                        String schedDuration = appointmentModel.getSchedDuration();

                                        AppointmentModel appointmentModel1 = new AppointmentModel(
                                                transactionNo, docFirstName, docMiddleName, docLastName,
                                                servDesc, servCost, schedDate, schedStartTime, schedEndtime,
                                                schedDuration
                                        );

                                        appointmentModelList.add(appointmentModel1);
                                    }

                                    adapter = new BookingHistoryAdapter(BookingHistoryActivity.this, appointmentModelList);
                                    binding.rvBookinghistory.setAdapter(adapter);
                                }


                            }
                            @Override
                            public void onError(Throwable e) {
                                loadingDialog.dismiss();
                                if(Constants.isNetworkConnected(BookingHistoryActivity.this)){
                                    Toast.makeText(BookingHistoryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(BookingHistoryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
    }

    private void showDialog(){
        loadingDialog = new Dialog(BookingHistoryActivity.this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setContentView(R.layout.dialog_loading);


        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }
}