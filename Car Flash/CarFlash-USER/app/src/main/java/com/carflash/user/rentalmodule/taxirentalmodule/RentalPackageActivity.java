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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.carflash.user.R;
import com.carflash.user.samwork.Config;


public class RentalPackageActivity extends Activity {

    LinearLayout root ;
    ListView list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_package);
        list = (ListView) findViewById(R.id.list);
        root = (LinearLayout) findViewById(R.id.root);
        list.setAdapter(new ListAdapter());


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        root.setMinimumWidth(width- 100);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Config.SELECTED_PACKAGE = Config.response.getDetails().get(position);
                Config.SELECTED_PACKAGE_ID = Config.response.getDetails().get(position).getRental_category_id();
                Config.SELECTED_PACKAGE_NAME = Config.response.getDetails().get(position).getRental_category();
                Config.SELECTED_PACKAGE_POSITION = position ;
                startActivity(new Intent(RentalPackageActivity.this ,RentalCarTypeActivity.class));
                finish();
            }
        });

    }


    public class ListAdapter extends BaseAdapter{

        public ListAdapter(){
        }

        @Override
        public int getCount() {
            return Config.response.getDetails().size();
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
            view = LayoutInflater.from(RentalPackageActivity.this).inflate(R.layout.item_text, null);
            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText(""+ Config.response.getDetails().get(position).getRental_category());
            return view;
        }
    }
}
