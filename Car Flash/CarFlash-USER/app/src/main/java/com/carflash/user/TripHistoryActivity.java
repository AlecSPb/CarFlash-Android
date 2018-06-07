package com.carflash.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.carflash.user.holders.HolderRideHistoryRentalDesign;
import com.carflash.user.holders.HolderRideHistorySecondDesign;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.NewRideHistoryModel;
import com.carflash.user.samir.customviews.TypeFaceTextMonixRegular;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.sam.placer.PlaceHolderView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TripHistoryActivity extends BaseActivity implements ApiManager.APIFETCHER {


    private static final String TAG = "TripHistoryActivity";
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.activity_name)
    TypeFaceTextMonixRegular activityName;
    @Bind(R.id.root_action_bar)
    LinearLayout rootActionBar;
    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @Bind(R.id.container)
    ViewPager container;
    @Bind(R.id.root)
    LinearLayout root;

    private PagerAdapter mSectionsPagerAdapter;
    ApiManager apiManager;
    static SessionManager sessionManager;
    public static Activity activity;
    ProgressDialog progressDialog;
    public static Activity ridesActivity;
    Gson gson;
    public static NewRideHistoryModel ride_history_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        ButterKnife.bind(this);
        ridesActivity = this;
        apiManager = new ApiManager(this, this, this);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getResources().getString(R.string.loading));
        activity = this;
        gson = new GsonBuilder().create();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        activityName.setText(this.getResources().getString(R.string.your_trips));

        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        container.setAdapter(mSectionsPagerAdapter);
        viewpagertab.setViewPager(container);


        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", "" + sessionManager.getUserDetails().get(SessionManager.USER_ID));
        apiManager.execution_method_post("" + Config.ApiKeys.KEY_REST_RIDE_HISTORY, "" + Apis.RideHistory, data, true, ApiManager.ACTION_SHOW_DIALOG);
    }

    @Override
    protected void getConnectivityStatus(boolean val) {

    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {

        switch (APINAME) {
            case Config.ApiKeys.KEY_REST_RIDE_HISTORY:
                try {
                    ride_history_response = gson.fromJson("" + script, NewRideHistoryModel.class);
                    mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());
                    container.setAdapter(mSectionsPagerAdapter);
                    viewpagertab.setViewPager(container);
                } catch (Exception e) {
                    Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("" + TAG, "Exception caught while parsing ==>" + e.getMessage());
                }
                break;
        }
    }

    @Override
    public void onFetchResultZero(String s) {
        
    }


    public static class ActiveFragment extends Fragment {


        @Bind(R.id.place_holder)
        PlaceHolderView placeHolder;

        public ActiveFragment() {
        }

        public static ActiveFragment newInstance() {
            ActiveFragment fragment = new ActiveFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.active_fragment, container, false);
            ButterKnife.bind(this, rootView);

            try{for (int i = 0; i < ride_history_response.getDetails().size(); i++) {
                if (ride_history_response.getDetails().get(i).getRide_mode().equals("1")) {  // normal ride
                    if (ride_history_response.getDetails().get(i).getNormal_Ride().getRide_status().equals(Config.Status.PARTIAL_ACCEPT) ||
                            ride_history_response.getDetails().get(i).getNormal_Ride().getRide_status().equals(Config.Status.NORMAL_ACCEPTED_BY_DRIVER) ||
                            ride_history_response.getDetails().get(i).getNormal_Ride().getRide_status().equals(Config.Status.NORMAL_ARRIVED) ||
                            ride_history_response.getDetails().get(i).getNormal_Ride().getRide_status().equals(Config.Status.NORMAL_RIDE_STARTED) ||
                            ride_history_response.getDetails().get(i).getNormal_Ride().getRide_status().equals(Config.Status.NORMAL_BOOKING)) {
                        placeHolder.addView(new HolderRideHistorySecondDesign(getActivity(), placeHolder, ride_history_response.getDetails().get(i).getNormal_Ride(), sessionManager));
                    }
                } else if (ride_history_response.getDetails().get(i).getRide_mode().equals("2")) {  // rental rides
                    if (ride_history_response.getDetails().get(i).getRental_Ride().getBooking_status().equals("" + Config.Status.RENTAL_ACCEPTED) ||
                            ride_history_response.getDetails().get(i).getRental_Ride().getBooking_status().equals("" + Config.Status.RENTAL_ARRIVED) ||
                            ride_history_response.getDetails().get(i).getRental_Ride().getBooking_status().equals("" + Config.Status.RENTAl_RIDE_STARTED) ||
                            ride_history_response.getDetails().get(i).getRental_Ride().getBooking_status().equals("" + Config.Status.PARTIAL_ACCEPT) ||
                            ride_history_response.getDetails().get(i).getRental_Ride().getBooking_status().equals("" + Config.Status.RENTAL_BOOKED)) {
                        placeHolder.addView(new HolderRideHistoryRentalDesign(getActivity(), placeHolder, ride_history_response.getDetails().get(i).getRental_Ride(), sessionManager));
                    }
                }
            }}catch (Exception e){}


            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }
    }


    public static class PastFragment extends Fragment {

        @Bind(R.id.place_holder)
        PlaceHolderView placeHolder;

        public PastFragment() {
        }

        public static PastFragment newInstance(int sectionNumber) {
            PastFragment fragment = new PastFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.past_fragment, container, false);
            ButterKnife.bind(this, rootView);

            try{for (int i = 0; i < ride_history_response.getDetails().size(); i++) {
                if (ride_history_response.getDetails().get(i).getRide_mode().equals("1")) {  // normal ride
                    if (ride_history_response.getDetails().get(i).getNormal_Ride().getRide_status().equals("" + Config.Status.NORMAL_RIDE_END) ||
                            ride_history_response.getDetails().get(i).getNormal_Ride().getRide_status().equals("" + Config.Status.NORMAL_CANCEL_BY_DRIVER) ||
                            ride_history_response.getDetails().get(i).getNormal_Ride().getRide_status().equals("" + Config.Status.NORMAL_CANCEL_BY_USER) ||
                            ride_history_response.getDetails().get(i).getNormal_Ride().getRide_status().equals("" + Config.Status.NORMAL_RIDE_CANCEl_BY_ADMIN)) {
                        placeHolder.addView(new HolderRideHistorySecondDesign(getActivity(), placeHolder, ride_history_response.getDetails().get(i).getNormal_Ride(), sessionManager));
                    }
                } else if (ride_history_response.getDetails().get(i).getRide_mode().equals("2")) {  // rental rides
                    if (ride_history_response.getDetails().get(i).getRental_Ride().getBooking_status().equals("" + Config.Status.RENTAL_RIDE_END) ||
                            ride_history_response.getDetails().get(i).getRental_Ride().getBooking_status().equals("" + Config.Status.RENTAL_RIDE_CANCEL_BY_USER) ||
                            ride_history_response.getDetails().get(i).getRental_Ride().getBooking_status().equals("" + Config.Status.RENTAL_RIDE_CANCEL_BY_DRIVER) ||
                            ride_history_response.getDetails().get(i).getRental_Ride().getBooking_status().equals("" + Config.Status.RENTAL_RIDE_CANCEl_BY_ADMIN)) {
                        placeHolder.addView(new HolderRideHistoryRentalDesign(getActivity(), placeHolder, ride_history_response.getDetails().get(i).getRental_Ride(), sessionManager));
                    }
                }
            }}catch (Exception e){}
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }
    }


    public class PagerAdapter extends FragmentPagerAdapter {
        String[] fragmens_name = {getString(R.string.on_going_trips), getString(R.string.past)};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return ActiveFragment.newInstance();
            } else {
                return PastFragment.newInstance(position);
            }
        }

        @Override
        public int getCount() {
            return fragmens_name.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmens_name[position];
        }
    }
}
