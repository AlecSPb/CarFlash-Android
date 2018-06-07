package com.carflash.user.rentalmodule.taxirentalmodule;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.Add_Card_First;
import com.carflash.user.R;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.rentalmodule.switchdatetimepicker.SwitchDateTimeDialogFragment;
import com.carflash.user.rentalmodule.taxirentalmodule.viewpaymentoption.ViewPaymentOption;

import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class ConfirmRentalActivity extends FragmentActivity implements ApiManager.APIFETCHER, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "ConfirmRentalActivity";
    TextView terms_txt  , car_type_name,  car_models_list,  selected_package_name,  base_fare_txt, base_fare_price , additional_distance_fare_txt , additional_distance_fare_price , additional_time_txt , additional_time_price , coupon_tx , payment_txt  ;
    ImageView car_image ;
    ApiManager apiManager ;
    ProgressDialog progressDialog ;
    Gson gson ;
    private String COUPON_CODE = "";
    private String PAYMENT_ID  = "";
    String LATERDATE, LATERTIME;

    public static Activity activity ;
    ViewPaymentOption viewPaymentOption;
    SessionManager sessionManager ; 

    String paystack_id, paystack_authCode, paystack_last4;


    SwitchDateTimeDialogFragment dateTimeFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder().create();
        activity = this ;
        setContentView(R.layout.activity_confirm_rental);
        apiManager = new ApiManager(this , this , this);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(""+this.getResources().getString(R.string.loading));
        Config.is_confirm_rental_activity_is_open = true ;
        terms_txt = (TextView) findViewById(R.id.terms_txt);
        car_type_name = (TextView) findViewById(R.id.car_type_name);
        car_models_list = (TextView) findViewById(R.id.car_models_list);
        selected_package_name = (TextView) findViewById(R.id.selected_package_name);
        base_fare_txt = (TextView) findViewById(R.id.base_fare_txt);
        base_fare_price = (TextView) findViewById(R.id.base_fare_price);
        additional_distance_fare_txt = (TextView) findViewById(R.id.additional_distance_fare_txt);
        additional_distance_fare_price = (TextView) findViewById(R.id.additional_distance_fare_price);
        additional_time_txt = (TextView) findViewById(R.id.additional_time_txt);
        additional_time_price = (TextView) findViewById(R.id.additional_time_price);
        car_image = (ImageView) findViewById(R.id.car_image);
        coupon_tx = (TextView) findViewById(R.id.coupon_tx);
        payment_txt = (TextView) findViewById(R.id.payment_txt);

        setUpDateTimePicker();

        HashMap<String ,String> data3 = new HashMap<>();
        data3.put("language_id" , "1");
        data3.put("user_id" , ""+Config.User_id);

        apiManager.execution_method_post(Config.ApiKeys.KEY_PaymentOption , ""+ Apis.viewPaymentOption, data3,true , ApiManager.ACTION_DO_NOTHING);


        findViewById(R.id.apply_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ConfirmRentalActivity.this, CouponActivity.class), 101);
                overridePendingTransition(R.anim.animation3, R.anim.animation4);
            }
        });

        findViewById(R.id.package_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmRentalActivity.this , RentalPackageActivity.class));
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finalizeActivity ();
            }
        });


        findViewById(R.id.payment_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogForSelectPayment();
            }
        });


        findViewById(R.id.book_later_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   dateTimeFragment.show(ConfirmRentalActivity.this.getSupportFragmentManager(), "dialog_time");

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(ConfirmRentalActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.setMinDate(calendar);
                dpd.setAccentColor(ConfirmRentalActivity.this.getResources().getColor(R.color.colorPrimary));
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                dpd.show(getFragmentManager(), "Date Picker Dialog");
            }
        });

        findViewById(R.id.ride_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String , String > data = new HashMap<String, String>();
                data.put("booking_type" , "1");
                data.put("pickup_lat" , ""+Config.User_latitude);
                data.put("pickup_long" , ""+Config.User_longitude);
                data.put("pickup_location" , ""+Config.User_location);
                data.put("car_type_id" , ""+Config.SELECTED_RENTAL_CAR_BEAN.getCar_type_id());
                data.put("rentcard_id" , ""+Config.SELECTED_RENTAL_CAR_BEAN.getRentcard_id());
                data.put("user_id" , ""+Config.User_id);
                data.put("coupan_code" , ""+COUPON_CODE);
                data.put("payment_option_id" , ""+PAYMENT_ID);
                apiManager.execution_method_post_withoutcheck(Config.ApiKeys.KEY_RENTAl_Book_car , ""+Config.Base_Url+Apis.book_ride , data,true , ApiManager.ACTION_SHOW_TOP_BAR);

            }
        });
    }



    public static String getmonthname(int val){
        String retutningval ;
        if(val == 0){
            retutningval = "JANUARY";
        }else if (val == 1){
            retutningval = "FEBRUARY";
        }else if (val == 2){
            retutningval = "MARCH";
        }else if (val == 3){
            retutningval = "APRIL";
        }else if (val == 4){
            retutningval = "MAY";
        }else if (val == 5){
            retutningval = "JUNE";
        }else if (val == 6){
            retutningval = "JULY";
        }else if (val == 7){
            retutningval = "AUGUST";
        }else if (val == 8){
            retutningval = "SEPTEMBER";
        }else if (val == 9){
            retutningval = "OCTOBER";
        }else if (val == 10){
            retutningval = "NOVEMBER";
        }else if (val == 11){
            retutningval = "DECEMBER";
        }else {
            retutningval = "WRONG_DATE";
        }
        return retutningval;
    }


    private void setUpDateTimePicker() {
         dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                "Select Date and Time",
                "OK",
                "Cancel"
        );
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.set24HoursMode(true);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(year, month, date).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(year, month, date, hour, minutes).getTime());
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

