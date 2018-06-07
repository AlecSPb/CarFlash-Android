package com.carflash.user.rentalmodule.taxirentalmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.carflash.user.R;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.samwork.Config;
import com.bumptech.glide.Glide;

public class RentalCarTypeActivity extends Activity {

    ListView listView ;
    LinearLayout root ;

    TextView selected_package_name ;
    public static Activity activity ;
    SessionManager sessionManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this ;
        setContentView(R.layout.activity_rental_car_type);
        listView = (ListView) findViewById(R.id.list);
        root = (LinearLayout) findViewById(R.id.root);
        selected_package_name = (TextView) findViewById(R.id.selected_package_name);

        selected_package_name.setText(""+ Config.SELECTED_PACKAGE_NAME);
        listView.setAdapter(new ListAdapter());
        sessionManager = new SessionManager(this);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        root.setMinimumWidth(width- 100);


        findViewById(R.id.package_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RentalCarTypeActivity.this , RentalPackageActivity.class));
                finish();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Config.SELECTED_RENTAL_CAR_BEAN  = Config.response.getDetails().get(Config.SELECTED_PACKAGE_POSITION).getRental_Pakage_Car().get(position);
                if(!Config.is_confirm_rental_activity_is_open){
                    startActivity(new Intent(RentalCarTypeActivity.this , ConfirmRentalActivity.class));
                }
                finish();
            }
        });
    }



    public class ListAdapter extends BaseAdapter {

        public ListAdapter(){

        }

        @Override
        public int getCount() {
            return Config.response.getDetails().get(Config.SELECTED_PACKAGE_POSITION).getRental_Pakage_Car().size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = LayoutInflater.from(RentalCarTypeActivity.this).inflate(R.layout.item_rental_car_type, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            TextView car_typeee = (TextView) view.findViewById(R.id.car_typerr);
            TextView price = (TextView) view.findViewById(R.id.price);

            Glide.with(RentalCarTypeActivity.this).load(""+ Config.Image_Base_Url+ Config.response.getDetails().get(Config.SELECTED_PACKAGE_POSITION).getRental_Pakage_Car().get(position).getCar_type_image()).into(image);
            price.setText(""+sessionManager.getCurrencyCode()+ Config.response.getDetails().get(Config.SELECTED_PACKAGE_POSITION).getRental_Pakage_Car().get(position).getPrice());
            car_typeee.setText(""+ Config.response.getDetails().get(Config.SELECTED_PACKAGE_POSITION).getRental_Pakage_Car().get(position).getCar_type_name());
            return view;


        }
    }



}
