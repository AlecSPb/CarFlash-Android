package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.carflash.user.adapter.CarAdapter;
import com.carflash.user.adapter.CityAdapter;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.RateCard;
import com.carflash.user.models.ViewCarType;
import com.carflash.user.models.ViewCity;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RateCardActivity extends AppCompatActivity implements ApiManager.APIFETCHER{

    ApiManager apiManager ;
    LinearLayout back, ll_city, ll_car;
    public static Activity ratecard;
    String city_id, car_id, car_type_name, city_name;
    TextView tv_city, tv_car_type, tv_base_price_miles, tv_price_per_mile, tv_base_price_minute, tv_price_per_minute, tv_base_price_waiting, tv_price_per_minute_waiting , peak_time_charges , night_time_charges;

    String  carTypeCheck = "";

    String cityLocation_name = "", car_type_id = "", car_name = "";

    LanguageManager languageManager;

    ViewCity viewCity;
    ViewCarType viewCarType;

    String language_id;
    SessionManager sessionManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_card);
        ratecard = this;
        apiManager = new ApiManager(this , this , this );
        sessionManager = new SessionManager(this);
        back = (LinearLayout) findViewById(R.id.bck);
        ll_car = (LinearLayout) findViewById(R.id.ll_car);
        ll_city = (LinearLayout) findViewById(R.id.ll_city);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_car_type = (TextView) findViewById(R.id.tv_car_type);
        tv_base_price_miles = (TextView) findViewById(R.id.tv_base_price_miles);
        tv_price_per_mile = (TextView) findViewById(R.id.tv_price_per_mile);

        tv_base_price_minute = (TextView) findViewById(R.id.tv_base_price_minute);
        tv_price_per_minute = (TextView) findViewById(R.id.tv_price_per_minute);

        tv_base_price_waiting = (TextView) findViewById(R.id.tv_base_price_waiting);
        tv_price_per_minute_waiting = (TextView) findViewById(R.id.tv_price_per_minute_waiting);

        peak_time_charges = (TextView) findViewById(R.id.peak_time_charges);
        night_time_charges = (TextView) findViewById(R.id.night_time_charges);

        cityLocation_name = super.getIntent().getExtras().getString("city_name");
        car_type_id = super.getIntent().getExtras().getString("car_type_id");
        car_type_name = super.getIntent().getExtras().getString("car_type_name");

        languageManager = new LanguageManager(this);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);

   //     tv_car_type.setText(car_type_name);
    //    tv_city.setText("Select City");


        apiManager.execution_method_get(""+ Config.ApiKeys.Key_Virew_Cities, ""+ Apis.viewCities+"language_id="+language_id, true,ApiManager.ACTION_SHOW_DIALOG);
        apiManager.execution_method_get(""+Config.ApiKeys.Key_Virew_Rate_Card_Cities , ""+Apis.viewRateCardCity+"city_id="+getIntent().getExtras().getString("city_id")+"&car_type_id="+car_type_id+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_DIALOG);




        ll_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (viewCity.getMsg().size()>0) {
                        final Dialog dialog = new Dialog(RateCardActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.CENTER);
                        dialog.setContentView(R.layout.dialog_for_city);

                        ListView lv_cities = (ListView) dialog.findViewById(R.id.lv_cities);
                        lv_cities.setAdapter(new CityAdapter(RateCardActivity.this, viewCity));

                        lv_cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                  city_id = viewCity.getMsg().get(position).getCity_id();
                                    city_name = viewCity.getMsg().get(position).getCity_name();
                                tv_city.setText(city_name);
                                apiManager.execution_method_get(""+ Config.ApiKeys.Key_View_car_by_Cities, ""+ Apis.viewCarByCities+"city_id="+city_id+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_DIALOG);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        Toast.makeText(RateCardActivity.this, getString(R.string.no_record_found), Toast.LENGTH_SHORT).show();
                    }

            }
        });

        ll_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (carTypeCheck.equals("1")) {
                    final Dialog dialog = new Dialog(RateCardActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    dialog.setContentView(R.layout.dialog_for_car);

                    ListView lv_cars = (ListView) dialog.findViewById(R.id.lv_cars);
                    lv_cars.setAdapter(new CarAdapter(RateCardActivity.this, viewCarType));

                    lv_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            car_type_id = viewCarType.getMsg().get(position).getCar_type_id();
                                car_name = viewCarType.getMsg().get(position).getCar_type_name();

                            tv_car_type.setText(car_name);
                            dialog.dismiss();
                            apiManager.execution_method_get(""+Config.ApiKeys.Key_Virew_Rate_Card_Cities , ""+Apis.viewRateCardCity+"city_id="+city_id+"&car_type_id="+car_type_id+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_DIALOG);

                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(RateCardActivity.this, getString(R.string.no_record_found), Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }






    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals(""+Config.ApiKeys.Key_Virew_Cities)) {
                viewCity = gson.fromJson(""+script, ViewCity.class);
            }

            if (APINAME.equals(""+Config.ApiKeys.Key_View_car_by_Cities)) {
                carTypeCheck = "1";
                viewCarType = gson.fromJson(""+script, ViewCarType.class);
            }

            if (APINAME.equals(""+Config.ApiKeys.Key_Virew_Rate_Card_Cities)) {
                RateCard rateCard_response;
                rateCard_response = gson.fromJson(""+script, RateCard.class);
                tv_base_price_miles.setText( sessionManager.getCurrencyCode()+rateCard_response.getDetails().getBase_distance_price()+" "+getString(R.string.RATE_CARD_ACTIVITY___for)+rateCard_response.getDetails().getBase_distance()+" "+rateCard_response.getDetails().getDistance_unit());
                tv_price_per_mile.setText( sessionManager.getCurrencyCode()+ rateCard_response.getDetails().getBase_price_per_unit()+" "+ getString(R.string.RATE_CARD_ACTIVITY__per)+rateCard_response.getDetails().getDistance_unit());
                tv_base_price_minute.setText(  rateCard_response.getDetails().getFree_ride_minutes()+" "+getString(R.string.RATE_CARD_ACTIVITY__min));
                tv_price_per_minute.setText( sessionManager.getCurrencyCode()+rateCard_response.getDetails().getPrice_per_ride_minute() +" "+ getString(R.string.RATE_CARD_ACTIVITY__per_min));
                tv_base_price_waiting.setText( rateCard_response.getDetails().getFree_waiting_time()+" "+getString(R.string.RATE_CARD_ACTIVITY__min));
                tv_price_per_minute_waiting.setText( sessionManager.getCurrencyCode() + rateCard_response.getDetails().getWating_price_minute() +" "+ getString(R.string.RATE_CARD_ACTIVITY__per_min));
                peak_time_charges.setText(""+sessionManager.getCurrencyCode()+rateCard_response.getDetails().getPeak_time_charge());
                night_time_charges.setText(""+sessionManager.getCurrencyCode()+rateCard_response.getDetails().getNight_time_charge());

            }
        }catch (Exception e){
          //  Toast.makeText(ratecard, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
