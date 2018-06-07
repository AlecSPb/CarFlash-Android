package com.carflash.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.DoneRideInfo;
import com.carflash.user.rentalmodule.taxirentalmodule.ResultCheck;
import com.carflash.user.samwork.Config;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.paystack.android.PaystackSdk;
import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentFailedActivity extends Activity implements ApiManager.APIFETCHER {
    private static final String TAG = "PaymentFailedActivity";
    ApiManager apiManager;
    @Bind(R.id.map_image)
    ImageView mapImage;
    @Bind(R.id.net_payble_amount)
    TextView netPaybleAmount;
    @Bind(R.id.total_distance_txt)
    TextView totalDistanceTxt;
    @Bind(R.id.total_ride_time_txt)
    TextView totalRideTimeTxt;
    @Bind(R.id.total_waiting_time_txt)
    TextView TotalWaitingTimeTxt;
    @Bind(R.id.wallet_deducted_amount_txt)
    TextView walletDeductedAmountTxt;
    @Bind(R.id.pick_address_txt)
    TextView pickAddressTxt;
    @Bind(R.id.drop_address_txt)
    TextView dropAddressTxt;
    @Bind(R.id.rating_bar)
    RatingBar ratingBar;
    @Bind(R.id.distance_charges)
    TextView distanceCharges;
    @Bind(R.id.ride_time_charges_txt)
    TextView rideTimeChargesTxt;
    @Bind(R.id.waiting_charge_txt)
    TextView waitingChargeTxt;
    @Bind(R.id.night_charge_txt)
    TextView nightChargeTxt;
    @Bind(R.id.peak_charge_txt)
    TextView peakChargeTxt;
    @Bind(R.id.coupon_code_txt)
    TextView couponCodeTxt;
    @Bind(R.id.coupon_price_txt)
    TextView couponPriceTxt;
    @Bind(R.id.coupon_layout)
    LinearLayout couponLayout;
    @Bind(R.id.tv_ride_fare)
    TextView tvRideFare;
    @Bind(R.id.cash_layout)
    LinearLayout cashLayout;
    @Bind(R.id.cash_charges_txt)
    TextView cashChargesTxt;
    @Bind(R.id.online_payment_layout)
    LinearLayout onlinePaymentLayout;
    @Bind(R.id.payment_success_layout)
    LinearLayout paymentSuccessLayout;
    @Bind(R.id.payment_failed_layout)
    CardView paymentFailedLayout;

    public static final int PAYPAL_REQUEST_CODE = 123;
    public static String DoneRideId;
    public static String RideId;
    DoneRideInfo doneRideInfo;
    @Bind(R.id.paypal_btn)
    TextView paypalBtn;
    @Bind(R.id.payment_option)
    TextView paymentOption;
    @Bind(R.id.ok)
    ImageView ok;
    @Bind(R.id.comment_edt)
    EditText commentEdt;
    @Bind(R.id.driver_image)
    CircleImageView driverImage;
    @Bind(R.id.amount_paid)
    TextView amountPaid;
    private Boolean responseCode;
    String pay_amount, authorization_url, callback_url;
    int payment_result;
    String email;
    SessionManager sessionManager;
    PayResponse tokenGenrator;
    GsonBuilder builder;
    String reference_number;
    int status;
    LinearLayout web_cross;
    RelativeLayout webLayout;
    PaystackResponseModel responseModel;
    LinearLayout rating_layout;

//    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.

    private static final int REQUEST_CODE_PAYMENT = 133;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
//    private static PayPalConfiguration config;
    @Bind(R.id.driver_name_txt)
    TextView driverNameTxt;
    WebView webView;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);
        ButterKnife.bind(this);
        PaystackSdk.initialize(this);

        sessionManager = new SessionManager(this);
        builder = new GsonBuilder();
        gson = builder.create();

        rating_layout = (LinearLayout) findViewById(R.id.rating_layout);
        rating_layout.setVisibility(View.GONE);
        webLayout = (RelativeLayout) findViewById(R.id.webLayout);
        webView = (WebView) findViewById(R.id.webview);
        email = sessionManager.getEmail();
        Log.d("**EMAIL=====", email);
        DoneRideId = super.getIntent().getExtras().getString("DONE_RIDE_ID");
        RideId = super.getIntent().getExtras().getString("RIDE_ID");
        apiManager = new ApiManager(this, this, this);
      /*  config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(this.getResources().getString(R.string.paypal_key))
                .merchantName("Example Merchant")
                .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);*/
        apiManager.execution_method_get("" + Config.ApiKeys.Key_view_Done_Ride, "" + Apis.viewDoneRide + "done_ride_id=" + DoneRideId + "&language_id=1", true, ApiManager.ACTION_SHOW_DIALOG);

        findViewById(R.id.paypal_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startPaystackPayment();
                } catch (Exception e) {
                    Toast.makeText(PaymentFailedActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.payment_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showpaymentMethod();
                } catch (Exception e) {
                }
            }
        });

        paymentFailedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showpaymentMethod();
                } catch (Exception e) {
                }
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiManager.execution_method_get("" + Config.ApiKeys.Key_Rating_Api, "" + Apis.rating + "ride_id=" + RideId + "&user_id=" + doneRideInfo.getMsg().getUser_id() + "&driver_id=" + doneRideInfo.getMsg().getDriver_id() + "&rating_star=" + ratingBar.getRating() + "&comment=" + commentEdt.getText().toString().replace(" ", "%20") + "&language_id=1", true, ApiManager.ACTION_SHOW_DIALOG);
            }
        });
    }


    public void showAlert(String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("" + message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });

        dialog.show();
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {

        try {

            if (APINAME.equals(Config.ApiKeys.Key_view_Done_Ride)) {
                doneRideInfo = new DoneRideInfo();
                doneRideInfo = gson.fromJson("" + script, DoneRideInfo.class);
                if (doneRideInfo.getResult() == 1) {
                    try {
                        //    Glide.with(PaymentFailedActivity.this).load("https://maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=200x200&maptype=roadmap&markers=color:green|label:S|28.41218546490328,77.04312231391668&markers=color:red|label:D|28.451863901432855,77.0694337785244&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4").into(mapImage);
                        String imgUrl = doneRideInfo.getMsg().getRide_image();
                        Log.e("RIDE_IMAGE_URL---", imgUrl);
                        Log.e("MODEL_IMG_LINK", doneRideInfo.getMsg().getRide_image());

                        String new_ImgUrl = imgUrl.replaceAll(":", "://");
                        Log.e("NEW_RIDE_IMAGE_URL---", imgUrl);

                        //  Picasso.with(PaymentFailedActivity.this).load("https://maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=200x200&maptype=roadmap&markers=color:green|label:S|28.41218487512079,77.04313706606628&markers=color:red|label:D|28.4592693,77.0724192&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4").error(R.drawable.app_logo_100).into(mapImage);

                        Glide.with(PaymentFailedActivity.this).load(new_ImgUrl).into(mapImage);
                    } catch (Exception e) {
                        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    netPaybleAmount.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getTotal_payable_amount());
                    totalDistanceTxt.setText("" + this.getResources().getString(R.string.total_distance) + doneRideInfo.getMsg().getDistance());
                    totalRideTimeTxt.setText(this.getResources().getString(R.string.total_ride_time) + doneRideInfo.getMsg().getRide_time());
                    TotalWaitingTimeTxt.setText("" + this.getResources().getString(R.string.total_waiting_time) + doneRideInfo.getMsg().getWaiting_time());
                    pickAddressTxt.setText("" + doneRideInfo.getMsg().getBegin_location());
                    dropAddressTxt.setText("" + doneRideInfo.getMsg().getEnd_location());
                    distanceCharges.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getAmount());
                    rideTimeChargesTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getRide_time_price());
                    waitingChargeTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getWaiting_price());
                    nightChargeTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getNight_time_charge());
                    peakChargeTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getPeak_time_charge());
                    couponCodeTxt.setText(getString(R.string.coupon_bracket) + doneRideInfo.getMsg().getCoupons_code() + ")");
                    couponPriceTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getCoupons_price());
                    tvRideFare.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getTotal_amount());
                    walletDeductedAmountTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getWallet_deducted_amount());
                    cashChargesTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getTotal_payable_amount());
                    pay_amount = doneRideInfo.getMsg().getTotal_payable_amount();
                    Log.e("PAY_AMOUNT---", pay_amount);
                    amountPaid.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getTotal_payable_amount());
                    driverNameTxt.setText(getString(R.string.rate_driver) + " " + doneRideInfo.getMsg().getDriver_name());
                    try {
                        setpaymentAccordingly(doneRideInfo.getMsg().getPayment_option_id(), doneRideInfo.getMsg().getPayment_status());
                    } catch (Exception e) {
                    }
                    try {
                        Glide.with(this).load(Apis.imageDomain + "" + doneRideInfo.getMsg().getDriver_image()).into(driverImage);
                    } catch (Exception e) {
                        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    //dialog as the ride is done "Pay WITH Card"
                    if (doneRideInfo.getMsg().getPayment_status().equals("0")) {
                        showAlert("" + doneRideInfo.getMsg().getPayment_falied_message());
                        ok.setVisibility(View.GONE);
                    } else {
                        ok.setVisibility(View.VISIBLE);
                        rating_layout.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(PaymentFailedActivity.this, "" + doneRideInfo.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }
            if (APINAME.equals("" + Config.ApiKeys.Key_Rating_Api)) {
                ResultCheck rs = gson.fromJson("" + script, ResultCheck.class);
                if (rs.result.equals("1")) {
                    finish();
                    try {
                        TrackRideAactiviySamir.trackRideActivity.finish();
                    } catch (Exception e) {
                    }
                    try {
                        RentalTrackActivity.trackRideActivity.finish();
                    } catch (Exception e) {
                    }
                    try {
                        TrialRideConfirmDialogActivity.activity.finish();
                    } catch (Exception e) {
                    }
                }
            }
            if (APINAME.equals("" + Config.ApiKeys.Key_payment_api)) {
                ResultCheck rs = gson.fromJson("" + script, ResultCheck.class);
                if (rs.result.equals("1")) {
                    apiManager.execution_method_get("" + Config.ApiKeys.Key_view_Done_Ride, "" + Apis.viewDoneRide + "done_ride_id=" + DoneRideId + "&language_id=1", true, ApiManager.ACTION_SHOW_DIALOG);
                } else {
                    Toast.makeText(this, "payment failed", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Log.d("**OBJECTSCRIPT_PAYMENT--", String.valueOf(script));

        if (APINAME.equals(Config.ApiKeys.KEY_PAY_WITH_PAYSTACK)) {
            // progressBar.setVisibility(View.GONE);
            tokenGenrator = gson.fromJson("" + script, PayResponse.class);
            status = tokenGenrator.getResult();

            Log.d("**STATUS_CARD_PAYMENT--", String.valueOf(status));

            reference_number = tokenGenrator.getDetails().getData().getReference();
            Log.d("**REFERENCE_NUMBER---", reference_number);

            authorization_url = tokenGenrator.getDetails().getData().getAuthorization_url();
            Log.d("**authorization_url---", authorization_url);

            callback_url = tokenGenrator.getDetails().getCall_back_url();
            Log.d("**callback_url---", callback_url);

            //  if (!reference_number.equals(null) || status == false){
            if (status == 1) {
                webLayout.setVisibility(View.VISIBLE);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new MyBrowser());
                webView.loadUrl(authorization_url);

                /*
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Log.d("**URL_ONPAGEFINISH", url);
                       // finish();
                    }
                });*/

            } else {

                Toast.makeText(this, "Invalid Credentials !", Toast.LENGTH_SHORT).show();
            }

        }

        //confirm payment
        if(APINAME.equals(Config.ApiKeys.CONFIRM_PAYMENT)){
            Log.d("**OBJECTSCRIPT_CONFIRM_PAYMENT saced api--", String.valueOf(script));
            responseModel = gson.fromJson("" + script, PaystackResponseModel.class);
            int result = responseModel.getResult();

            if (result == 1){
                webLayout.setVisibility(View.GONE);
                //payment saved api
                Log.d("**result- payment saved api-", String.valueOf(result));
                apiManager.execution_method_get("" + Config.ApiKeys.Key_payment_api, "" + Apis.payment + "order_id=" + doneRideInfo.getMsg().getDone_ride_id() + "&user_id=" + doneRideInfo.getMsg().getUser_id() + "&payment_id=" + doneRideInfo.getMsg().getPayment_option_id() + "&payment_method=" + "Cash" + "&payment_platform=" + "Android" + "&payment_amount=" + doneRideInfo.getMsg().getTotal_payable_amount().replace("" + sessionManager.getCurrencyCode(), "") + "&payment_date_time=" + "Anything" + "&payment_status=" + "Anything" + "&language_id=1", true, ApiManager.ACTION_SHOW_TOP_BAR);

            }else {
                //for unsuccessful payment, close the webview and show the payment screen to the user
                Toast.makeText(this, "Transaction is unsuccessful. Please try again!", Toast.LENGTH_SHORT).show();
                webLayout.setVisibility(View.GONE);
            }

            Log.e("RESULT_CONFIRM_PAYMENT---", String.valueOf(result));
        }
    }


    @Override
    public void onFetchResultZero(String s) {

    }


    public void setpaymentAccordingly(String mode, String payment_success_status) throws Exception {

        if (mode.equals("1")) {  // cash selected at ride booking time
            cashLayout.setVisibility(View.VISIBLE);
            paymentFailedLayout.setVisibility(View.GONE);
            onlinePaymentLayout.setVisibility(View.GONE);
            paymentSuccessLayout.setVisibility(View.GONE);
        }
        if (mode.equals("4")) {  // wallet selected
            cashLayout.setVisibility(View.GONE);
            onlinePaymentLayout.setVisibility(View.GONE);
            if (payment_success_status.equals("0")) {
                paymentFailedLayout.setVisibility(View.VISIBLE);
                paymentSuccessLayout.setVisibility(View.GONE);
            }
            if (payment_success_status.equals("1")) {
                paymentFailedLayout.setVisibility(View.GONE);
                paymentSuccessLayout.setVisibility(View.VISIBLE);
            }
        }
        if (mode.equals("3")) {  // credit card
            cashLayout.setVisibility(View.GONE);
            onlinePaymentLayout.setVisibility(View.GONE);
            if (payment_success_status.equals("0")) {
                paymentFailedLayout.setVisibility(View.VISIBLE);
                paymentSuccessLayout.setVisibility(View.GONE);
            }
            if (payment_success_status.equals("1")) {
                paymentFailedLayout.setVisibility(View.GONE);
                paymentSuccessLayout.setVisibility(View.VISIBLE);
            }
        }
        if (mode.equals("2")) { // paypal selected
            cashLayout.setVisibility(View.GONE);
            paymentFailedLayout.setVisibility(View.GONE);
            if (payment_success_status.equals("0")) {
                onlinePaymentLayout.setVisibility(View.VISIBLE);
                paymentSuccessLayout.setVisibility(View.GONE);
            }
            if (payment_success_status.equals("1")) {
                onlinePaymentLayout.setVisibility(View.GONE);
                paymentSuccessLayout.setVisibility(View.VISIBLE);
            }

        }
    }

   /* private void startPayPalPayment() throws Exception {
        Intent intent = new Intent(PaymentFailedActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, new PayPalPayment(new BigDecimal("" + netPaybleAmount.getText().toString().replace("" + sessionManager.getCurrencyCode(), "")), "USD", "sample item", PayPalPayment.PAYMENT_INTENT_SALE));
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }*/

    private void startPaystackPayment() throws Exception {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("user_id",doneRideInfo.getMsg().getUser_id());
        hashMap.put("email", email);
        hashMap.put("amount",pay_amount);
        hashMap.put("ride_id", doneRideInfo.getMsg().getRide_id());
        hashMap.put("ride_mode", "1");

        Log.d("**HASHMAP==paystack=", String.valueOf(hashMap));

        apiManager.execution_paystack(Config.ApiKeys.KEY_PAY_WITH_PAYSTACK,  Apis.PAY_WITH_PAYSTACK, hashMap, true, ApiManager.ACTION_SHOW_DIALOG);

    }

    private void showpaymentMethod() throws Exception {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PaymentFailedActivity.this);
        builderSingle.setTitle(R.string.Payment_Failed_select_payment_option);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentFailedActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Pay with Card");
        arrayAdapter.add("CASH");

        builderSingle.setNegativeButton("" + this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(PaymentFailedActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle(R.string.are_you_syre_you_want_to_proceed_with);
                builderInner.setPositiveButton("" + PaymentFailedActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (strName.equals("Pay with Card")) {
                            try {
                                startPaystackPayment();
                            } catch (Exception e) {
                                Toast.makeText(PaymentFailedActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (strName.equals("CASH")) {
                            apiManager.execution_method_get("" + Config.ApiKeys.Key_payment_api, "" + Apis.payment + "order_id=" + doneRideInfo.getMsg().getDone_ride_id() + "&user_id=" + doneRideInfo.getMsg().getUser_id() + "&payment_id=" + doneRideInfo.getMsg().getPayment_option_id() + "&payment_method=" + "Cash" + "&payment_platform=" + "Android" + "&payment_amount=" + doneRideInfo.getMsg().getTotal_payable_amount().replace("" + sessionManager.getCurrencyCode(), "") + "&payment_date_time=" + "Anything" + "&payment_status=" + "Anything" + "&language_id=1", true, ApiManager.ACTION_SHOW_TOP_BAR);
                        }
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    @Override
    public void onBackPressed() {
    }


    //for paypal
  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        apiManager.execution_method_get("" + Config.ApiKeys.Key_payment_api, "" + Apis.payment + "order_id=" + doneRideInfo.getMsg().getDone_ride_id() + "&user_id=" + doneRideInfo.getMsg().getUser_id() + "&payment_id=" + doneRideInfo.getMsg().getPayment_option_id() + "&payment_method=" + "paystack" + "&payment_platform=" + "Android" + "&payment_amount=" + doneRideInfo.getMsg().getTotal_payable_amount().replace("" + sessionManager.getCurrencyCode(), "") + "&payment_date_time=" + "Anything" + "&payment_status=" + "Anything" + "&language_id=1", true, ApiManager.ACTION_SHOW_TOP_BAR);

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PaypalConfiguration was submitted. Please see the docs.");
            }
        }
    }*/
/*
  //for paystack

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("RESULT_CODE--", String.valueOf(resultCode));

        switch (REQUEST_CODE_PAYMENT) {
            case 133:
                try {
                    *//*String generatedToken = data.getExtras().getString("A");
                    String generatedTokenAdd = data.getExtras().getString("B");
                    *//*responseCode = data.getExtras().getBoolean("response");
                    Log.d("**responseCode_paymentFailed--", String.valueOf(responseCode));

                    payment_result = data.getExtras().getInt("payment_result");
                    Log.e("PAYMNET__RESULT-----", String.valueOf(payment_result));
                    //  Toast.makeText(this, "receivedData" + responseCode, Toast.LENGTH_SHORT).show();
                    if (payment_result == 1){
                        //set the data
                         apiManager.execution_method_get("" + Config.ApiKeys.Key_payment_api, "" + Apis.payment + "order_id=" + doneRideInfo.getMsg().getDone_ride_id() + "&user_id=" + doneRideInfo.getMsg().getUser_id() + "&payment_id=" + doneRideInfo.getMsg().getPayment_option_id() + "&payment_method=" + "Cash" + "&payment_platform=" + "Android" + "&payment_amount=" + doneRideInfo.getMsg().getTotal_payable_amount().replace("" + sessionManager.getCurrencyCode(), "") + "&payment_date_time=" + "Anything" + "&payment_status=" + "Anything" + "&language_id=1", true, ApiManager.ACTION_SHOW_TOP_BAR);
                    }
                }catch (Exception e){
                   // Toast.makeText(this, "onActivity Result exception  "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            Log.d("**LOAD OVERRIDE URL==",  url);
            Log.d("**LOAD OVERRIDE view==", String.valueOf(view));

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("**LOAD PAGEFINISH URL==",  url);
            Log.d("**created url==", callback_url+"?trxref=" + reference_number + "&reference=" + reference_number);

            if (url.equals(callback_url+"?trxref=" + reference_number + "&reference=" + reference_number) || url.equals("https://standard.paystack.co/close")){
                Log.d("**created url=inside loop=", callback_url+"?trxref=" + reference_number + "&reference=" + reference_number);
                HashMap<String, String> body = new HashMap<String, String>();
                body.put("token", reference_number);
                body.put("user_id", doneRideInfo.getMsg().getUser_id());

                apiManager.execution_method_post(Config.ApiKeys.CONFIRM_PAYMENT,  Apis.CONFIRM_PAYMENT, body, true, ApiManager.ACTION_SHOW_DIALOG);
                Log.e("**API_CONFIRM_PAYMENT===",Apis.CONFIRM_PAYMENT+"token="+reference_number);
                Log.d("**USER_ID===", doneRideInfo.getMsg().getUser_id());

            }
        }
    }


}