// Set listener
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                if(((date.getTime() - System.currentTimeMillis() )/(60 * 1000)) >= Config.Minimum_time_for_ride_later){
                    startActivity(new Intent(ConfirmRentalActivity.this, RidelaterConfirmActivity.class)
                       //     .putExtra("booking_date" , ""+Calendar.getInstance().get(Calendar.YEAR)+"-"+getmonthname(date.getMonth())+"-"+date.getDate())
                            .putExtra("booking_date" , ""+date.getYear()+"-"+date.getYear()+"-"+date.getDate())
                            .putExtra("booking_time" , ""+date.getHours()+":"+date.getMinutes())
                            .putExtra("coupon" , ""+COUPON_CODE)
                            .putExtra("payment_id" , ""+PAYMENT_ID));
                }else {
                    Toast.makeText(ConfirmRentalActivity.this, "Please make sure that pickup time is after at least "+Config.Minimum_time_for_ride_later+" minutes from now", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewAccordingSelection ();
    }

    private void finalizeActivity() {
        finish();
        try{
            RentalCarTypeActivity.activity.finish();
        }catch (Exception e){
        }
    }

    @Override
    public void onBackPressed() {
        finalizeActivity();
    }

    private void setViewAccordingSelection() {
        car_type_name.setText(""+Config.SELECTED_RENTAL_CAR_BEAN.getCar_type_name());
        Glide.with(this).load(""+Config.Image_Base_Url+Config.SELECTED_RENTAL_CAR_BEAN.getCar_type_image()).into(car_image);
        selected_package_name.setText(""+Config.SELECTED_PACKAGE_NAME);

        base_fare_txt.setText("includes "+Config.SELECTED_PACKAGE.getRental_category_hours()+" hours "+Config.SELECTED_PACKAGE.getRental_category_kilometer());
        base_fare_price.setText(sessionManager.getCurrencyCode()+Config.SELECTED_RENTAL_CAR_BEAN.getPrice());

        additional_distance_fare_txt.setText("After First "+Config.SELECTED_PACKAGE.getRental_category_kilometer());
        additional_distance_fare_price.setText(""+sessionManager.getCurrencyCode()+Config.SELECTED_RENTAL_CAR_BEAN.getPrice_per_kms());

        additional_time_txt.setText("After First "+Config.SELECTED_PACKAGE.getRental_category_hours()+" hours ");
        additional_time_price.setText(sessionManager.getCurrencyCode()+Config.SELECTED_RENTAL_CAR_BEAN.getPrice_per_hrs());
      //  terms_txt.setText(Html.fromHtml(""+Config.SELECTED_PACKAGE.getRental_category_description()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Config.is_confirm_rental_activity_is_open = false ;
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        Log.e("SCRIPT==", ""+script);

        try{
            switch (APINAME){

            case Config.ApiKeys.KEY_RENTAl_Book_car :
                ResultStatusChecker rs = gson.fromJson("" +script, ResultStatusChecker.class);
                if(rs.getStatus() == 1){
                    Config.PostedRequest_RENTAL = true ;
                    Config.PostedRentalType = 1 ;
                    Toast.makeText(ConfirmRentalActivity.this, ""+rs.getMessage(), Toast.LENGTH_SHORT).show();
                    Config.rental_ride_now_response  = gson.fromJson(""+script , RentalRidenowModel.class);
                    finish();
                }else {
                    Toast.makeText(ConfirmRentalActivity.this, ""+rs.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break ;
            case Config.ApiKeys.KEY_PaymentOption :
                ResultCheck resultCheck;
                resultCheck = gson.fromJson(""+script, ResultCheck.class);
                if (resultCheck.result.equals("1")) {
                    viewPaymentOption = gson.fromJson(""+script, ViewPaymentOption.class);
                    payment_txt.setText(""+viewPaymentOption.getMsg().get(0).getPaymentOptionName());
                  //  PAYMENT_ID = viewPaymentOption.getMsg().get(0).getPaymentOptionId();
                } else {
                    Toast.makeText(this, "Problem in fetching payment option", Toast.LENGTH_SHORT).show();
                }
                break ;
        }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }



    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res == Activity.RESULT_OK) {
            try {
                if (req == 101) {
                    COUPON_CODE = data.getStringExtra("coupon_code");
                    coupon_tx.setText("Coupon Applied \n"+data.getStringExtra("coupon_code"));
                }   if (req == 106) {
                //    payment_txt.setText("Pay With Card");
                //    PAYMENT_ID = "3";
                    paystack_id = data.getExtras().getString("paystack_id");
                    paystack_authCode = data.getExtras().getString("paystack_authCode");
                    paystack_last4 = data.getExtras().getString("paystack_last4");
                    PAYMENT_ID = paystack_authCode;

                    payment_txt.setText("**** **** **** "+paystack_last4);
                }
            } catch (Exception e) {
                Log.e("res ", e.toString());
            }
        }
    }




    public void dialogForSelectPayment() {
        final Dialog dialog = new Dialog(ConfirmRentalActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_payment_option);

        ListView lv_payment_option = (ListView) dialog.findViewById(R.id.lv_payment_option);
        try {
            if(viewPaymentOption.getMsg().size() >0){
                lv_payment_option.setAdapter(new PaymentAdapter(ConfirmRentalActivity.this, viewPaymentOption));
            }
        }catch (Exception e){

        }


        lv_payment_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (viewPaymentOption.getMsg().get(position).getPaymentOptionName().equals("Pay With Card")) {

                    PAYMENT_ID = viewPaymentOption.getMsg().get(position).getPaymentOptionId();
                    startActivityForResult(new Intent(ConfirmRentalActivity.this, Add_Card_First.class).putExtra("payment", "rental"), 106);

                } else {

                payment_txt.setText("" + viewPaymentOption.getMsg().get(position).getPaymentOptionName());
                PAYMENT_ID = viewPaymentOption.getMsg().get(position).getPaymentOptionId();

                }
                   /* payment_txt.setText(""+viewPaymentOption.getMsg().get(position).getPaymentOptionName());
                    PAYMENT_ID = viewPaymentOption.getMsg().get(position).getPaymentOptionId();
*/
                    Log.d("***PAYMENT_ID===", PAYMENT_ID);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        LATERDATE = year + "-" + month + "-" + dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        if(Calendar.getInstance().getTime().getDate() == dayOfMonth){
            Date currentTime = Calendar.getInstance().getTime();
            Timepoint timepoint = new Timepoint(currentTime.getHours()+2 , currentTime.getMinutes() , currentTime.getSeconds());
            tpd.setMinTime(timepoint);
        }

        tpd.setAccentColor(this.getResources().getColor(R.color.colorPrimary));
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });
        tpd.show(getFragmentManager(), "Time Picker Dialog");
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        LATERTIME = hourString + ":" + minuteString;


        startActivity(new Intent(ConfirmRentalActivity.this, RidelaterConfirmActivity.class)
                .putExtra("booking_date", "" + LATERDATE)
                .putExtra("booking_time", "" +LATERTIME)
                .putExtra("coupon" , ""+COUPON_CODE)
                .putExtra("payment_id" , ""+PAYMENT_ID));

    }

}
