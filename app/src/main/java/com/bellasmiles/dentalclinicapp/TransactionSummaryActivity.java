package com.bellasmiles.dentalclinicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.bellasmiles.dentalclinicapp.api.ApiClientRx;
import com.bellasmiles.dentalclinicapp.api.ApiService;
import com.bellasmiles.dentalclinicapp.constant.Constants;
import com.bellasmiles.dentalclinicapp.databinding.ActivityTransactionSummaryBinding;
import com.bellasmiles.dentalclinicapp.model.LoginRegisterModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TransactionSummaryActivity extends AppCompatActivity {

    ActivityTransactionSummaryBinding binding;

    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = ApiClientRx.getClient(Constants.BASE_URL).create(ApiService.class);

        Intent intent = getIntent();
        String selectedDoctor = intent.getStringExtra("selectedDoctor");
        String selectedService = intent.getStringExtra("selectedService");
        String selectedTimeSchedule = intent.getStringExtra("selectedTimeSchedule");
        String serviceCost = intent.getStringExtra("serviceCost");
        String doctorId = intent.getStringExtra("doctorId");
        String scheduleId = intent.getStringExtra("scheduleId");
        String clientId = intent.getStringExtra("clientId");
        String serviceId = intent.getStringExtra("serviceId");

        binding.tvSelectedDoctor.setText(selectedDoctor);
        binding.tvSelectedService.setText(selectedService);
        binding.tvSelectedTimeschedule.setText(selectedTimeSchedule);
        binding.tvCost.setText(serviceCost+".00");

        binding.btnConfirm.setOnClickListener(view -> {
            Dialog loadingDialog = new Dialog(TransactionSummaryActivity.this);
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setContentView(R.layout.dialog_loading);


            loadingDialog.setCancelable(false);
            loadingDialog.show();

            bookAppointment(loadingDialog, clientId, doctorId, serviceId, scheduleId);
        });

    }

    private void bookAppointment(Dialog dialog, String clientId, String doctorId, String serviceId, String scheduleId) {

        disposable.add(
                apiService.bookAppointment(clientId, doctorId, serviceId, scheduleId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginRegisterModel>() {
                            @Override
                            public void onSuccess(LoginRegisterModel response) {
                                String success = response.getSuccess();
                                dialog.dismiss();
                                if(success.equals("1")){
                                    Toast.makeText(TransactionSummaryActivity.this, "Booking appointment successful", Toast.LENGTH_SHORT).show();
                                    Constants.gotoActivity(TransactionSummaryActivity.this, MainActivity.class);
                                    finish();
                                    finishAffinity();
                                }
                                else if(success.equals("0")){
                                    Toast.makeText(TransactionSummaryActivity.this, "No availability at chosen time", Toast.LENGTH_SHORT).show();
                                }

                                else{
                                    Toast.makeText(TransactionSummaryActivity.this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                                }

                            }
                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if(Constants.isNetworkConnected(TransactionSummaryActivity.this)){
                                    Toast.makeText(TransactionSummaryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(TransactionSummaryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
    }
}