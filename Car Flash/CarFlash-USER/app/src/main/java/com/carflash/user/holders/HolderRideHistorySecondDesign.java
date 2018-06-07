package com.carflash.user.holders;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.carflash.user.R;
import com.carflash.user.RidesSelectedActivity;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.NewRideHistoryModel;
import com.carflash.user.others.MapUtils;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.sam.placer.PlaceHolderView;
import com.sam.placer.annotations.Click;
import com.sam.placer.annotations.Layout;
import com.sam.placer.annotations.Resolve;
import com.sam.placer.annotations.View;

/**
 * Created by lenovo-pc on 3/26/2017.
 */

@Layout(R.layout.holder_ride_histoy_second_layout)
public class HolderRideHistorySecondDesign {


    @View(R.id.image_start) private ImageView image_start;
    @View(R.id.image_destination) private ImageView image_destination;
    @View(R.id.start_txt) private TextView start_txt;

    @View(R.id.end_txt) private TextView end_txt;

    @View(R.id.time_txt) private TextView time_txt;
    @View(R.id.car_name_txt) private TextView car_name_txt;
    @View(R.id.amount_txt) private TextView amount_txt;
    @View(R.id.ride_status_txt) private TextView ride_status_txt;
    @View(R.id.car_image) private ImageView car_image;





    private Context mContext;
    private PlaceHolderView mPlaceHolderView;
    NewRideHistoryModel.DetailsBean.NormalRideBean mMsg ;
    SessionManager sessionManager ;

    public HolderRideHistorySecondDesign(Context context, PlaceHolderView placeHolderView, NewRideHistoryModel.DetailsBean.NormalRideBean msg , SessionManager sessionManager ) {
        mContext = context;
        mPlaceHolderView = placeHolderView;
        this.sessionManager = sessionManager ;
        mMsg = msg ;
    }

    @Resolve
    private void onResolved() {
        if(mMsg.getRide_type().equals("2")){  // that is for later type ride
            time_txt.setText( mMsg.getLater_date()+" "+mMsg.getLater_time());
        }if (mMsg.getRide_type().equals("1")){  // That is of normal ride type
            time_txt.setText(""+ mMsg.getRide_date()+" "+mMsg.getRide_time());
        }else {

        }

        car_name_txt.setText(""+mMsg.getCar_type_name());
        if(!mMsg.getTotal_amount().equals("")){
            amount_txt.setText(sessionManager.getCurrencyCode() + mMsg.getTotal_amount());
        }
        ride_status_txt.setText(""+Config.getStatustext(mMsg.getRide_status(), mContext));

        start_txt.setText(""+mMsg.getPickup_location());
        end_txt.setText(""+mMsg.getDrop_location());


        Glide.with(mContext).load(MapUtils.getStaticMapImageUrl(""+mMsg.getPickup_lat() , ""+mMsg.getPickup_long() , mContext)).into(image_start);
        Glide.with(mContext).load(MapUtils.getStaticMapImageUrl(""+mMsg.getDrop_lat() , ""+mMsg.getDrop_long() , mContext)).into(image_destination);
        Glide.with(mContext).load(""+ Apis.imageDomain+mMsg.getCar_type_image()).into(car_image);

    }




    @Click(R.id.root)
    private void OnClickRoot(){
        mContext.startActivity(new Intent(mContext, RidesSelectedActivity.class)
                .putExtra("ride_id", mMsg.getRide_id())
                .putExtra("ride_status", mMsg.getRide_status())
                .putExtra("date_time", mMsg.getRide_date()+""+mMsg.getRide_time())
                .putExtra("ride_type", mMsg.getRide_time())
                .putExtra("image" , "")
                .putExtra("pick_lat" , ""+mMsg.getPickup_lat())
                .putExtra("pick_long" , ""+mMsg.getPickup_long())
                .putExtra("drop_lat" , ""+mMsg.getDrop_lat())
                .putExtra("drop_long" , ""+mMsg.getDrop_long())
                .putExtra("ride_mode" , "1"));   // main url

    }
}
