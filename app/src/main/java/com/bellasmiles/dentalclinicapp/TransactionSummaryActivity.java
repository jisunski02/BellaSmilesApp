package com.bellasmiles.dentalclinicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bellasmiles.dentalclinicapp.api.ApiClientRx;
import com.bellasmiles.dentalclinicapp.api.ApiService;
import com.bellasmiles.dentalclinicapp.constant.Constants;
import com.bellasmiles.dentalclinicapp.databinding.ActivityTransactionSummaryBinding;
import com.bellasmiles.dentalclinicapp.model.LoginRegisterModel;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.cancel.OnCancel;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.error.ErrorInfo;
import com.paypal.checkout.error.OnError;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

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

        binding.toolbar.ivProfile.setVisibility(View.GONE);
        binding.toolbar.ivHistory.setVisibility(View.GONE);
        binding.toolbar.btnBooknow.setVisibility(View.GONE);
        binding.toolbar.ivTooth.setVisibility(View.GONE);

        binding.toolbar.ivBack.setOnClickListener(v-> finish());

        Intent intent = getIntent();
        String selectedDoctor = intent.getStringExtra("selectedDoctor");
        String selectedService = intent.getStringExtra("selectedService");
        String selectedDate = intent.getStringExtra("selectedDate");
        String selectedStartTime = intent.getStringExtra("selectedStartTime");
        String selectedEndTime = intent.getStringExtra("selectedEndTime");
        String serviceCost = intent.getStringExtra("serviceCost");
        String doctorId = intent.getStringExtra("doctorId");
        String scheduleId = intent.getStringExtra("scheduleId");
        String clientId = intent.getStringExtra("clientId");
        String serviceId = intent.getStringExtra("serviceId");

        String startTime = LocalTime.parse(selectedStartTime).format(DateTimeFormatter.ofPattern("h:mma"));
        String endTime = LocalTime.parse(selectedEndTime).format(DateTimeFormatter.ofPattern("h:mma"));

        binding.tvSelectedDoctor.setText(selectedDoctor);
        binding.tvSelectedService.setText(selectedService);
        binding.tvSelectedTimeschedule.setText(selectedDate+"  "+startTime+" to "+endTime);
        binding.tvCost.setText("Php "+serviceCost+".00");

        binding.btnConfirm.setOnClickListener(view -> {
            Dialog loadingDialog = new Dialog(TransactionSummaryActivity.this);
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setContentView(R.layout.dialog_loading);


            loadingDialog.setCancelable(false);
            loadingDialog.show();

            bookAppointment(loadingDialog, clientId, doctorId, serviceId, scheduleId, "No", "OTC");
        });


        binding.paymentButtonContainer.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        Toast.makeText(TransactionSummaryActivity.this, "Setting up Paypal", Toast.LENGTH_SHORT).show();
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.PHP)
                                                        .value(serviceCost)
                                                        .build()
                                        )
                                        .build()
                        );
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(result -> {

                            Toast.makeText(TransactionSummaryActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();

                                Dialog loadingDialog = new Dialog(TransactionSummaryActivity.this);
                                loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
                                loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                loadingDialog.setContentView(R.layout.dialog_loading);


                                loadingDialog.setCancelable(false);
                                loadingDialog.show();

                                bookAppointment(loadingDialog, clientId, doctorId, serviceId, scheduleId, "Yes","PayPal");
                        });

                        new OnCancel() {
                            @Override
                            public void onCancel() {
                                Toast.makeText(TransactionSummaryActivity.this, "Payment cancelled", Toast.LENGTH_SHORT).show();

                            }

                        };

                        new OnError() {
                            @Override
                            public void onError(@NotNull ErrorInfo errorInfo) {
                                Toast.makeText(TransactionSummaryActivity.this, errorInfo.toString(), Toast.LENGTH_SHORT).show();

                            }
                        };

                    }



                }
        );

    }

    private void bookAppointment(Dialog dialog, String clientId, String doctorId, String serviceId, String scheduleId, String payment_status, String payment_type) {

        disposable.add(
                apiService.bookAppointment(clientId, doctorId, serviceId, scheduleId, payment_status, payment_type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginRegisterModel>() {
                            @Override
                            public void onSuccess(LoginRegisterModel response) {
                                String success = response.getSuccess();
                                dialog.dismiss();
                                if(success.equals("1")){
                                    Toast.makeText(TransactionSummaryActivity.this, "Booking appointment successful", Toast.LENGTH_SHORT).show();
                                    Constants.gotoActivity(TransactionSummaryActivity.this, HomeActivity.class);
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